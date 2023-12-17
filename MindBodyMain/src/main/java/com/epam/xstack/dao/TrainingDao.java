package com.epam.xstack.dao;

import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;

import java.util.List;
import java.util.Optional;

public interface TrainingDao {
    Optional<Training> create(Training training);

    List<Training> list(boolean isTrainer, TrainingFilterDTO trainingFilterDTO);

    Optional<Training> changeTrainer(Training training);

    boolean delete(Training training);

    Optional<Training> getTraining(RequestTrainingDTO trainingDTO);
}
