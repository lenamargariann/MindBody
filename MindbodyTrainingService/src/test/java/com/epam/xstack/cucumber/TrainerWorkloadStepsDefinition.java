package com.epam.xstack.cucumber;

import com.epam.xstack.model.TrainerWorkload;
import com.epam.xstack.model.TrainerWorkloadMessage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerWorkloadStepsDefinition {

    private final String BASE_URL = "http://localhost:8096/api/v1/workload";
    private final RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<?> successResponse;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


    @Given("the trainer workload service is running")
    public void theTrainerWorkloadServiceIsRunning() {
    }

    @When("a new trainer workload is added with the following details:")
    public void aNewTrainerWorkloadIsAddedWithTheFollowingDetails(DataTable details) {
        TrainerWorkloadMessage workload = TrainerWorkloadMessage.builder()
                .firstname(details.cell(1, 0))
                .lastname(details.cell(1, 1))
                .username(details.cell(1, 2))
                .isActive(true)
                .trainingDate(LocalDateTime.parse(details.cell(1, 3), formatter))
                .trainingDuration(Integer.parseInt(details.cell(1, 4))).build();
        HttpEntity<TrainerWorkloadMessage> requestEntity = new HttpEntity<>(workload);

        successResponse = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                requestEntity,
                TrainerWorkload.class);

    }

    @When("a trainer workload is deleted with the following details:")
    public void aTrainerWorkloadIsDeletedWithTheFollowingDetails(DataTable details) {
        TrainerWorkloadMessage workload = TrainerWorkloadMessage.builder()
                .firstname(details.cell(1, 0))
                .lastname(details.cell(1, 1))
                .username(details.cell(1, 2))
                .isActive(true)
                .trainingDate(LocalDateTime.parse(details.cell(1, 3), formatter))
                .trainingDuration(Integer.parseInt(details.cell(1, 4))).build();
        HttpEntity<TrainerWorkloadMessage> requestEntity = new HttpEntity<>(workload);

        successResponse = restTemplate.exchange(
                BASE_URL,
                HttpMethod.DELETE,
                requestEntity,
                TrainerWorkload.class);

    }

    @When("the trainer workload for username {string} is requested")
    public void theTrainerWorkloadForUsernameIsRequested(String username) {
        successResponse = restTemplate.getForEntity(BASE_URL + "/" + username, TrainerWorkload.class);

    }

    @Then("the trainer workload request should be successful")
    public void theTrainerWorkloadShouldBeRetrievedSuccessfully() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }



}
