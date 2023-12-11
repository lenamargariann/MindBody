package com.epam.xstack.service.impl;

import com.epam.xstack.dao.TraineeDao;
import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.User;
import com.epam.xstack.model.dto.RequestTraineeDTO;
import com.epam.xstack.model.dto.TraineeDTO;
import com.epam.xstack.service.TraineeService;
import com.epam.xstack.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final UserService userService;
    private final TraineeDao traineeDao;

    @Override
    @Transactional
    public Optional<Trainee> update(String username, TraineeDTO traineeDTO) {
        return traineeDao.findByUsername(username)
                .map(trainee -> {
                    if (traineeDTO.getFirstname() != null) trainee.getUser().setFirstname(traineeDTO.getFirstname());
                    if (traineeDTO.getLastname() != null) trainee.getUser().setLastname(traineeDTO.getLastname());
                    if (traineeDTO.getAddress() != null) trainee.setAddress(traineeDTO.getAddress());
                    if (traineeDTO.getDateOfBirth() != null) trainee.setDateOfBirth(traineeDTO.getDateOfBirth());
                    return traineeDao.update(trainee).orElse(null);
                });
    }

    @Override
    @Transactional
    public Optional<Trainee> create(RequestTraineeDTO traineeDTO) {
        User user = userService.create(traineeDTO.getFirstname(), traineeDTO.getLastname(), traineeDTO.getPassword());
        Trainee trainee = new Trainee(traineeDTO.getDateOfBirth(), traineeDTO.getAddress(), user);
        return traineeDao.create(trainee);
    }

    @Override
    public Optional<Trainee> select(String username) {
        return traineeDao.findByUsername(username);
    }

    @Override
    @Transactional
    public boolean deleteByUsername(String username) {
        return traineeDao.findByUsername(username)
                .map(t -> {
                    traineeDao.delete(t);
                    return true;
                }).orElse(false);
    }

    @Override
    @Transactional
    public Optional<List<Trainer>> updateTrainers(String username, List<String> names) {
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
                });
    }
}



