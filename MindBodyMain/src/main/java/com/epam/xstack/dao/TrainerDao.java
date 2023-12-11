package com.epam.xstack.dao;

import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.TrainingType;

import java.util.List;

public interface TrainerDao extends AbstractDao<Trainer> {
    List<Trainer> listNotAssigned();

    List<Trainer> listBySpecialization(TrainingType trainingType);

}


