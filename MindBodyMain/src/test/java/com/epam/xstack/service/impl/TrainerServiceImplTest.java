package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.TrainerDaoImpl;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.TrainingType;
import com.epam.xstack.model.User;
import com.epam.xstack.model.dto.RequestTrainerDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Mock
    private TrainerDaoImpl trainerDao;

    @Mock
    private UserServiceImpl userService;

//    @Test
//    public void testUpdate() {
//        Trainer trainer = new Trainer();
//
//        when(trainerDao.update(trainer)).thenReturn(Optional.of(trainer));
//
//        trainerService.update(trainer);
//
//        verify(trainerDao, times(1)).update(trainer);
//    }

    @Test
    public void testCreate() {
        String firstname = "John";
        String lastname = "Doe";
        TrainingType trainingType = new TrainingType();

        when(userService.create(firstname, lastname, "aerhstrdtrA")).thenReturn(new User());
        when(trainerDao.create(any())).thenReturn(Optional.of(new Trainer()));

        trainerService.create(new RequestTrainerDTO());

        verify(trainerDao, times(1)).create(any());
        verify(userService, times(1)).create(firstname, lastname, "ycghuyvhA");
    }

    @Test
    public void testListNotAssigned() {
        List<Trainer> expectedList = new ArrayList<>();
        when(trainerDao.listNotAssigned()).thenReturn(expectedList);

        List<Trainer> resultList = trainerService.listNotAssigned();

        assertEquals(expectedList, resultList);
        verify(trainerDao, times(1)).listNotAssigned();
    }

//    @Test
//    public void testListBySpecialization() {
//        TrainingType trainingType = new TrainingType();
//        List<Trainer> expectedList = new ArrayList<>();
//        when(trainerDao.listBySpecialization(trainingType)).thenReturn(expectedList);
//
////        List<Trainer> resultList = trainerService.listBySpecialization(trainingType);
//
//        assertEquals(expectedList, resultList);
//        verify(trainerDao, times(1)).listBySpecialization(trainingType);
//    }
}
