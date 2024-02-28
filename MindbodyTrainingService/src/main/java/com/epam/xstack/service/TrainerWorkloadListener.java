package com.epam.xstack.service;

import com.epam.xstack.model.TrainerWorkloadMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class TrainerWorkloadListener {
    private final Validator validator;
    private final TrainerWorkloadService trainerWorkloadService;


    public TrainerWorkloadListener(TrainerWorkloadService trainerWorkloadService) {
        this.trainerWorkloadService = trainerWorkloadService;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @JmsListener(destination = "trainer-workload-save-queue")
    public void saveWorkloadMessage(TrainerWorkloadMessage trainerWorkload) {
        validate(trainerWorkload);
        trainerWorkloadService.create(trainerWorkload);
    }


    @JmsListener(destination = "trainer-workload-delete-queue")
    public void deleteWorkloadMessage(TrainerWorkloadMessage trainerWorkload) {
        validate(trainerWorkload);
        trainerWorkloadService.delete(trainerWorkload);
    }

    public void validate(TrainerWorkloadMessage object) {
        Set<ConstraintViolation<TrainerWorkloadMessage>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<TrainerWorkloadMessage> violation : violations) {
                sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
            }
            throw new RuntimeException("Validation failed: " + sb);
        }
    }
}
