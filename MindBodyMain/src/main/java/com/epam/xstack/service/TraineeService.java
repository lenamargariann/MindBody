package com.epam.xstack.service;

import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.RequestTraineeDTO;
import com.epam.xstack.model.dto.TraineeDTO;

import java.util.List;

public interface TraineeService extends AbstractService<Trainee> {
    Trainee update(String username, TraineeDTO traineeDTO);

   Trainee create(RequestTraineeDTO traineeDTO);

    void deleteByUsername(String username);

    List<Trainer> updateTrainers(String username, List<String> names);
}
