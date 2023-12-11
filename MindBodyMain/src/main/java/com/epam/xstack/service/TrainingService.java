package com.epam.xstack.service;

import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;

import java.util.List;
import java.util.Optional;

public interface TrainingService {
    List<Training> list(boolean isTrainer, TrainingFilterDTO trainingFilterDTO);

    Optional<Training> create(Training training);

    void changeTrainer(Training training);

    Optional<Training> get(RequestTrainingDTO trainingDTO);

    boolean delete(RequestTrainingDTO trainingDTO);
}
