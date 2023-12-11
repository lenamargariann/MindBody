package com.epam.xstack.controller;

import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.Trainer;
import com.epam.xstack.model.Training;
import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainingDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;
import com.epam.xstack.service.TraineeService;
import com.epam.xstack.service.TrainerService;
import com.epam.xstack.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.xstack.test_data.TestStorage.trainees;
import static com.epam.xstack.test_data.TestStorage.trainers;
import static com.epam.xstack.test_data.TestStorage.trainings;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    void list_WithContent() {
        TrainingFilterDTO filterDTO = new TrainingFilterDTO();
        List<Training> mockTrainings = List.of(new Training());

        when(trainingService.list(anyBoolean(), any(TrainingFilterDTO.class))).thenReturn(mockTrainings);

        ResponseEntity<List<TrainingDTO>> response = trainingController.list("trainer", filterDTO);

        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void list_NoContent() {
        TrainingFilterDTO filterDTO = new TrainingFilterDTO(); // Properly populate this DTO

        when(trainingService.list(anyBoolean(), any(TrainingFilterDTO.class))).thenReturn(Collections.emptyList());

        ResponseEntity<List<TrainingDTO>> response = trainingController.list("trainer", filterDTO);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void add_Success() {
        Trainee mockTrainee = trainees.get(0);
        Trainer mockTrainer = trainers.get(0);
        Training mockTraining = trainings.get(0);
        RequestTrainingDTO trainingDTO = new RequestTrainingDTO();
        when(trainingService.create(any(Training.class))).thenReturn(Optional.of(mockTraining));

        ResponseEntity<?> response = trainingController.add(trainingDTO);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void add_TraineeNotFound() {
        RequestTrainingDTO trainingDTO = new RequestTrainingDTO();
        when(traineeService.select(trainingDTO.getTraineeUsername())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            trainingController.add(trainingDTO);
        });

        assertEquals("Trainee not found", exception.getReason());
    }

    @Test
    void add_TrainerNotFound() {
        RequestTrainingDTO trainingDTO = new RequestTrainingDTO();
        Trainee mockTrainee = new Trainee();

        when(traineeService.select(trainingDTO.getTraineeUsername())).thenReturn(Optional.of(mockTrainee));
        when(trainerService.select(trainingDTO.getTrainerUsername())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            trainingController.add(trainingDTO);
        });

        assertEquals("Trainer not found", exception.getReason());
    }

    @Test
    void add_CreationError() {
        Trainee mockTrainee = trainees.get(0);
        Trainer mockTrainer = trainers.get(0);
        Training mockTraining = trainings.get(0);
        RequestTrainingDTO trainingDTO = new RequestTrainingDTO();

        when(traineeService.select(trainingDTO.getTraineeUsername())).thenReturn(Optional.of(mockTrainee));
        when(trainerService.select(trainingDTO.getTrainerUsername())).thenReturn(Optional.of(mockTrainer));
        when(trainingService.create(any(Training.class))).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            trainingController.add(trainingDTO);
        });

        assertEquals("Unable to create training", exception.getReason());
    }
}
