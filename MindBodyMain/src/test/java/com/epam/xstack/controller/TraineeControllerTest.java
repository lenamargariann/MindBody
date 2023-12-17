package com.epam.xstack.controller;

import com.epam.xstack.service.TraineeService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TraineeControllerTest {
    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void register_Success() {
//        RequestTraineeDTO traineeDTO = new RequestTraineeDTO();
//        when(traineeService.create(traineeDTO)).thenReturn(Optional.of(TestStorage.trainees.get(0)));
//
//        ResponseEntity<?> response = traineeController.register(traineeDTO);
//
//        assertEquals(CREATED.value(), response.getStatusCode().value());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void register_Failure() {
//        RequestTraineeDTO traineeDTO = new RequestTraineeDTO();
//        when(traineeService.create(traineeDTO)).thenReturn(Optional.empty());
//
//        ResponseEntity<?> response = traineeController.register(traineeDTO);
//        assertEquals(BAD_REQUEST.value(), response.getStatusCode().value());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void getProfile_Success() {
//        String username = "testuser";
//        Trainee mockTrainee = new Trainee();
//        when(traineeService.select(username)).thenReturn(Optional.of(mockTrainee));
//
//        ResponseEntity<?> response = traineeController.getProfile(username);
//
//        assertEquals(OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void getProfile_NotFound() {
//        String username = "nonexistent";
//        when(traineeService.select(username)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResponseStatusException.class, () -> {
//            traineeController.getProfile(username);
//        });
//
//        assertEquals("404 NOT_FOUND \"Trainee not found\"", exception.getMessage());
//    }
//
//    @Test
//    void updateProfile_Success() {
//        String username = "testuser";
//        TraineeDTO traineeDTO = new TraineeDTO();
//        Trainee updatedTrainee = new Trainee();
//        when(traineeService.update(username, traineeDTO)).thenReturn(Optional.of(updatedTrainee));
//
//        TraineeDTO response = traineeController.updateProfile(username, traineeDTO);
//
//    }
//
//    @Test
//    void updateProfile_NotFound() {
//        String username = "nonexistent";
//        TraineeDTO traineeDTO = new TraineeDTO();
//        when(traineeService.update(username, traineeDTO)).thenReturn(Optional.empty());
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            traineeController.updateProfile(username, traineeDTO);
//        });
//
//        assertEquals("Trainee not found", (exception).getReason());
//    }
//
//    @Test
//    void deleteProfile_Success() {
//        String username = "testuser";
//        when(traineeService.deleteByUsername(username)).thenReturn(true);
//
//        ResponseEntity<?> response = traineeController.deleteProfile(username);
//
//        assertEquals(OK, response.getStatusCode());
//    }
//
//    @Test
//    void deleteProfile_NotFound() {
//        String username = "nonexistent";
//        when(traineeService.deleteByUsername(username)).thenReturn(false);
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            traineeController.deleteProfile(username);
//        });
//
//        assertEquals("Trainee not found", exception.getReason());
//    }
//
//    @Test
//    void updateTrainers_Success() {
//        String username = "testuser";
//        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");
//        List<Trainer> updatedTrainers = Arrays.asList(new Trainer(), new Trainer());
//        when(traineeService.updateTrainers(username, trainerUsernames)).thenReturn(Optional.of(updatedTrainers));
//
//        ResponseEntity<?> response = traineeController.updateTrainers(username, trainerUsernames);
//
//        assertEquals(OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void updateTrainers_NotFound() {
//        String username = "nonexistent";
//        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");
//        when(traineeService.updateTrainers(username, trainerUsernames)).thenReturn(Optional.empty());
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            traineeController.updateTrainers(username, trainerUsernames);
//        });
//
//        assertEquals("Trainee not found", exception.getReason());
//    }
}