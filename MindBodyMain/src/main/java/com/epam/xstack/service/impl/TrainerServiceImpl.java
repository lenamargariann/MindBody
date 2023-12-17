package com.epam.xstack.service.impl;

import com.epam.xstack.dao.TrainerDao;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.RequestTrainerDTO;
import com.epam.xstack.model.dto.TrainerDTO;
import com.epam.xstack.service.TrainerService;
import com.epam.xstack.service.TrainingTypeService;
import com.epam.xstack.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerDao trainerDao;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;


    @Override
    @Transactional
    public Trainer create(RequestTrainerDTO trainerDTO) {
        return trainingTypeService.findByName(trainerDTO.getSpecialization())
                .map(trainingType ->
                        trainerDao.create(new Trainer(trainingType, userService.create(trainerDTO.getFirstname(), trainerDTO.getLastname(), trainerDTO.getPassword())))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found.")))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't create trainer"));


    }

    @Override
    @Transactional
    public Trainer update(String username, TrainerDTO trainerDTO) {
        return trainerDao.findByUsername(username)
                .map(trainer -> trainer
                        .updateFromDTO(trainerDTO, trainingTypeService.findByName(trainerDTO.getSpecialization())
                                .orElse(null)))
                .flatMap(trainerDao::update)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found."));
    }

    @Override
    public Trainer select(String username) {
        log.debug("Selecting trainer with username: {}", username);
        return trainerDao.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found."));
    }

    @Override
    public List<Trainer> listNotAssigned() {
        return trainerDao.listNotAssigned();
    }

}



