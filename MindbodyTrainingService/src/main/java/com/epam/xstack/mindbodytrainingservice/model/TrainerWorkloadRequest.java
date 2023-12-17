package com.epam.xstack.mindbodytrainingservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "trainer_workload")
public class TrainerWorkloadRequest {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+[0-9]*$", message = "Username is not valid.")
    @Column(name = "username")
    private String username;

    @Pattern(regexp = "^[a-zA-Z]+$")
    @Column(name = "firstname")
    private String firstname;

    @Pattern(regexp = "^[a-zA-Z]+$")
    @Column(name = "lastname")
    private String lastname;

    @Column(name = "is_active")
    @JsonProperty(value = "isActive")
    private Boolean isActive;

    @NotNull(message = "Training date is required")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    @Column(name = "training_date")
    private LocalDateTime trainingDate;

    @Min(30)
    @Max(300)
    @Column(name = "training_duration")
    private Integer trainingDuration;

    public TrainerWorkload toTrainerWorkload(List<TrainingDateProjection> trainingDateProjection) {
        return TrainerWorkload.builder()
                .firstname(firstname)
                .lastname(lastname)
                .username(username)
                .status(isActive ? "Active" : "Inactive")
                .workload(trainingDateProjection)
                .build();
    }

}