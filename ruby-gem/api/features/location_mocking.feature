Feature: Mocking the location of the application

  Calabash is able to mock the location of the application. It will mock all
  active providers. These are often GPS and wifi.


  Scenario Outline: Mocking the location of the device
    Given the application is listening for coordinates based on <type>
    When Calabash is asked to mock the location to certain coordinates
    Then the application will believe the device is current at those coordinates
    Examples:
      | type  |
      | GPS   |
      | wifi  |


  Scenario Outline: Mocking the location of the device twice
    Given the application is listening for coordinates based on <type>
    When Calabash is asked to mock the location to certain coordinates
    Then the application will believe the device is current at those coordinates
    When Calabash is asked to mock the location again
    Then the application will believe the device is current at the new coordinates
    Examples:
      | type  |
      | GPS   |
      | wifi  |