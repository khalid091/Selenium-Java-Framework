Feature: Register Functionality
  As a user
  I want to be able to register for a new account
  So that I can access the application

  @smoke
  Scenario: User Registration Flow
    Given user is on the login page
    When user fills username and email from row 1
    And user clicks signup button