package com.epam.xstack.controller;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/trainer")
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
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@Valid @RequestBody RequestTrainerDTO trainerDTO) {
        Trainer trainer = trainerService.create(trainerDTO);
        return Map.of("username", trainer.getUser().getUsername(),
                "password", trainer.getUser().getPassword());

    }


    @GetMapping("/{username}")
    @Operation(summary = "Get trainer profile by username")
    @ResponseStatus(HttpStatus.OK)
    public TrainerDTO getProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.") String username) {
        return TrainerMapper.INSTANCE.toDto(trainerService.select(username));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainer profile")
    @ResponseStatus(HttpStatus.OK)
    public TrainerDTO updateTrainerProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.") String username,
                                           @Valid @RequestBody TrainerDTO updateDTO) {
        return TrainerMapper.INSTANCE.toDto(trainerService.update(username, updateDTO));

    }

    @GetMapping("/not-assigned")
    @Operation(summary = "List trainers not assigned to a trainee")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerDTO> listNotAssigned() {
        return TrainerMapper.INSTANCE.toDtoForList(trainerService.listNotAssigned());
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete a trainer profile")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Trainer deleted!")
    public void deleteProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$") String username) {
        trainerService.deleteByUsername(username);
    }
}


