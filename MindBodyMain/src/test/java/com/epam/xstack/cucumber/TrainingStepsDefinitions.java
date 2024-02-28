package com.epam.xstack.cucumber;

import com.epam.xstack.model.dto.RequestTrainingDTO;
import com.epam.xstack.model.dto.TrainingDTO;
import com.epam.xstack.model.dto.TrainingFilterDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingStepsDefinitions {
    private final String BASE_URL = "http://localhost:9000/api/v1/training";
    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<?> successResponse;
    private ResponseEntity<String> errorResponse;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Given("the training service is running")
    public void theTrainingServiceIsRunning() {
    }

    @And("the trainee and trainer profiles exist")
    public void theTraineeAndTrainerProfilesExist() {
    }

    @When("a new training session is added with the following details:")
    public void aNewTrainingSessionIsAddedWithTheFollowingDetails(DataTable trainingDetails) {
        RequestTrainingDTO requestTrainingDTO = new RequestTrainingDTO(trainingDetails.cell(1, 0), trainingDetails.cell(1, 1), trainingDetails.cell(1, 2), LocalDateTime.parse(trainingDetails.cell(1, 3), formatter), Integer.parseInt(trainingDetails.cell(1, 4)));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestTrainingDTO> requestEntity = new HttpEntity<>(requestTrainingDTO, headers);
        try {
            successResponse = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, TrainingDTO.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("the training session should be created successfully")
    public void theTrainingSessionShouldBeCreatedSuccessfully() {
        assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
    }

    @Then("the training session creation should result in a validation error")
    public void theTrainingSessionCreationShouldResultInAValidationError() {
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());

    }

    @When("a training session is deleted with the following details:")
    public void aTrainingSessionIsDeletedWithTheFollowingDetails(DataTable trainingDetails) {
        RequestTrainingDTO requestTrainingDTO = new RequestTrainingDTO(trainingDetails.cell(1, 0), trainingDetails.cell(1, 1), trainingDetails.cell(1, 2), LocalDateTime.parse(trainingDetails.cell(1, 3), formatter), Integer.parseInt(trainingDetails.cell(1, 4)));
        HttpEntity<RequestTrainingDTO> requestEntity = new HttpEntity<>(requestTrainingDTO);
        try {
            successResponse = restTemplate.exchange(BASE_URL, HttpMethod.DELETE, requestEntity, TrainingDTO.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("the training session should be deleted successfully")
    public void theTrainingSessionShouldBeDeletedSuccessfully() {
        assertEquals(HttpStatus.NO_CONTENT, successResponse.getStatusCode());
    }

    @Then("the training session deletion should result in an error")
    public void theTrainingSessionDeletionShouldResultInAnError() {
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
    }

    @When("the list of training sessions is requested for the trainer profile with filter:")
    public void theListOfTrainingSessionsIsRequestedForTheTrainerProfileWithFilter(DataTable trainingDetails) {
        TrainingFilterDTO filterDTO = TrainingFilterDTO.builder().username(trainingDetails.cell(1, 0)).dateFrom(LocalDate.parse(trainingDetails.cell(1, 1), formatter2)).dateTo(LocalDate.parse(trainingDetails.cell(1, 2), formatter2)).build();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TrainingFilterDTO> requestEntity = new HttpEntity<>(filterDTO, headers);
        try {
            successResponse = restTemplate.exchange(BASE_URL + "/trainer/list",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<TrainingDTO>>() {
                    });
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @When("the list of training sessions is requested for the trainee profile with filter:")
    public void theListOfTrainingSessionsIsRequestedForTheTraineeProfileWithFilter(DataTable trainingDetails) {
        TrainingFilterDTO filterDTO = TrainingFilterDTO.builder().username(trainingDetails.cell(1, 0)).dateFrom(LocalDate.parse(trainingDetails.cell(1, 1), formatter2)).dateTo(LocalDate.parse(trainingDetails.cell(1, 2), formatter2)).build();
        HttpEntity<TrainingFilterDTO> requestEntity = new HttpEntity<>(filterDTO);
        try {
            successResponse = restTemplate.exchange(
                    BASE_URL + "/trainee/list",
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<List<TrainingDTO>>() {
                    });
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("the list of training sessions should be retrieved successfully")
    public void theListOfTrainingSessionsShouldBeRetrievedSuccessfully() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());

    }


}
