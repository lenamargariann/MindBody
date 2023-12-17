package com.epam.xstack.mindbodytrainingservice.service.impl;

import com.epam.xstack.mindbodytrainingservice.dao.TrainerWorkloadDao;
import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkload;
import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;
import com.epam.xstack.mindbodytrainingservice.service.TrainerWorkloadService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private final TrainerWorkloadDao dao;

    @Override
    @CircuitBreaker(name = "workloadFallback", fallbackMethod = "createFallback")
    public TrainerWorkload create(TrainerWorkloadRequest request) {
        log.info("Creating workload request: {}", request);
        return Optional.of(dao.save(request).toTrainerWorkload(new ArrayList<>()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    @CircuitBreaker(name = "workloadFallback", fallbackMethod = "listFallback")
    public TrainerWorkload list(String username) {
        log.info("Creating training list request: {}", username);
        return dao.findFirstByUsername(username)
                .map(trainerWorkloadRequest -> trainerWorkloadRequest.toTrainerWorkload(dao.findTrainingDatesByUserName(username)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @Override
    @Transactional
    @CircuitBreaker(name = "workloadFallback", fallbackMethod = "deleteFallback")
    public void delete(TrainerWorkloadRequest request) {
        long count = dao.count();
        log.info("Deleting workload: {}", request.toString());
        dao.deleteByFields(request.getUsername(), request.getFirstname(), request.getLastname(), request.getIsActive(), request.getTrainingDate(), request.getTrainingDuration());
        if (count < dao.count()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    //FALLBACK
    public TrainerWorkload createFallback(TrainerWorkloadRequest request, Throwable t) {
        log.error("Creating TrainerWorkLoad failed, fallback method called", t);
        return TrainerWorkload.builder().build();
    }

    public void deleteFallback(TrainerWorkloadRequest request, Throwable t) {
        log.error("Deleting TrainerWorkLoad failed, fallback method called", t);
    }

    public TrainerWorkload listFallback(String username, Throwable t) {
        log.error(t.getMessage(), t);
        return TrainerWorkload.builder().build();
    }
}
