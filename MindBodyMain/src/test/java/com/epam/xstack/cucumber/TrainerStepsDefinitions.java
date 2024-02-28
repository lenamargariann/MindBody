package com.epam.xstack.cucumber;

import com.epam.xstack.model.dto.RequestTrainerDTO;
import com.epam.xstack.model.dto.TrainerDTO;
import com.epam.xstack.model.dto.UserDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerStepsDefinitions {

    private final String BASE_URL = "http://localhost:9000/api/v1/trainer";
    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<?> successResponse;
    private ResponseEntity<String> errorResponse;

    @Given("the trainer service is running")
    public void theTrainerServiceIsRunning() {
        UserDTO userDTO = new UserDTO("test.trainer", "Password12");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);
        restTemplate.postForEntity(
                "http://localhost:9000/api/v1/user/login",
                requestEntity,
                String.class
        );

    }

    @When("a new trainer is registered with the following details:")
    public void aNewTrainerIsRegisteredWithTheFollowingDetails(DataTable trainerDetails) {
        HttpHeaders headers = new HttpHeaders();
        RequestTrainerDTO trainerDTO = new RequestTrainerDTO(trainerDetails.cell(1, 0), trainerDetails.cell(1, 1), "tennis", trainerDetails.cell(1, 2));
        HttpEntity<RequestTrainerDTO> requestEntity = new HttpEntity<>(trainerDTO, headers);
        try {
            successResponse = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, Map.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("the trainer should be successfully created")
    public void theTrainerShouldBeSuccessfullyCreated() {
        assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());

    }

    @Then("the trainer registration should result in a validation error")
    public void theTrainerRegistrationShouldResultInAValidationError() {
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());

    }

    @Given("a trainer with username {string} exists")
    public void aTrainerWithUsernameExists(String arg0) {

    }

    @When("the trainer profile is requested for the username {string}")
    public void theTrainerProfileIsRequestedForTheUsername(String username) {
        try {
            successResponse = restTemplate.getForEntity(BASE_URL.concat("/").concat(username), TrainerDTO.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("the trainer profile should be retrieved successfully")
    public void theTrainerProfileShouldBeRetrievedSuccessfully() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Then("the trainer profile should not be retrieved successfully")
    public void theTrainerProfileShouldNotBeRetrievedSuccessfully() {
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
    }

    @When("the trainer profile for username {string} is updated with the following details:")
    public void theTrainerProfileForUsernameIsUpdatedWithTheFollowingDetails(String username, DataTable dataTable) {

        TrainerDTO updatedTrainerDTO = new TrainerDTO();
        updatedTrainerDTO.setSpecialization(dataTable.cell(1, 0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TrainerDTO> requestEntity = new HttpEntity<>(updatedTrainerDTO, headers);
        try {
            successResponse = restTemplate.exchange(BASE_URL + "/" + username, HttpMethod.PUT, requestEntity, TrainerDTO.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("the trainer profile should be successfully updated")
    public void theTrainerProfileShouldBeSuccessfullyUpdated() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());

    }

    @Then("the trainer profile should not be successfully updated")
    public void theTrainerProfileShouldNotBeSuccessfullyUpdated() {
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
    }

    @Given("trainee valid profile")
    public void traineeValidProfile() {
        UserDTO userDTO = new UserDTO("test.trainee", "Password12");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);
        restTemplate.postForEntity(
                "http://localhost:9000/api/v1/user/login",
                requestEntity,
                String.class
        );
    }

    @When("the list of trainers not assigned to a trainee is requested")
    public void theListOfTrainersNotAssignedToATraineeIsRequested() {
        try {
            successResponse = restTemplate.exchange(BASE_URL + "/not-assigned", HttpMethod.GET, null, List.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }

    }

    @Then("the list of trainers not assigned should be retrieved successfully")
    public void theListOfTrainersNotAssignedShouldBeRetrievedSuccessfully() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @When("the trainer profile for username {string} is deleted")
    public void theTrainerProfileForUsernameIsDeleted(String arg0) {
        try {
            successResponse = restTemplate.exchange(BASE_URL + "/" + arg0, HttpMethod.DELETE, null, String.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("the trainer profile should be successfully deleted")
    public void theTrainerProfileShouldBeSuccessfullyDeleted() {
        assertEquals(HttpStatus.NO_CONTENT, successResponse.getStatusCode());
    }


}
