Feature: REST API Feature

  Scenario: Making an unauthenticated request to Gateway
    When request is sent to authenticated endpoint
    Then sender gets authentication error

  Scenario: Making request to an unauthenticated endpoint
    When request is sent to unauthenticated endpoint
    Then Sender gets created response

  Scenario: Making an authenticated request to Gateway
    Given authenticating request
    When request is sent to authenticated endpoint
    Then Sender gets success response

  Scenario: Checking interconnection between MindBodyMain and MindBodyTraining services
    When new training is saved in MindBodyMain's db
    Then new workload is added to workloads db

  Scenario: Checking MindBody service working without Training service up
    Given Training service is down
    When Training is saved in MindBody Main
    Then Sender gets created response



