package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.TrainingDaoImpl;
import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;
import com.epam.xstack.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    private final TrainingDaoImpl trainingDao;

    @Override
    public List<Training> list(boolean isTrainer, TrainingFilterDTO trainingFilterDTO) {
        return trainingDao.list(isTrainer, trainingFilterDTO);
    }

    @Override
    public Optional<Training> create(Training training) {
        return trainingDao.create(training);
    }

    @Override
    public void changeTrainer(Training training) {
        trainingDao.changeTrainer(training).ifPresentOrElse(training1 -> System.out.println(training1.getName() + " training updated!"), () -> System.out.println("Failed to update training's trainer!"));

    }

    @Override
    public Optional<Training> get(RequestTrainingDTO trainingDTO) {
        return trainingDao.getTraining(trainingDTO);
    }

    @Override
    public boolean delete(RequestTrainingDTO trainingDTO) {
        return get(trainingDTO).map(trainingDao::delete).orElse(false);
    }
}
