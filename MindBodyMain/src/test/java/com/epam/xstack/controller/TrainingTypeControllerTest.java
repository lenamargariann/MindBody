package com.epam.xstack.controller;

import com.epam.xstack.service.TrainingTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNull;

public class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void list_ReturnsTrainingTypesSuccessfully() throws JsonProcessingException {
//        List<TrainingType> trainingTypes = TestStorage.trainingTypes;
//        when(trainingTypeService.list()).thenReturn(trainingTypes);
//
//        var response = trainingTypeController.list();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(objectMapper.writeValueAsString(trainingTypes), response.getBody());
//
//        verify(trainingTypeService).list();
//     }
//
//    @Test
//    void list_ThrowsJsonProcessingException() throws JsonProcessingException {
//        when(trainingTypeService.list()).thenReturn(List.of(new TrainingType()));
//        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Test exception") {
//        });
//
//        var response = trainingTypeController.list();
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertNull(response.getBody());
//
//        verify(trainingTypeService).list();
//        verify(objectMapper).writeValueAsString(any());
//    }
}
