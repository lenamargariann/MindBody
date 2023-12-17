package com.epam.xstack.model.dto;

import com.epam.xstack.model.Training;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class TrainerWorkloadDTO {
    @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z]+$")
    private String firstname;

    @Pattern(regexp = "^[a-zA-Z]+$")
    private String lastname;

    @JsonProperty(value = "isActive")
    private Boolean isActive;

    @NotNull(message = "Training date is required")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm")
    private LocalDateTime trainingDate;

    @Min(30)
    @Max(300)
    private Integer trainingDuration;

    public static TrainerWorkloadDTO fromTraining(Training training) {
        return TrainerWorkloadDTO.builder()
                .isActive(training.getTrainer().getUser().isActive())
                .firstname(training.getTrainer().getUser().getFirstname())
                .lastname(training.getTrainer().getUser().getLastname())
                .username(training.getTrainer().getUser().getUsername())
                .trainingDuration(training.getDuration())
                .trainingDate(training.getDate()).build();
    }

}
