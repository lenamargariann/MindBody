package com.epam.xstack.service;

import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;

import java.util.List;

public interface TrainingService {
    List<Training> list(boolean isTrainer, TrainingFilterDTO trainingFilterDTO);

    Training create(String header, Training training);

    void changeTrainer(Training training);

    Training get(RequestTrainingDTO trainingDTO);

    void delete(String header, RequestTrainingDTO trainingDTO);
}
