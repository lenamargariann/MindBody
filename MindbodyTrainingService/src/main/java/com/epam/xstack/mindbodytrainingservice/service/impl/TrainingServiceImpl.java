package com.epam.xstack.mindbodytrainingservice.service.impl;

import com.epam.xstack.mindbodytrainingservice.dao.TrainerWorkloadDao;
import com.epam.xstack.mindbodytrainingservice.model.TrainerWorkload;
import com.epam.xstack.mindbodytrainingservice.model.TrainingDTO;
import com.epam.xstack.mindbodytrainingservice.service.TrainingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    private final TrainerWorkloadDao dao;
    private final RestTemplate restTemplate;

    @Override
    @CircuitBreaker(name = "restService", fallbackMethod = "listFallback")
    public Optional<TrainerWorkload> list(String username) {
        return dao.findFirstByUsername(username)
                .map(trainerWorkloadRequest -> trainerWorkloadRequest.toTrainerWorkload(dao.findTrainingDatesByUserName(username)));

    }

    public Optional<TrainerWorkload> listFallback(String username, Throwable t) {
        log.error(t.getMessage());
        return Optional.empty();
    }

    @Override
    @CircuitBreaker(name = "restService", fallbackMethod = "postDeleteFallback")
    public Map<HttpStatus, String> add(String header, TrainingDTO trainingDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", "Bearer=" + header);
            var request = new HttpEntity<>(trainingDTO, headers);
            var obj = restTemplate.postForObject("http://MAIN-MINDBODY-SERVICE/training", request, String.class);
            return Map.of(HttpStatus.OK, obj);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return Map.of(HttpStatus.valueOf(e.getStatusCode().value()), e.getMessage());
        }

    }

    public Map<HttpStatus, String> postDeleteFallback(String header, TrainingDTO trainingDTO, Throwable throwable) {
        log.error(throwable.getMessage());
        return Map.of(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong!");
    }

    @Override
    @CircuitBreaker(name = "restService", fallbackMethod = "postDeleteFallback")
    public Map<HttpStatus, String> delete(String header, TrainingDTO trainingDTO) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", "Bearer=" + header);
            var request = new HttpEntity<>(trainingDTO, headers);
            var response = restTemplate.exchange("http://MAIN-MINDBODY-SERVICE/training", HttpMethod.DELETE, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Map.of(HttpStatus.OK, "Training deleted");
            } else {
                return Map.of(HttpStatus.NOT_FOUND, "Delete failed");
            }
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return Map.of(HttpStatus.valueOf(e.getStatusCode().value()), e.getMessage());
        }
    }


}
