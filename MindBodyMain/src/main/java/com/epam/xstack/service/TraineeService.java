package com.epam.xstack.service;

import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.RequestTraineeDTO;
import com.epam.xstack.model.dto.TraineeDTO;

import java.util.List;
import java.util.Optional;

public interface TraineeService extends AbstractService<Trainee> {
    Optional<Trainee> update(String username, TraineeDTO traineeDTO);

    Optional<Trainee> create(RequestTraineeDTO traineeDTO);

    boolean deleteByUsername(String username);

    Optional<List<Trainer>> updateTrainers(String username, List<String> names);
}
