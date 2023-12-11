package com.epam.xstack.controller;

import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.RequestTraineeDTO;
import com.epam.xstack.model.dto.TraineeDTO;
import com.epam.xstack.service.TraineeService;
import com.epam.xstack.utils.TraineeMapper;
import com.epam.xstack.utils.TrainerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/trainee")
@Tag(name = "TraineeController", description = "Operations pertaining to trainees in the application")
public class TraineeController {
    private final TraineeService traineeService;

    @PostMapping
    @Operation(summary = "Register a new trainee", responses = {
            @ApiResponse(responseCode = "201", description = "Successfully created trainee",
                    content = @Content(schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description = "Couldn't create trainee"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> register(@Valid @RequestBody RequestTraineeDTO traineeDTO) {
        return traineeService.create(traineeDTO)
                .map((Function<Trainee, ResponseEntity<?>>) trainee -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of(
                                "username", trainee.getUser().getUsername(),
                                "password", trainee.getUser().getPassword()
                        )
                )).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't save trainee."));
    }

    @GetMapping("/{username}")
    @RolesAllowed(value = "user")
    @Operation(summary = "Get trainee profile by username")
    public ResponseEntity<?> getProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$") String username) {
        Trainee trainee = traineeService.select(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
        return ResponseEntity.ok(TraineeMapper.INSTANCE.toDto(trainee));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainee profile")
    public ResponseEntity<?> updateProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$") String username, @RequestBody TraineeDTO dto) {
        Trainee trainee = traineeService.update(username, dto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
        return ResponseEntity.ok(TraineeMapper.INSTANCE.toDto(trainee));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete a trainee profile")
    public ResponseEntity<?> deleteProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$") String username) {
        if (!traineeService.deleteByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found");
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update trainee's trainers")
    public ResponseEntity<?> updateTrainers(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
                                            String username,
                                            @Valid
                                            @ParameterObject @RequestBody List<@Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.") String> trainerUsernames) {
        List<Trainer> updatedTrainers = traineeService.updateTrainers(username, trainerUsernames)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
        return ResponseEntity.ok(TrainerMapper.INSTANCE.toDtoForList(updatedTrainers));
    }
}



