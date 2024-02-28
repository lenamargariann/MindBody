package com.epam.xstack.service.impl;

import com.epam.xstack.dao.TrainerWorkloadDao;
import com.epam.xstack.model.TrainerWorkload;
import com.epam.xstack.model.TrainerWorkloadMessage;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TrainerWorkloadServiceImplTest {

    @Mock
    private TrainerWorkloadDao dao;

    @InjectMocks
    private TrainerWorkloadServiceImpl service;
    TrainerWorkloadMessage request = TrainerWorkloadMessage.builder()
            .firstname("John")
            .lastname("Doe")
            .username("john.doe")
            .isActive(true)
            .trainingDate(LocalDateTime.now())
            .trainingDuration(100)
            .build();
    TrainerWorkloadMessage existing = TrainerWorkloadMessage.builder()
            .firstname("John")
            .lastname("Doe")
            .username("john.doe")
            .isActive(true)
            .trainingDate(LocalDateTime.now())
            .trainingDuration(200)
            .build();


    public TrainerWorkloadServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        TrainerWorkload trainingWorkload = request.toTrainingWorkload();

        when(dao.add(trainingWorkload)).thenReturn(trainingWorkload);

        TrainerWorkload result = service.create(request);

        assertNotNull(result);
        assertEquals(trainingWorkload, result);
    }


    @Test
    void testDelete() {
        TrainerWorkload trainingWorkload = request.toTrainingWorkload();

        when(dao.delete(trainingWorkload)).thenReturn(trainingWorkload);

        TrainerWorkload result = service.delete(request);

        assertNotNull(result);
        assertEquals(trainingWorkload, result);
    }


    @Test
    void testGet() {
        TrainerWorkload expectedWorkload = existing.toTrainingWorkload();

        when(dao.get(expectedWorkload.getTrainerUsername())).thenReturn(expectedWorkload);

        TrainerWorkload result = service.get(expectedWorkload.getTrainerUsername());
        assertNotNull(result);
        assertEquals(expectedWorkload, result);
    }


}