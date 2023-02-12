Feature: Facebook Login Feature

  @Regression @FB
  Scenario: FB login feature1
    Given I open "https://www.fb.com"
    When user log into fb using "username" and "password"

  @Regression @FB
  Scenario: FB login scenario2
    Given I open "https://www.fb.com"
    Given I print something