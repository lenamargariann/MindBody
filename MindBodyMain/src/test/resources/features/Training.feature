Feature: Training Controller API

  Background:
    Given the training service is running
    And the trainee and trainer profiles exist

  Scenario: Add a new training session successfully
    When a new training session is added with the following details:
      | traineeUsername | trainerUsername | name             | date             | duration |
      | test.trainee    | test.trainer    | Training Session | 02-01-2023 10:00 | 60       |
    Then the training session should be created successfully

  Scenario: Add a new training session with invalid trainee username
    When a new training session is added with the following details:
      | traineeUsername | trainerUsername | name            | date             | duration |
      | invalid.trainee | test.trainer    | Invalid Session | 01-01-2023 11:00 | 90       |
    Then the training session creation should result in a validation error

  Scenario: Add a new training session with invalid trainer username
    When a new training session is added with the following details:
      | traineeUsername | trainerUsername | name            | date             | duration |
      | test.trainee    | invalid.trainer | Invalid Session | 01-01-2023 12:00 | 120      |
    Then the training session creation should result in a validation error

  Scenario: Delete a training session successfully
    When a training session is deleted with the following details:
      | traineeUsername | trainerUsername | name             | date             | duration |
      | test.trainee    | test.trainer    | Training Session | 01-01-2023 10:00 | 60       |
    Then the training session should be deleted successfully

  Scenario: Delete a non-existing training session
    When a training session is deleted with the following details:
      | traineeUsername     | trainerUsername | name                 | date             | duration |
      | nonexistent.trainee | test.trainer    | Non-Existing Session | 01-01-2023 14:00 | 180      |
    Then the training session deletion should result in an error

  Scenario: List training sessions based on trainer profile successfully
    When the list of training sessions is requested for the trainer profile with filter:
      | username     | startDate  | endDate    |
      | test.trainer | 01-01-2023 | 01-03-2023 |
    Then the list of training sessions should be retrieved successfully

  Scenario: List training sessions based on trainee profile successfully
    When the list of training sessions is requested for the trainee profile with filter:
      | username     | startDate  | endDate    |
      | test.trainee | 01-01-2023 | 01-03-2023 |
    Then the list of training sessions should be retrieved successfully



    