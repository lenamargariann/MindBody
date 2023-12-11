package com.epam.xstack.service.impl;

import com.epam.xstack.dao.TrainerDao;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.TrainingType;
import com.epam.xstack.model.User;
import com.epam.xstack.model.dto.RequestTrainerDTO;
import com.epam.xstack.model.dto.TrainerDTO;
import com.epam.xstack.service.TrainerService;
import com.epam.xstack.service.TrainingTypeService;
import com.epam.xstack.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDao trainerDao;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;


    @Override
    @Transactional
    public Optional<Trainer> create(RequestTrainerDTO trainerDTO) {
        User user = userService.create(trainerDTO.getFirstname(), trainerDTO.getLastname(), trainerDTO.getPassword());
        TrainingType trainingType = trainingTypeService.findByName(trainerDTO.getSpecialization())
                .orElseThrow(() -> new EntityNotFoundException("TrainingType not found"));
        Trainer trainer = new Trainer(trainingType, user);
        log.info("Creating trainer for user: {}", user.getUsername());
        return trainerDao.create(trainer);
    }

    @Override
    @Transactional
    public Optional<Trainer> update(String username, TrainerDTO trainerDTO) {
        return trainerDao.findByUsername(username)
                .map(trainer -> {
                    TrainingType trainingType = trainingTypeService.findByName(trainerDTO.getSpecialization())
                            .orElseThrow(() -> new EntityNotFoundException("TrainingType not found for update"));
                    return trainer.updateFromDTO(trainerDTO, Optional.of(trainingType));
                })
                .flatMap(trainerDao::update);
    }

    @Override
    public Optional<Trainer> select(String username) {
        log.debug("Selecting trainer with username: {}", username);
        return trainerDao.findByUsername(username);
    }

    @Override
    public List<Trainer> listNotAssigned() {
        return trainerDao.listNotAssigned();
    }

}



