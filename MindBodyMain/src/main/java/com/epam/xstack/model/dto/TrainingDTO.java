package com.epam.xstack.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy hh:mm")
    private LocalDateTime date;

    @NotNull(message = "Training duration is required")
    private Integer duration;

    @NotNull
    private String trainingType;

}
