Feature: General Calabash Test
  Background
    Given the application is running

  Scenario: I want to mock the location on the device
    Then I can mock the location

  Scenario: Pressing Textfields Should not result in screen switch
    Given I am on the textfield screen

    When I enter text into the 0th field
    Then I should still see text in field number 0'

    When I enter text into the 2th field
    Then I should still see text in field number 2'

    # TODO Test the other fields

  Scenario: Input values
    When I set the date to "20-03-2015" and time to "15:32"
    And I go to the views screen
    Then I press the first list item
    And I select the "B" radio button
    And I check the checkbox
    And I set the seek bar to 60 %
    Then the displayed data should be correct

  Scenario: Web view
    Given I am on the web view screen
    When I submit the text "Hello World!"
    Then the result should be "Hello World!"

  Scenario: Scroll down
    Given I am on the scroll screen
    Then I should be able to scroll until I reach the bottom

  Scenario: Flick
    Given I am on the swipe screen
    Then I should be able to flick "left"
    And I should be able to flick "right"
