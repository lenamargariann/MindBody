package com.epam.xstack.controller;

import com.epam.xstack.model.dto.PasswordChangeDTO;
import com.epam.xstack.model.dto.UserDTO;
import com.epam.xstack.security.JwtTokenProvider;
import com.epam.xstack.service.UserService;
import com.epam.xstack.service.impl.LoginAttemptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "UserController", description = "Operations pertaining to users")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    @Operation(summary = "Login a user", responses = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> login(HttpServletResponse response, HttpServletRequest request, @RequestBody UserDTO userDTO) {
        if (loginAttemptService.isBlocked(request))
            throw new AccessDeniedException("Exceeded login attempts.Try again in 5 minutes.");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword())
        );
        String jwt = jwtTokenProvider.createToken(authentication);
        Cookie jwtCookie = new Cookie("Bearer", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) jwtTokenProvider.getValidityInMilliseconds() / 1000);
        response.addCookie(jwtCookie);
        loginAttemptService.loginSucceeded(request);
        return ResponseEntity.ok("Successfully logged in.");


    }

    @Operation(summary = "Logout",
            responses = @ApiResponse(responseCode = "200", description = "Logout successful"))
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            jwtTokenProvider.blacklistToken(token, jwtTokenProvider.getExpirationDateFromToken(token).getTime());
            return ResponseEntity.ok().body("Successfully logged out.");
        }
        return ResponseEntity.badRequest().body("Invalid token.");
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change user's password", responses = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Password change failed")
    })
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        if (!userService.changePassword(passwordChangeDTO.getUsername(), passwordChangeDTO.getOldPassword(), passwordChangeDTO.getNewPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password change failed");
        return ResponseEntity.ok("Password changed successfully");
    }

    @PatchMapping("/activate/{username}")
    @Operation(summary = "Activate or deactivate a user account")
    public ResponseEntity<?> activate(@Parameter(description = "Username of the user") @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
                                      @PathVariable String username,
                                      @Parameter(description = "Boolean value to activate or deactivate") @RequestParam("activate") boolean activate) {
        return userService.activate(username, activate) ? (ResponseEntity.ok("User is " + (activate ? "activated." : "de-activated.")))
                : ResponseEntity.badRequest().body("Something went wrong.");

    }


}

