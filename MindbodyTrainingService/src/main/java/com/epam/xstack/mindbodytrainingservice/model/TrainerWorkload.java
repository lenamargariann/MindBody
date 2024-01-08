package com.epam.xstack.mindbodytrainingservice.model;

import lombok.Builder;
import lombok.Data;

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
