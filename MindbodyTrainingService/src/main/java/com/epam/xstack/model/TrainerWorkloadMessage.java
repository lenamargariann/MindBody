package com.epam.xstack.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerWorkloadMessage {
    @JsonIgnore
    private Long id;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime trainingDate;

    @Min(30)
    @Max(300)
    private Integer trainingDuration;

    public TrainerWorkload toTrainingWorkload() {
        return TrainerWorkload.builder()
                .trainerFirstname(firstname)
                .trainerLastname(lastname)
                .trainerUsername(username)
                .isActive(isActive)
                .years(Map.of(String.valueOf(trainingDate.getYear()), Map.of(trainingDate.getMonth().name(), trainingDuration)))
                .build();
    }

}