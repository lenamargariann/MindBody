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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
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
    @ResponseStatus(HttpStatus.OK)
    public String login(HttpServletResponse response, HttpServletRequest request, @RequestBody UserDTO userDTO) {
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
        return "Successfully logged in.";
    }


    @PutMapping("/change-password")
    @Operation(summary = "Change user's password", responses = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Password change failed")
    })
    @ResponseStatus(HttpStatus.OK)
    public String changePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO) {
        userService.changePassword(passwordChangeDTO.getUsername(), passwordChangeDTO.getOldPassword(), passwordChangeDTO.getNewPassword());
        return "Password changed successfully";
    }

    @PatchMapping("/activate/{username}")
    @Operation(summary = "Activate or deactivate a user account")
    @ResponseStatus(HttpStatus.OK)
    public String activate(@Parameter(description = "Username of the user") @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
                           @PathVariable String username,
                           @Parameter(description = "Boolean value to activate or deactivate") @RequestParam("activate") boolean activate) {
        return userService.activate(username, activate) ? "User is activated." : "de-activated.";

    }


}

