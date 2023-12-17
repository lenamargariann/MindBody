package com.epam.xstack.service;

import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.RequestTrainerDTO;
import com.epam.xstack.model.dto.TrainerDTO;

import java.util.List;

public interface TrainerService extends AbstractService<Trainer>{
     Trainer create(RequestTrainerDTO trainerDTO);
     Trainer update(String username, TrainerDTO trainerDTO);
     List<Trainer> listNotAssigned();
 }
