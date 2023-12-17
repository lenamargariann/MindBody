package com.epam.xstack.mindbodytrainingservice.model;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TrainerWorkload {
    private String username;
    private String firstname;
    private String lastname;
    private String status;
    private List<TrainingDateProjection> workload;
}
