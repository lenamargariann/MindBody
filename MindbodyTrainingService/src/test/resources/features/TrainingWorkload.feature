Feature: TrainerWorkloadController API

  Background:
    Given the trainer workload service is running


  Scenario: Add new trainer workload
    When a new trainer workload is added with the following details:
      | firstname | lastname | username     | date             | duration |
      | test      | user     | test.trainer | 01-01-2023 10:00 | 120      |
    Then the trainer workload request should be successful


  Scenario: Get trainer workload by username
    When the trainer workload for username "test.trainer" is requested
    Then the trainer workload request should be successful

  Scenario: Delete  trainer workload
    When a trainer workload is deleted with the following details:
      | firstname | lastname | username     | date             | duration |
      | test      | user     | test.trainer | 01-01-2023 10:00 | 30       |
    Then the trainer workload request should be successful