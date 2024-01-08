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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/training")
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
    @ResponseStatus(value = HttpStatus.CREATED)
    public TrainingDTO add(@RequestBody @Valid RequestTrainingDTO trainingDTO) {
        return TrainingMapper.INSTANCE.toDto(
                trainingService.create(
                        trainingDTO.toTraining(traineeService.select(trainingDTO.getTraineeUsername()),
                                trainerService.select(trainingDTO.getTrainerUsername()))
                ));
    }

    @DeleteMapping
    @Operation(summary = "Deleted a training session", responses = {
            @ApiResponse(responseCode = "200", description = "Training session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee or Trainer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Training session deleted.")
    public void delete(@RequestBody @Valid RequestTrainingDTO trainingDTO) {
        trainingService.delete(trainingDTO);
    }

    @GetMapping("/{profile}/list")
    @Operation(summary = "List training sessions based on profile", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(schema = @Schema(implementation = Training.class))),
            @ApiResponse(responseCode = "204", description = "No content found")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<TrainingDTO> list(@PathVariable("profile") String profile, @RequestBody @Valid TrainingFilterDTO filterDTO) {
        return trainingService.list(profile.equalsIgnoreCase("trainer"), filterDTO).stream()
                .map(TrainingMapper.INSTANCE::toDto)
                .toList();
    }


}

