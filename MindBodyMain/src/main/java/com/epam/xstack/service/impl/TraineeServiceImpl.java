package com.epam.xstack.service.impl;

import com.epam.xstack.dao.TraineeDao;
import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.dto.RequestTraineeDTO;
import com.epam.xstack.model.dto.TraineeDTO;
import com.epam.xstack.service.TraineeService;
import com.epam.xstack.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final UserService userService;
    private final TraineeDao traineeDao;

    @Override
    @Transactional
    public Trainee update(String username, TraineeDTO traineeDTO) {
        return traineeDao.findByUsername(username)
                .map(trainee -> {
                    if (traineeDTO.getFirstname() != null) trainee.getUser().setFirstname(traineeDTO.getFirstname());
                    if (traineeDTO.getLastname() != null) trainee.getUser().setLastname(traineeDTO.getLastname());
                    if (traineeDTO.getAddress() != null) trainee.setAddress(traineeDTO.getAddress());
                    if (traineeDTO.getDateOfBirth() != null) trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
                    return traineeDao.update(trainee).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found."));
    }

    @Override
    @Transactional
    public Trainee create(RequestTraineeDTO traineeDTO) {
        return
                traineeDao.create(new Trainee(
                                traineeDTO.getDateOfBirth(),
                                traineeDTO.getAddress(),
                                userService.create(traineeDTO.getFirstname(), traineeDTO.getLastname(), traineeDTO.getPassword())))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

    }

    @Override
    public Trainee select(String username) {
        return traineeDao.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainee not found."));
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        traineeDao.findByUsername(username)
                .map(traineeDao::delete)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainee not found."));
    }

    @Override
    @Transactional

    public List<Trainer> updateTrainers(String username, List<String> names) {
        return traineeDao.findByUsername(username)
                .map(trainee -> {
                    List<Trainer> trainers = names.stream()
                            .map(userService::getProfile)
                            .filter(Trainer.class::isInstance)
                            .map(Trainer.class::cast)
                            .collect(Collectors.toList());
                    trainee.setTrainers(trainers);
                    traineeDao.update(trainee);
                    return trainers;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Trainee not found."));
    }
}



