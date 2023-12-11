package com.epam.xstack.service;

import com.epam.xstack.model.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeService {
    Optional<TrainingType> findByName(String trainingTypeName);
    List<TrainingType> list();
}
