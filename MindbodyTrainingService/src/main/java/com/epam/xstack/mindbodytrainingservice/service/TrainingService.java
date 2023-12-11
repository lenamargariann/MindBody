package com.epam.xstack.mindbodytrainingservice.service;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkload;
import com.epam.xstack.mindbodytrainingservice.model.TrainingDTO;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;


public interface TrainingService {
    Optional<TrainerWorkload> list(String username);

    Map<HttpStatus, String> add(String header, TrainingDTO trainingDTO);

    Map<HttpStatus, String> delete(String header, TrainingDTO trainingDTO);
}
