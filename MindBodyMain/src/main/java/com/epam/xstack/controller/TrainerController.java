package com.epam.xstack.controller;

import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.RequestTrainerDTO;
import com.epam.xstack.model.dto.TrainerDTO;
import com.epam.xstack.service.TrainerService;
import com.epam.xstack.utils.TrainerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trainer")
@Tag(name = "TrainerController", description = "Operations pertaining to trainers in the application")
public class TrainerController {
    private final TrainerService trainerService;


    @PostMapping
    @Operation(summary = "Register a new trainer", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created trainee",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "Couldn't create trainee"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> register(@Valid @RequestBody RequestTrainerDTO trainerDTO) {
        return trainerService.create(trainerDTO)
                .map((Function<Trainer, ResponseEntity<?>>) trainer -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of(
                                        "username", trainer.getUser().getUsername(),
                                        "password", trainer.getUser().getPassword()
                                )
                        )).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't save trainer."));

    }


    @GetMapping("/{username}")
    @Operation(summary = "Get trainer profile by username")
    public ResponseEntity<?> getProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.") String username) {
        return trainerService.select(username)
                .map(trainer -> ResponseEntity.ok(TrainerMapper.INSTANCE.toDto(trainer)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainer profile")
    public ResponseEntity<?> updateTrainerProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.") String username,
                                                  @Valid @RequestBody TrainerDTO updateDTO) {
        return trainerService.update(username, updateDTO)
                .map(trainer -> ResponseEntity.ok(TrainerMapper.INSTANCE.toDto(trainer)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"));

    }

    @GetMapping("/not-assigned")
    @Operation(summary = "List trainers not assigned to a trainee")
    public ResponseEntity<?> listNotAssigned() {
        return trainerService.listNotAssigned().isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(TrainerMapper.INSTANCE.toDtoForList(trainerService.listNotAssigned()));
    }
}


