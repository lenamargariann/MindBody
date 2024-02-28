package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.TrainingDaoImpl;
import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainerWorkloadDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;
import com.epam.xstack.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    private final JmsTemplate jmsTemplate;
    private final TrainingDaoImpl trainingDao;

    @Override
    public List<Training> list(boolean isTrainer, TrainingFilterDTO trainingFilterDTO) {
        return trainingDao.list(isTrainer, trainingFilterDTO);
    }

    @Override
    public Training create(Training training) {
        return trainingDao.create(training)
                .map(training1 -> {
            try {
                postTrainerWorkload(training1);
            } catch (ResponseStatusException e) {
                log.error(e.getMessage());
            }
            return training1;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }


    @Override
    public void changeTrainer(Training training) {
        trainingDao.changeTrainer(training)
                .ifPresentOrElse(training1 -> log.info(training1.getName() + " training updated!"),
                        () -> System.out.println("Failed to update training's trainer!"));

    }

    @Override
    public Training get(RequestTrainingDTO trainingDTO) {
        return trainingDao.getTraining(trainingDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public void delete(RequestTrainingDTO trainingDTO) {
        Training training = get(trainingDTO);
        trainingDao.delete(training);
        try {
            deleteTrainerWorkload(training);
        } catch (ResponseStatusException e) {
            log.error(e.getMessage());
        }
    }


    private void deleteTrainerWorkload(Training training) {
        try {
            jmsTemplate.convertAndSend("trainer-workload-delete-queue", TrainerWorkloadDTO.fromTraining(training));
        } catch (JmsException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    private void postTrainerWorkload(Training training) {
        try {
            jmsTemplate.convertAndSend("trainer-workload-save-queue", TrainerWorkloadDTO.fromTraining(training));
        } catch (JmsException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }


}
