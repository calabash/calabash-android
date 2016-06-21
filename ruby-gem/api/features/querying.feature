@querying
Feature: Querying

  Calabash is capable of querying for all views rendered by the application
  under test. The queries can match for any predicate by invoking a method on
  the view. They can match for class names of the native views. They can match
  for a certain position in the view hierarchy, i.e. ancestors, parents,
  descendants, children, or siblings. It can also match for visibility.

  Scenario: Querying for a native view by id
    Given a screen with a view with some id
    When Calabash queries for a any view with that id
    Then the list of results should contain that element

  Scenario: Querying for a native view by text
    Given a screen with a view with some text
    When Calabash queries for a any view with that text
    Then the list of results should contain that element

  Scenario: Querying for a native view by simple class name
    Given a screen with a view of some class
    When Calabash queries for any view of that particular class
    Then the list of results should contain that element

  Scenario: Querying for a native view by class name using inheritance
    Given a screen with a view of some class that inherits from another class
    When Calabash queries for any view of the fully qualified parent class
    Then the list of results should contain that element

  Scenario: Querying for webview elements using css
    Given a webview with an element in it with some html id
    When Calabash queries for a any element in the webview with that html id
    Then the list of results should contain that element

  Scenario: Querying for elements in a dialogue
    Given a dialogue is rendered on screen
    When Calabash queries for elements
    Then the list of results should contain both elements from the dialogue as well as elements outside the dialogue

  # Calabash searches for visible elements by default
  Scenario: Querying for a native view that is invisible without specifying any visibility
    Given a screen with a view that is invisible
    When Calabash queries for the invisible view
    Then the list of results should not contain that element

  Scenario: Querying for a native view that is invisible by specifying any visibility
    Given a screen with a view that is invisible
    When Calabash queries for the invisible view with the query specifying any visibility
    Then the list of results should contain that element
