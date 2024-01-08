package com.epam.xstack.mindbodytrainingservice.service;

import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@Slf4j
public class TrainerWorkloadListener {
    private final Validator validator;
    private final TrainerWorkloadService trainerWorkloadService;


    public TrainerWorkloadListener(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @JmsListener(destination = "trainer-workload-save-queue")
    private void saveWorkloadMessage(TrainerWorkloadRequest trainerWorkload) {
        validate(trainerWorkload);
        trainerWorkloadService.create(trainerWorkload);
    }


    @JmsListener(destination = "trainer-workload-delete-queue")
    private void deleteWorkloadMessage(TrainerWorkloadRequest trainerWorkload) {
        validate(trainerWorkload);
        trainerWorkloadService.delete(trainerWorkload);
    }

    public void validate(TrainerWorkloadRequest object) {
        Set<ConstraintViolation<TrainerWorkloadRequest>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<TrainerWorkloadRequest> violation : violations) {
                sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
            }
            throw new RuntimeException("Validation failed: " + sb);
        }
    }
}
