Feature: Orientations
  In order to see how my app behaves in different orientations
  As a Calabash user
  I need to be able to see my app in various orientations

  Scenario: Portrait
    Given I see a view with orientation "Portrait"
    Then the app should be shown in orientation "Portrait"

  Scenario: Reverse Portrait
    Given I see a view with orientation "Reverse Portrait"
    Then the app should be shown in orientation "Reverse Portrait"

  Scenario: Landscape
    Given I see a view with orientation "Landscape"
    Then the app should be shown in orientation "Landscape"

  Scenario: Reverse Landscape
    Given I see a view with orientation "Reverse Landscape"
    Then the app should be shown in orientation "Reverse Landscape"
