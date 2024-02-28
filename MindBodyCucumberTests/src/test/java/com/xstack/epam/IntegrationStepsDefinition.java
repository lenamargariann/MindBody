package com.xstack.epam;


import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationStepsDefinition {
    private static final RestTemplate client = new RestTemplate();
    private final static String BASE_URL = "http://localhost:9091/%1$s/api/v1/%2$s";
    private static Process activeMQ;
    private ResponseEntity<?> successResponse;
    private ResponseEntity<?> errorResponse;
    private static final HttpHeaders headers = new HttpHeaders();
    private static String securityHeader;
    private static String trainingStr;


    @SneakyThrows
    @BeforeAll
    public static void beforeAll() {
        String batchFilePath = "C:\\Users\\lenam\\Documents\\apache-activemq-6.0.1\\bin\\activemq.bat";
        String[] commandTest = {"cmd", "/c", "C:\\Program^ Files\\apache-maven-3.9.6\\bin\\mvn", "spring-boot:run", "-Dspring-boot.run.profiles=test"};
        String[] command = {"cmd", "/c", "C:\\Program^ Files\\apache-maven-3.9.6\\bin\\mvn", "spring-boot:run"};
        String[] activeMQCommands = {"cmd", "/c", batchFilePath, "start"};
        ProcessBuilder processBuilder = new ProcessBuilder(activeMQCommands)
                .directory(new File("C:\\Users\\lenam\\Documents\\apache-activemq-6.0.1\\bin\\"));
        try {
            processBuilder.redirectErrorStream(true);
            activeMQ = processBuilder.start();
        } catch (IOException e) {
            System.out.println("Could not start process." + e.getMessage());
        }


        assertTrue(activeMQ.isAlive());

        String BASE = "C:\\Users\\lenam\\Documents\\IdeaProjects\\MindBody\\";
        Process discovery = new ProcessBuilder(command).directory(new File(BASE + "MindBodyDiscoveryService")).start();
        assertTrue(discovery.isAlive());
        try (InputStreamReader inputStreamReader = new InputStreamReader(discovery.getInputStream());
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;


            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Started MindBodyDiscoveryApplication")) {
                    System.out.println("Spring Boot application started successfully.");
                    break;
                }
            }
        }

        Process gateway = new ProcessBuilder(commandTest).directory(new File(BASE + "MindBodyGateway")).start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(gateway.getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Started MindBodyGatewayApplication")) {
                    System.out.println("Spring Boot application started successfully.");
                    break;
                }
            }
        }
        assertTrue(gateway.isAlive());

        Process main = new ProcessBuilder(commandTest).directory(new File(BASE + "MindBodyMain")).start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(main.getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Started MindBodyApplication")) {
                    System.out.println("Spring Boot application started successfully.");
                    break;
                }
            }
        }
        assertTrue(main.isAlive());

        Process training = new ProcessBuilder(commandTest).directory(new File(BASE + "MindbodyTrainingService")).start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(training.getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("Started MindbodyTrainingServiceApplication")) {
                    System.out.println("Spring Boot application started successfully.");
                    break;
                }
            }
        }
        assertTrue(training.isAlive());


        main.waitFor(40, TimeUnit.SECONDS);
        headers.setContentType(MediaType.APPLICATION_JSON);


    }


    @When("^request is sent to authenticated endpoint$")
    public void sendRequestToAuthenticatedEndpoint() {
        try {
            headers.set("cookie", securityHeader);
            successResponse = client.exchange(String.format(BASE_URL, "main-service-path", "trainer/jane.smith"), HttpMethod.GET, null, Object.class);
        } catch (HttpClientErrorException e) {
            errorResponse = ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    @Then("Sender gets created response")
    public void senderGetsCreatedResponse() {
        assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());

    }

    @Then("^sender gets authentication error$")
    public void getAuthenticationError() {
        assertEquals(HttpStatus.UNAUTHORIZED, errorResponse.getStatusCode());
    }

    @When("request is sent to unauthenticated endpoint")
    public void requestIsSentToUnauthenticatedEndpoint() {
        String request = """
                {
                "firstname": "Jane",
                "lastname": "Jackson",
                "specialization": "yoga",
                "password": "Password123"
                }""";
        HttpEntity<?> requestEntity = new HttpEntity<>(request, headers);
        try {
            successResponse = client.exchange(String.format(BASE_URL, "main-service-path", "trainer"), HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            errorResponse = ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }

    }

    @Given("authenticating request")
    public void authenticatingRequest() {
        String userDTO = """
                {
                    "username": "test.trainer",
                    "password": "Password12"
                }
                """;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(userDTO, headers);
        successResponse = client.postForEntity(
                "http://localhost:9000/api/v1/user/login",
                requestEntity,
                String.class
        );
        securityHeader = Objects.requireNonNull(successResponse.getHeaders().get("Set-Cookie")).get(0);
    }

    @Then("^Sender gets success response$")
    public void getSuccessResponse() {
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @When("new training is saved in MindBodyMain's db")
    public void newTrainingIsSavedInMindBodyMainSDb() {
        trainingStr = """
                {
                  "traineeUsername": "test.trainee",
                  "trainerUsername": "test.trainer",
                  "name": "yoga",
                  "date": "21-01-2022 12:30",
                  "duration": 60
                }
                """;
        headers.set("cookie", securityHeader);
        HttpEntity<String> requestEntity = new HttpEntity<>(trainingStr, headers);
        try {
            client.exchange(String.format(BASE_URL, "main-service-path", "training"),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );


            successResponse = client.exchange(
                    String.format(BASE_URL, "training-service-path", "workload/test.trainer"),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class);
        } catch (HttpClientErrorException e) {
            errorResponse = ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
            System.out.println(errorResponse.getStatusCode() + " " + errorResponse.getBody());
        }


    }


    @Then("new workload is added to workloads db")
    public void newWorkloadIsAddedToWorkloadsDb() {
        System.out.println("BODY " + successResponse.getBody());
        String trainerWorkload = "\\{\"id\":\"\\w+\",\"trainerUsername\":\"test\\.trainer\",\"trainerFirstname\":\"test\",\"trainerLastname\":\"trainer\",\"years\":\\{\"2022\":\\{\"JANUARY\":-?\\d+}},\"isActive\":false}";
        assertTrue(Pattern.matches(trainerWorkload, Objects.requireNonNull(successResponse.getBody()).toString()));
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }


    @Given("Training service is down")
    public void trainingServiceIsDown() {
        try {
            var response = client.exchange("http://localhost:9073/actuator/shutdown", HttpMethod.POST, new HttpEntity<>(headers), String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Training shot down");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @When("Training is saved in MindBody Main")
    public void trainingIsSavedInMindBodyMain() {
        try {
            headers.set("cookie", securityHeader);
            successResponse = client.exchange(String.format(BASE_URL, "main-service-path", "training"),
                    HttpMethod.POST,
                    new HttpEntity<>(trainingStr, headers),
                    String.class
            );
        } catch (HttpClientErrorException e) {
            errorResponse = ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }


    @AfterAll
    public static void afterAll() {
        activeMQ.destroy();
        assertFalse(activeMQ.isAlive());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {

            var response = client.exchange("http://localhost:9070/actuator/shutdown", HttpMethod.POST, requestEntity, String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Discovery shot down");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            var response = client.exchange("http://localhost:9071/actuator/shutdown", HttpMethod.POST, requestEntity, String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Gateway shot down");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            var response = client.exchange("http://localhost:9072/actuator/shutdown", HttpMethod.POST, requestEntity, String.class);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            System.out.println("Main shot down");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("AfterAll is called");
    }


}
