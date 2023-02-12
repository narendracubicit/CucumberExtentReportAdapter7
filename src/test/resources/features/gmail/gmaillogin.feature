Feature: Gmail Login Feature

  @Regression @Gmail
  Scenario: Gmail login scenario1
    Given I open "https://www.gmail.com"
    When user log into gmail using "username" and "password"

    @Regression @Gmail
  Scenario: Gmail login scenario2
    Given I open "https://www.gmail.com"
    Given I print something