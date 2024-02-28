Feature: Trainee RestController API

  Background:
    Given the trainee service is running

  Scenario: Register a new trainee
    When a new trainee is registered with the following details:
      | firstname | lastname | password   |
      | firstname | lastname | Password12 |
    Then the trainee should be successfully created

  Scenario: Attempt to register a new trainee with invalid details
    When a new trainee is registered with the following details:
      | firstname | lastname | password |
      | invalid   | lastname | password |
    Then the trainee should not be successfully created

  Scenario: Get trainee profile by username
    When the trainee profile is requested for the username "firstname.lastname"
    Then the trainee profile should be retrieved successfully


  Scenario: Get invalid trainee profile by username
    When the trainee profile is requested for the username "invalid.username"
    Then the trainee profile should not be retrieved successfully


  Scenario: Update trainee profile
    When the trainee profile for username "firstname.lastname" is updated with the following details:
      | address         |
      | Address updated |
    Then the trainee profile should be successfully updated

  Scenario: Update invalid trainee profile
    When the trainee profile for username "invalid.username" is updated with the following details:
      | address         |
      | Address updated |
    Then the trainee profile should not be successfully found and updated


  Scenario: Update trainee's trainers
    When the trainers for the trainee with username "firstname.lastname" are updated with the following usernames:
      | trainerUsername | anotherTrainerUsername |
      | jane.smith      | dua.lipa               |
    Then the trainers for the trainee should be successfully updated

  Scenario: Invalid update trainee's trainers
    When the trainers for the trainee with username "firstname.lastname" are updated with the following usernames:
      | trainerUsername | anotherTrainerUsername |
      | jane_smith      | dualipa                |
    Then the trainers for the trainee should not be successfully updated


  Scenario: Delete a trainee profile
    When the trainee profile for username "firstname.lastname" is deleted
    Then the trainee profile should be successfully deleted
