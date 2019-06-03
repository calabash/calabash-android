Feature: General Calabash 1 Test
  Background
    Given I run the application
    And I wait for the "View Data" view to appear

  Scenario: Input values
    When I set the date to "20-03-2015" and time to "15:32"
    And I go to the views screen
    Then I press the first list item
    And I select the "B" radio button
    And I check the checkbox
    And I set the seek bar to 60 %
    Then the displayed data should be correct
