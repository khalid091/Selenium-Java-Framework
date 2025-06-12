Feature: Register Functionality
  As a user
  I want to be able to login to the application
  So that I can access my account

  @smoke
  Scenario: User Registered Successfully
    Given user is in the login page
    When user fill username and email
    And user click signup button