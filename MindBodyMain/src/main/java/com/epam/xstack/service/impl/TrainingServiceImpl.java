package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.TrainingDaoImpl;
import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainerWorkloadDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;
import com.epam.xstack.service.TrainingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDaoImpl trainingDao;
    private final RestTemplate restTemplate;

    @Override
    public List<Training> list(boolean isTrainer, TrainingFilterDTO trainingFilterDTO) {
        return trainingDao.list(isTrainer, trainingFilterDTO);
    }

    @Override
    public Training create(String header, Training training) {
        return trainingDao.create(training)
                .map(training1 -> {
                    postTrainerWorkload(header, training1);
                    return training1;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }


    @Override
    public void changeTrainer(Training training) {
        trainingDao.changeTrainer(training)
                .ifPresentOrElse(training1 -> System.out.println(training1.getName() + " training updated!"),
                        () -> System.out.println("Failed to update training's trainer!"));

    }

    @Override
    public Training get(RequestTrainingDTO trainingDTO) {
        return trainingDao.getTraining(trainingDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void delete(String header, RequestTrainingDTO trainingDTO) {
        Training training = get(trainingDTO);
        trainingDao.delete(training);
        deleteTrainerWorkload(header, training);
    }

    @CircuitBreaker(name = "restService", fallbackMethod = "postDeleteFallback")
    private void postTrainerWorkload(String header, Training training) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("cookie", "Bearer=" + header);
            var request = new HttpEntity<>(TrainerWorkloadDTO.fromTraining(training), headers);
            var response = restTemplate.postForObject("http://localhost:9091/training-service-path/api/v1/workload", request, String.class);
            log.info("Trainer workload posted: " + response);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }
    }

    @CircuitBreaker(name = "restService", fallbackMethod = "postDeleteFallback")
    private void deleteTrainerWorkload(String header, Training training) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("cookie", "Bearer=" + header);
            var request = new HttpEntity<>(TrainerWorkloadDTO.fromTraining(training), headers);
            var response = restTemplate.exchange("http://localhost:9091/training-service-path/api/v1/workload", HttpMethod.DELETE, request, String.class);
            log.info("Trainer workload posted: " + response);
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }
    }

    public void postDeleteFallback(String header, Training training, Throwable throwable) {
        log.error(throwable.getMessage());
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
