@gesture
Feature: Tapping

  Calabash is capable of tapping any visible native view and webview element.
  It simulates a user gesture based on the coordinates returned by the query.

  Scenario: Tapping on a native view
    Given any visible view
    When Calabash is asked to tap it
    Then it will perform a tap gesture on the coordinates of the view

  Scenario: Tapping on a webview element
    Given any visible webview element
    When Calabash is asked to tap it
    Then it will perform a tap gesture on the coordinates of the element

  Scenario: Tapping elements in a dialogue
    Given a dialogue is rendered on screen
    When Calabash is asked to tap an element in the dialogue
    Then it will perform a tap gesture on the coordinates of the view in the dialogue