package com.epam.xstack.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class TraineeControllerSpringTest {

//    @Autowired
//    private MockMvc mvc;
//    @Autowired
//    private TraineeService traineeService;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    private static Stream<Arguments> provideTraineesForRegistration() {
//        return Stream.of(
//                Arguments.of(new RequestTraineeDTO("John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "Password12")),
//                Arguments.of(new RequestTraineeDTO("Jan", "Smith", LocalDate.of(1992, 2, 2), "456 Maple Ave", "Password12")),
//                Arguments.of(new RequestTraineeDTO("Alice", "Johnson", LocalDate.of(1985, 3, 3), "789 Oak Blvd", "Password12"))
//        );
//    }
//
//    //1
//    @ParameterizedTest
//    @MethodSource("provideTraineesForRegistration")
//    public void testRegisterNewTrainee(RequestTraineeDTO traineeDTO) throws Exception {
//        String traineeJson = objectMapper.writeValueAsString(traineeDTO);
//        mvc.perform(MockMvcRequestBuilders.post("/trainee")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(traineeJson))
//                .andExpect(status().isCreated());
//    }
//
//    //2
//    @ParameterizedTest
//    @MethodSource("provideTraineesForRegistration")
//    public void invalidTraineeValue(RequestTraineeDTO traineeDTO) throws Exception {
//        traineeDTO.setPassword("aa");
//        String traineeJson = objectMapper.writeValueAsString(traineeDTO);
//        mvc.perform(MockMvcRequestBuilders
//                        .post("/trainee")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(traineeJson))
//                .andExpect(status().is4xxClientError());
//    }
//
//    @ParameterizedTest
//    @WithMockUser
//    @MethodSource("provideTraineesForRegistration")
//    public void testGetProfile_Success(RequestTraineeDTO traineeDTO) throws Exception {
//        String username = (traineeDTO.getFirstname() + "." + traineeDTO.getLastname()).toLowerCase();
//        traineeService.create(traineeDTO);
//        mvc.perform(MockMvcRequestBuilders.get("/trainee/{username}", username))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value(username));
//    }
//
//    @Test
//    @WithMockUser
//    public void testGetProfile_NotFound() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.get("/trainee/{username}", "non.existent"))
//                .andExpect(status().isNotFound());
//    }
//
//    @WithMockUser
//    @ParameterizedTest
//    @MethodSource("provideTraineesForRegistration")
//    public void updateProfile_Success(RequestTraineeDTO traineeDTO) throws Exception {
//        Trainee trainee = traineeService.create(traineeDTO).get();
//        trainee.getUser().setFirstname("Volodia");
//        mvc.perform(MockMvcRequestBuilders
//                        .put("/trainee/{username}", trainee.getUser().getUsername())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(TraineeMapper.INSTANCE.toDto(trainee))))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    public void updateProfile_NotFound() throws Exception {
//        mvc.perform(MockMvcRequestBuilders
//                        .put("/trainee/nonexistent.user")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(TraineeMapper.INSTANCE.toDto(TestStorage.trainees.get(0)))))
//                .andExpect(status().isNotFound());
//    }
//
//    @ParameterizedTest
//    @WithMockUser
//    @MethodSource("provideTraineesForRegistration")
//    public void deleteProfile_Success(RequestTraineeDTO traineeDTO) throws Exception {
//        Trainee trainee = traineeService.create(traineeDTO).get();
//        mvc.perform(MockMvcRequestBuilders
//                        .delete("/trainee/{username}", trainee.getUser().getUsername())
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithMockUser
//    public void deleteProfile_NotFound() throws Exception {
//        mvc.perform(MockMvcRequestBuilders
//                        .delete("/trainee/nonexistent.user")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
//
//    @ParameterizedTest
//    @WithMockUser
//    @MethodSource("provideTraineesForRegistration")
//    public void updateTrainers_Success(RequestTraineeDTO traineeDTO) throws Exception {
//        List<String> trainerUsernames = TestStorage.trainers.stream().map(trainer -> trainer.getUser().getUsername()).toList();
//        Trainee trainee = traineeService.create(traineeDTO).get();
//        mvc.perform(MockMvcRequestBuilders
//                        .put("/trainee/{username}/trainers", trainee.getUser().getUsername())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(trainerUsernames)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    @Test
//    @WithMockUser
//    public void updateTrainers_TraineeNotFound() throws Exception {
//        List<String> trainerUsernames = TestStorage.trainers.stream().map(trainer -> trainer.getUser().getUsername()).toList();
//        mvc.perform(MockMvcRequestBuilders
//                        .put("/trainee/nonexistent.username/trainers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(trainerUsernames)))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    @WithMockUser
//    public void updateTrainers_InvalidTrainerUsernamesFormat() throws Exception {
//        List<String> invalidTrainerUsernames = List.of("invalid-trainer1", "invalid-trainer2");
//        mvc.perform(MockMvcRequestBuilders
//                        .put("/trainee/valid.username/trainers")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidTrainerUsernames)))
//                .andExpect(status().isBadRequest());
//    }
}
