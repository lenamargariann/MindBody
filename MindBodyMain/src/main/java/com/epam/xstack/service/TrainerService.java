package com.epam.xstack.service;

import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.RequestTrainerDTO;
import com.epam.xstack.model.dto.TrainerDTO;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Optional<Trainer> create(RequestTrainerDTO trainerDTO);
    Optional<Trainer> update(String username, TrainerDTO trainerDTO);
    Optional<Trainer> select(String username);
    List<Trainer> listNotAssigned();
 }
