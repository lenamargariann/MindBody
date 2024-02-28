package com.epam.xstack.controller;

import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.dto.RequestTraineeDTO;
import com.epam.xstack.model.dto.TraineeDTO;
import com.epam.xstack.model.dto.TrainerDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/trainee")
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
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> register(@Valid @RequestBody RequestTraineeDTO traineeDTO) {
        Trainee trainee = traineeService.create(traineeDTO);
        return Map.of("username", trainee.getUser().getUsername(), "password", trainee.getUser().getPassword());
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get trainee profile by username")
    @ResponseStatus(HttpStatus.OK)
    public TraineeDTO getProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$") String username) {
        return TraineeMapper.INSTANCE.toDto(traineeService.select(username));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update trainee profile")
    @ResponseStatus(HttpStatus.OK)
    public TraineeDTO updateProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$") String username, @RequestBody TraineeDTO dto) {
        return TraineeMapper.INSTANCE.toDto(traineeService.update(username, dto));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete a trainee profile")
    @ResponseStatus(value = HttpStatus.NO_CONTENT,reason = "Trainee deleted!")
    public void deleteProfile(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$") String username) {
        traineeService.deleteByUsername(username);
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update trainee's trainers")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerDTO> updateTrainers(@PathVariable @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
                                           String username,
                                           @Valid
                                           @ParameterObject @RequestBody List<@Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.") String> trainerUsernames) {
        return TrainerMapper.INSTANCE.toDtoForList(traineeService.updateTrainers(username, trainerUsernames));
    }
}



