package com.epam.xstack.mindbodytrainingservice.service.impl;

import com.epam.xstack.mindbodytrainingservice.dao.TrainerWorkloadDao;
import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkloadRequest;
import com.epam.xstack.mindbodytrainingservice.service.TrainerWorkloadService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private final TrainerWorkloadDao dao;

    @Override
    @CircuitBreaker(name = "workloadFallback", fallbackMethod = "createFallback")
    public TrainerWorkloadRequest create(TrainerWorkloadRequest request) {
        return dao.save(request);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "workloadFallback", fallbackMethod = "deleteFallback")
    public boolean delete(TrainerWorkloadRequest request) {
        long count = dao.count();
        dao.deleteByFields(request.getUsername(), request.getFirstname(), request.getLastname(), request.getIsActive(), request.getTrainingDate(), request.getTrainingDuration());
        return count > dao.count();
    }

    //FALLBACK

    public TrainerWorkloadRequest createFallback(TrainerWorkloadRequest request, Throwable t) {
        log.error("Creating TrainerWorkLoad failed, fallback method called", t);
        return new TrainerWorkloadRequest();
    }

    public boolean deleteFallback(TrainerWorkloadRequest request, Throwable t) {
        log.error("Deleting TrainerWorkLoad failed, fallback method called", t);
        return false;
    }
}
