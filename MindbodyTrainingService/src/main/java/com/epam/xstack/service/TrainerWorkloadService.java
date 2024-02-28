package com.epam.xstack.service;

import com.epam.xstack.model.TrainerWorkload;
import com.epam.xstack.model.TrainerWorkloadMessage;

public interface TrainerWorkloadService {
    TrainerWorkload get(String username);
    TrainerWorkload create(TrainerWorkloadMessage request);
    TrainerWorkload delete(TrainerWorkloadMessage trainerWorkload);
 }
