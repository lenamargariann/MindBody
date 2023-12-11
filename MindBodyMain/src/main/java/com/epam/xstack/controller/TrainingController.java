package com.epam.xstack.controller;

import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainingDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;
import com.epam.xstack.service.TraineeService;
import com.epam.xstack.service.TrainerService;
import com.epam.xstack.service.TrainingService;
import com.epam.xstack.utils.TrainingMapper;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/training")
@Tag(name = "TrainingController", description = "Operations pertaining to training sessions")
public class TrainingController {
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @PostMapping
    @Operation(summary = "Add a new training session", responses = {
            @ApiResponse(responseCode = "200", description = "Training session created successfully",
                    content = @Content(schema = @Schema(implementation = Training.class))),
            @ApiResponse(responseCode = "404", description = "Trainee or Trainer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TrainingDTO> add(@RequestBody @Valid RequestTrainingDTO trainingDTO) {
        return trainingService.create(trainingDTO.toTraining(traineeService.select(trainingDTO.getTraineeUsername())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found")),
                        trainerService.select(trainingDTO.getTrainerUsername())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found"))))
                .map(training -> ResponseEntity.ok(TrainingMapper.INSTANCE.toDto(training)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to create training"));

    }

    @DeleteMapping
    @Operation(summary = "Deleted a training session", responses = {
            @ApiResponse(responseCode = "200", description = "Training session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee or Trainer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> delete(@RequestBody @Valid  RequestTrainingDTO trainingDTO) {
        if (trainingService.delete(trainingDTO))
            return ResponseEntity.ok("Successfully deleted");
        else return ResponseEntity.badRequest().build();

    }

    @GetMapping("/{profile}/list")
    @Operation(summary = "List training sessions based on profile", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(schema = @Schema(implementation = Training.class))),
            @ApiResponse(responseCode = "204", description = "No content found")
    })
    public ResponseEntity<List<TrainingDTO>> list(@Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
                                                  @PathVariable("profile") String profile, @RequestBody TrainingFilterDTO filterDTO) {

        return trainingService.list(profile.equalsIgnoreCase("trainer"), filterDTO).isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(trainingService.list(profile.equalsIgnoreCase("trainer"), filterDTO).stream()
                .map(TrainingMapper.INSTANCE::toDto)
                .toList());
    }


}

