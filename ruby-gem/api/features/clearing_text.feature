@keyboard
Feature: Clearing text

  Calabash is capable of entering text in any view that is editable, this
  includes both webviews and native views. Calabash simulates a keyboard
  and gesture interaction, causing the same app behaviour as a user clearing
  the field.

  Scenario: Clearing text in a view
    Given an editable view with text in it
    When the user asks to clear the text of the view
    Then the view is focused by tapping it
    And the text is cleared using selection and the keyboard

  Scenario: Entering text into a webview
    Given a webview with an editable element with text in it
    When the user asks to clear the text of the element
    Then the webview element is focused by tapping it
    And the text is cleared using selection and the keyboard
