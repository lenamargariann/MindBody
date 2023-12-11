package com.epam.xstack.mindbodytrainingservice.service;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;

public interface TrainerWorkloadService {
    TrainerWorkloadRequest create(TrainerWorkloadRequest request);

    boolean delete(TrainerWorkloadRequest request);
}
