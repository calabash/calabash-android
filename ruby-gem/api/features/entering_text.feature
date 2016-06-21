@keyboard
Feature: Text entry

  Calabash is capable of entering text into any view that is editable, this
  includes both webviews and native views. Calabash simulates a keyboard
  interaction, causing the same app behaviour as a user typing.

  https://developer.xamarin.com/guides/testcloud/calabash/calabash-query-syntax/#Entering_text

  Scenario: Entering text into a view
    Given an editable view
    When the user asks to enter text
    Then the view is focused by tapping it
    And text is entered using the keyboard

  Scenario: Entering text into a webview
    Given a webview with an editable field
    When the user asks to enter text
    Then the webview element is focused by tapping it
    And text is entered using the keyboard
