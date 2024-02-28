Feature: Trainer RestController API

  Background:
    Given the trainer service is running

  Scenario: Register a new trainer
    When a new trainer is registered with the following details:
      | firstname | lastname | password   |
      | firstname | lastname | Password12 |
    Then the trainer should be successfully created

  Scenario: Attempt to register a new trainer with invalid details
    When a new trainer is registered with the following details:
      | firstname | lastname | password |
      | firstname | lastname | password |
    Then the trainer registration should result in a validation error


  Scenario: Get trainer profile by username
    When the trainer profile is requested for the username "firstname.lastname"
    Then the trainer profile should be retrieved successfully


  Scenario: Attempt to get a non-existing trainer profile
    When the trainer profile is requested for the username "nonexistent.trainer"
    Then the trainer profile should not be retrieved successfully


  Scenario: Update trainer profile
    Given a trainer with username "firstname.lastname" exists
    When the trainer profile for username "firstname.lastname" is updated with the following details:
      | specialization |
      | yoga           |
    Then the trainer profile should be successfully updated


  Scenario: Attempt to update a non-existing trainer profile
    When the trainer profile for username "nonexistent.trainer" is updated with the following details:
      | specialization |
      | yoga           |
    Then the trainer profile should not be successfully updated


  Scenario: List trainers not assigned to a trainee
    When the list of trainers not assigned to a trainee is requested
    Then the list of trainers not assigned should be retrieved successfully


  Scenario: Delete a trainer profile
    When the trainer profile for username "firstname.lastname" is deleted
    Then the trainer profile should be successfully deleted

