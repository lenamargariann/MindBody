package com.epam.xstack.cucumber;


import com.epam.xstack.model.dto.RequestTraineeDTO;
import com.epam.xstack.model.dto.TraineeDTO;
import com.epam.xstack.model.dto.UserDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraineeStepsDefinitions {

    private final String BASE_URL = "http://localhost:9000/api/v1/trainee";
    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<?> successResponse;
    private ResponseEntity<String> errorResponse;


    @Given("^the trainee service is running$")
    public void theTraineeServiceIsRunning() {
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


    @When("^a new trainee is registered with the following details:$")
    public void aNewTraineeIsRegisteredWithTheFollowingDetails(io.cucumber.datatable.DataTable traineeDetails) {
        HttpHeaders headers = new HttpHeaders();
        RequestTraineeDTO traineeDTO = new RequestTraineeDTO(traineeDetails.cell(1, 0), traineeDetails.cell(1, 1), LocalDate.now(), "address", traineeDetails.cell(1, 2));
        HttpEntity<RequestTraineeDTO> requestEntity = new HttpEntity<>(traineeDTO, headers);

        try {
            successResponse = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, Map.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("^the trainee should be successfully created$")
    public void theTraineeShouldBeSuccessfullyCreated() {
        assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
    }

    @Then("the trainee should not be successfully created")
    public void theTraineeShouldNotBeSuccessfullyCreated() {
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    }


    @When("^the trainee profile is requested for the username \"([^\"]*)\"$")
    public void theTraineeProfileIsRequestedForTheUsername(String arg0) {
        try {
            successResponse = restTemplate.getForEntity(BASE_URL.concat("/").concat(arg0), TraineeDTO.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("^the trainee profile should be retrieved successfully$")
    public void theTraineeProfileShouldBeRetrievedSuccessfully() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());

    }

    @Then("the trainee profile should not be retrieved successfully")
    public void theTraineeProfileShouldNotBeRetrievedSuccessfully() {
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
    }

    @When("^the trainee profile for username \"([^\"]*)\" is updated with the following details:$")
    public void theTraineeProfileForUsernameIsUpdatedWithTheFollowingDetails(String arg0, DataTable dataTable) {
        TraineeDTO updatedTraineeDTO = new TraineeDTO();
        updatedTraineeDTO.setAddress(dataTable.cell(1, 0));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TraineeDTO> requestEntity = new HttpEntity<>(updatedTraineeDTO, headers);
        try {
            successResponse = restTemplate.exchange(
                    BASE_URL + "/" + arg0,
                    HttpMethod.PUT,
                    requestEntity,
                    TraineeDTO.class
            );
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }


    @Then("^the trainee profile should be successfully updated$")
    public void theTraineeProfileShouldBeSuccessfullyUpdated() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());

    }

    @Then("^the trainee profile should not be successfully found and updated$")
    public void theTraineeProfileShouldNotBeSuccessfullyUpdated() {
        assertEquals(HttpStatus.NOT_FOUND, errorResponse.getStatusCode());
    }

    @When("^the trainers for the trainee with username \"([^\"]*)\" are updated with the following usernames:$")
    public void theTrainersForTheTraineeWithUsernameAreUpdatedWithTheFollowingUsernames(String arg0, DataTable dataTable) {
        List<String> trainers = Arrays.asList(dataTable.cell(1, 0), dataTable.cell(1, 1));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<String>> requestEntity = new HttpEntity<>(trainers, headers);
        try {
            successResponse = restTemplate.exchange(
                    BASE_URL + "/" + arg0 + "/trainers",
                    HttpMethod.PUT,
                    requestEntity,
                    List.class
            );
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("^the trainers for the trainee should be successfully updated$")
    public void theTrainersForTheTraineeShouldBeSuccessfullyUpdated() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Then("^the trainers for the trainee should not be successfully updated$")
    public void theTrainersForTheTraineeShouldNotBeSuccessfullyUpdated() {
        assertEquals(HttpStatus.BAD_REQUEST, errorResponse.getStatusCode());
    }

    @When("^the trainee profile for username \"([^\"]*)\" is deleted$")
    public void theTraineeProfileForUsernameIsDeleted(String arg0) {
        try {
            successResponse = restTemplate.exchange(BASE_URL + "/" + arg0, HttpMethod.DELETE, null, String.class);
        } catch (HttpClientErrorException ex) {
            errorResponse = new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
        }
    }

    @Then("^the trainee profile should be successfully deleted$")
    public void theTraineeProfileShouldBeSuccessfullyDeleted() {
        assertEquals(HttpStatus.NO_CONTENT, successResponse.getStatusCode());
    }


}
