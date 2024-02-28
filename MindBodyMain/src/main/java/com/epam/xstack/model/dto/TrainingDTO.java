package com.epam.xstack.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDTO {
    @NotBlank(message = "Trainee name is required")
    private String traineeName;

    @NotBlank(message = "Trainer name is required")
    private String trainerName;

    @NotBlank(message = "Training name is required")
    private String name;

    @NotNull(message = "Training date is required")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime date;

    @NotNull(message = "Training duration is required")
    private Integer duration;

    @NotNull
    private String trainingType;

}
