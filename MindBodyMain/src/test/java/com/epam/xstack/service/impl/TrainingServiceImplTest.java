package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.TrainingDaoImpl;
import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.Training;
import com.epam.xstack.model.TrainingType;
import com.epam.xstack.model.dto.TrainingFilterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Mock
    private TrainingDaoImpl trainingDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() {
        String username = "user123";
        boolean isTrainer = true;
        Training training = new Training();
        List<Training> trainingList = List.of(training);

        when(trainingDao.list( isTrainer,new TrainingFilterDTO())).thenReturn(trainingList);

        List<Training> result = trainingService.list( isTrainer,new TrainingFilterDTO());

        assertEquals(trainingList, result);
        verify(trainingDao, times(1)).list(isTrainer,new TrainingFilterDTO());
    }

    @Test
    public void testCreate_TrainingSaved() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        String trainingTypeName = "Cardio";
        TrainingType trainingType = new TrainingType();
        LocalDateTime trainingTime = LocalDateTime.now();
        int trainingDuration = 60;

        when(trainingDao.create(any(Training.class))).thenReturn(Optional.of(new Training()));

        trainingService.create(new Training());

        verify(trainingDao, times(1)).create(any(Training.class));
    }

    @Test
    public void testCreate_TrainingNotSaved() {
        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        String trainingTypeName = "Cardio";
        TrainingType trainingType = new TrainingType();
        LocalDateTime trainingTime = LocalDateTime.now();
        int trainingDuration = 60;

        when(trainingDao.create(any(Training.class))).thenReturn(Optional.empty());

        trainingService.create(new Training());

        verify(trainingDao, times(1)).create(any(Training.class));
    }

    @Test
    public void testChangeTrainer_TrainingUpdated() {
        Training training = new Training();
        training.setName("Strength");

        when(trainingDao.changeTrainer(training)).thenReturn(Optional.of(training));

        trainingService.changeTrainer(training);

        verify(trainingDao, times(1)).changeTrainer(training);
    }

    @Test
    public void testChangeTrainer_FailedToUpdate() {
        Training training = new Training();
        training.setName("Strength");

        when(trainingDao.changeTrainer(training)).thenReturn(Optional.empty());

        trainingService.changeTrainer(training);

        verify(trainingDao, times(1)).changeTrainer(training);
    }
}
