@webview
Feature: Webviews
  # A user should be able to query for elements within an iframe. e.g. query("ww css:'iframe' css:'...'")
  Scenario: Querying for elements inside a non-cross domain iframe
    Given I am in a webview with an iframe
    When I search for elements inside an iframe
    Then I should be able to interact with them

  # A user should get a good error message in an exception when executing invalid javascript
  Scenario: Evaluating bad javascript
    Given I have a webview available
    When I evaluate bad javascript
    Then I should get an error with an javascript exception

  # A user should get no elements when the webview has no body, not an exception
  Scenario: Querying for elements with no document.body
    Given I have a webview available
    And it has no body
    When I query for elements
    Then none should be returned