package com.epam.xstack.controller;

import com.epam.xstack.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
 public class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void register_Success() {
//        RequestTrainerDTO trainerDTO = new RequestTrainerDTO();
//        when(trainerService.create(any(RequestTrainerDTO.class))).thenReturn(Optional.of(trainers.get(0)));
//
//        ResponseEntity<?> response = trainerController.register(trainerDTO);
//
//        assertEquals(CREATED.value(), response.getStatusCode().value());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void register_Failure() {
//        RequestTrainerDTO trainerDTO = new RequestTrainerDTO();
//        when(trainerService.create(trainerDTO)).thenReturn(Optional.empty());
//
//        ResponseEntity<?> response = trainerController.register(trainerDTO);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Couldn't create trainer", response.getBody());
//    }
//
//    @Test
//    void getProfile_Success() {
//        String username = "testTrainer";
//        Trainer mockTrainer = new Trainer();
//        when(trainerService.select(username)).thenReturn(Optional.of(mockTrainer));
//
//        ResponseEntity<?> response = trainerController.getProfile(username);
//
//        assertEquals(OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody() instanceof Trainer);
//    }
//
//    @Test
//    void getProfile_NotFound() {
//        String username = "nonexistentTrainer";
//        when(trainerService.select(username)).thenReturn(Optional.empty());
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            trainerController.getProfile(username);
//        });
//
//        assertEquals("Trainer not found", exception.getReason());
//    }
//
//    @Test
//    void updateTrainerProfile_Success() {
//        String username = "existingTrainer";
//        TrainerDTO updateDTO = new TrainerDTO();
//        Trainer updatedTrainer = new Trainer();
//        when(trainerService.update(username, updateDTO)).thenReturn(Optional.of(updatedTrainer));
//
//        ResponseEntity<?> response = trainerController.updateTrainerProfile(username, updateDTO);
//
//        assertEquals(OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertTrue(response.getBody() instanceof Trainer);
//    }
//
//    @Test
//    void updateTrainerProfile_NotFound() {
//        String username = "nonexistentTrainer";
//        TrainerDTO updateDTO = new TrainerDTO();
//        when(trainerService.update(username, updateDTO)).thenReturn(Optional.empty());
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//            trainerController.updateTrainerProfile(username, updateDTO);
//        });
//
//        assertEquals("Trainer not found", exception.getReason());
//    }
//
//    @Test
//    void listNotAssigned_HasTrainers() {
//        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer()); // Set necessary properties
//        when(trainerService.listNotAssigned()).thenReturn(trainers);
//
//        ResponseEntity<?> response = trainerController.listNotAssigned();
//
//        assertEquals(OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertFalse(((List<?>) response.getBody()).isEmpty());
//    }
//
//    @Test
//    void listNotAssigned_EmptyList() {
//        when(trainerService.listNotAssigned()).thenReturn(Collections.emptyList());
//
//        ResponseEntity<?> response = trainerController.listNotAssigned();
//
//        assertEquals(NO_CONTENT, response.getStatusCode());
//        assertNull(response.getBody());
//    }
}
