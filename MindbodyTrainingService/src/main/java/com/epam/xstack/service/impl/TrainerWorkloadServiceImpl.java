package com.epam.xstack.service.impl;

import com.epam.xstack.dao.TrainerWorkloadDao;
import com.epam.xstack.model.TrainerWorkload;
import com.epam.xstack.model.TrainerWorkloadMessage;
import com.epam.xstack.service.TrainerWorkloadService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private final TrainerWorkloadDao dao;



    @Override
    public TrainerWorkload create(TrainerWorkloadMessage trainingWorkload) {
        log.info("Creating workload request: {}", trainingWorkload);
        return Optional.of(dao.add(trainingWorkload.toTrainingWorkload()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    public TrainerWorkload delete(TrainerWorkloadMessage trainerWorkloadMsg) {
        log.info("Deleting workload data: {}", trainerWorkloadMsg);
        return dao.delete(trainerWorkloadMsg.toTrainingWorkload());
    }


    @Override
    @CircuitBreaker(name = "workloadFallback", fallbackMethod = "listFallback")
    public TrainerWorkload get(String username) {
        log.info("Creating training list request: {}", username);
        return dao.get(username);
    }


}
