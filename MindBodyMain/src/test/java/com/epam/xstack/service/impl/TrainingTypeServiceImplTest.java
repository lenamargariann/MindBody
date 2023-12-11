package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.TrainingTypeDaoImpl;
import com.epam.xstack.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrainingTypeServiceImplTest {

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    @Mock
    private TrainingTypeDaoImpl trainingTypeDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByName_Found() {
        String trainingTypeName = "Cardio";
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(trainingTypeName);

        when(trainingTypeDao.findByName(trainingTypeName)).thenReturn(Optional.of(trainingType));

        Optional<TrainingType> result = trainingTypeService.findByName(trainingTypeName);

        assertTrue(result.isPresent());
        assertEquals(trainingTypeName, result.get().getTrainingTypeName());
        verify(trainingTypeDao, times(1)).findByName(trainingTypeName);
    }

    @Test
    public void testFindByName_NotFound() {
        String trainingTypeName = "Strength";

        when(trainingTypeDao.findByName(trainingTypeName)).thenReturn(Optional.empty());

        Optional<TrainingType> result = trainingTypeService.findByName(trainingTypeName);

        assertFalse(result.isPresent());
        verify(trainingTypeDao, times(1)).findByName(trainingTypeName);
    }
}
