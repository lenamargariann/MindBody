package com.epam.xstack.mindbodytrainingservice.service;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkload;
import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;

import java.util.Optional;

public interface TrainerWorkloadService {
    TrainerWorkload list(String username);

    TrainerWorkload create(TrainerWorkloadRequest request);

    void delete(TrainerWorkloadRequest request);
}
