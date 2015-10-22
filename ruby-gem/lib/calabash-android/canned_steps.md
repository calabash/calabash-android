Canned steps
============
Calabash Android comes with the following set of predefined steps.
You can add your own steps or change the ones you see here.

Assertion steps
---------------

To assert that specified text can be found use any of the following steps.

    Then /^I see the text "([^\"]*)"$/
    Then /^I see "([^\"]*)"$/
    Then /^I should see "([^\"]*)"$/
    Then /^I should see text containing "([^\"]*)"$/

To assert that specified text cannot be found use any of the following steps.

    Then /^I should not see "([^\"]*)"$/
    Then /^I don't see the text "([^\"]*)"$/
    Then /^I don't see "([^\"]*)"$/  


Input steps
-----------

    Then /^I toggle checkbox number (\d+)$/ do |index|
Toggles the checkout with the specified index.

    Then /^I long press "([^\"]*)"$/ do |text|
Long presses the view containing the specified text.

	Then /^I long press "([^\"]*)" and select item number (\d+)$/ do |text, index|
**This predefined step is deprecated**

Long presses the view containing the specified text and selects the menu item with the specified index in the context menu that appears.

    Then /^I long press "([^\"]*)" and select "([^\"]*)"$/ do |text, identifier|
**This predefined step is deprecated**

Long presses the view containing the specified text and selects the menu item marked by the specified identifier in the context menu that appears. 


    Given /^I set the date to "(\d\d-\d\d-\d\d\d\d)" on DatePicker with index "([^\"]*)"$/ do |date, index|
Finds the datepicker with the specified index and changes the date.


    Given /^I set the time to "(\d\d:\d\d)" on TimePicker with index "([^\"]*)"$/
    Given /^I set the "([^\"]*)" time to "(\d\d:\d\d)"$/
Finds the timepicker with the specified index and changes the time.

    Given /^I set the "([^\"]*)" date to "(\d\d-\d\d-\d\d\d\d)"$/ do |content_description, date|
Finds the datepicker by content description and changes the date.

    Then /^I enter "([^\"]*)" into input field number (\d+)$/ do |text, index|
Enters the specified text into the input field with the specified index.

	Then /^I enter text "([^\"]*)" into field with id "([^\"]*)"$/ do |text, id|
Enters the specified text into the input field with the specified id.

    Then /^I enter "([^\"]*)" as "([^\"]*)"$/ do |text, content_description|
    Then /^I enter "([^\"]*)" into "([^\"]*)"$/ do |text, content_description|
Enters the specified text into the input field that has the specified content description.

    Then /^I clear input field number (\d+)$/ do |index|
Clears the text of the input field with the specified index.

    Then /^I clear "([^\"]*)"$/ do |identifier|
Clears the text of input fields marked by the identifier.

    Then /^I clear input field with id "([^\"]*)"$/ do |id|
Clears the text of the input field with the specified id.

    Then /^I select "([^\"]*)" from "([^\"]*)"$/ do |item_identifier, spinner_identifier|
Finds the spinner marked by the specified 'spinner_identifier' or has a childview marked by the specified 'spinner_identifier'. It then selects the menu item marked by the specified 'item_identifier'.

Buttons
-------

    Then /^I go back$/
Simulates that the user pressed the back button.

    Then /^I press the menu key$/
Simulates that the user pressed the menu button.    

    Then /^I press the enter button$/
Simulates that the user pressed the enter button on the keyboard.    

Gestures
--------
    Then /^I swipe left$/
Swipes left.

    Then /^I swipe right$/
Swipes right.
    
    Then /^I scroll down$/
Scrolls down.
    
    Then /^I scroll up$/
Scrolls up.

	Then /^I select "([^\"]*)" from the menu$/ do |identifier|
Opens the menu by simulating pressing the menu button and then selects a menu item marked by the specified identifier.

    
	Then /^I drag from (\d+):(\d+) to (\d+):(\d+) moving with (\d+) steps$/ do |from_x, from_y, to_x, to_y, steps|
Drags from one point on the screen to another.

**Note: x:y co-ordinates are expressed as percentages of the screen width:height**
    
Touching
--------
    
	Given /^I press the "([^\"]*)" button$/ do |text|
Taps the button containing the specified text.

    Then /^I press button number (\d+)$/ do |index|
Taps the button with the specified index.

    Then /^I press image button number (\d+)$/ do |index|
Taps the image button with the specified index.

    Then /^I press view with id "([^\"]*)"$/ do |id|
Taps the view with the give id.

    Then /^I press "([^\"]*)"$/ do |identifier|
Taps the view marked by the specified identifier.

    Then /^I touch the "([^\"]*)" text$/ do |text|
Taps the specified text.

    Then /^I press list item number (\d+)$/ do |index|
**This predefined step is deprecated**

Taps the list item with the specified index in the first visible list.

    Then /^I long press list item number (\d+)$/ do |index|
**This predefined step is deprecated**

Long presses the list item with the specified index in the first visible list.

    Then /^I click on screen (\d+)% from the left and (\d+)% from the top$/ do |x, y|     
Taps the screen at the specified location.

Waiting
-------

    Then /^I wait for progress$/ do
Will wait until there are no more progress bars.

    Then /^I wait for "([^\"]*)" to appear$/ do |text|
    Then /^I wait to see "([^\"]*)"$/ do |text|
Waits for the specified text to appear.

    Then /^I wait up to (\d+) seconds for "([^\"]*)" to appear$/ do |timeout, text|
    Then /^I wait up to (\d+) seconds to see "([^\"]*)"$/ do |timeout, †ext|
Waits for the specified text to appear, with a custom timeout

    Then /^I wait for the "([^\"]*)" button to appear$/ do |identifier|
Waits for a button marked by the specified identifier to appear.

    Then /^I wait for the "([^\"]*)" screen to appear$/ do |activity_name|
Waits for a particular screen (Android Activity) to appear.

    Then /^I wait for the view with id "([^\"]*)" to appear$/ do |id|
Waits for a view with the specified if to appear.

    Then /^I wait up to (\d+) seconds for the "([^\"]*)" screen to appear$/ do |timeout, activity_name|
    Then /^I wait upto (\d+) seconds for the "([^\"]*)" screen to appear$/ do |timeout, activity_name|
Waits for a particular screen (Android Activity) to appear with a timeout.

    Then /^I wait for 1 second$/
    Then /^I wait for a second$/
Waits for one second.

    Then /^I wait$/
Waits for two seconds.
  
    Then /^I wait for (\d+) seconds$/ do |seconds|
Waits for a specified number of seconds.

Screenshots
-----------
To take a screenshot of the phone while running the test use any of these steps.

    Then /^take picture$/
    Then /^I take a picture$/
    Then /^I take a screenshot$/


Location steps
--------------
If you allow your phone to use mocked locations (configured on your device under development settings) and your app has the `ACCESS_MOCK_LOCATION` permission you can change the perceived location of the device by using any of these steps.


You can change the location any address or named location. This is done using the [geocoder gem](http://www.rubygeocoder.com/).


    Then /^I am in "([^\"]*)"$/ do |location|
    Then /^I am at "([^\"]*)"$/ do |location|
    Then /^I go to "([^\"]*)"$/ do |location|

To use a set of concrete GPS cordinates

    Then /^I am at ([-+]?[0-9]*\.?[0-9]+), ([-+]?[0-9]*\.?[0-9]+)$/ do |latitude, longitude|
    Then /^I go to ([-+]?[0-9]*\.?[0-9]+), ([-+]?[0-9]*\.?[0-9]+)$/ do |latitude, longitude|

Internationalization
--------------------

	Then /^I press text of translated l10nkey "?([^\"]*)"?$/ 
Simulates that the user pressed the text of the l10nkey.	

	Then /^I press button of translated l10nkey "?([^\"]*)"?$/
Simulates that the user pressed the button with the label text of the l10nkey.

	Then /^I press menu item of translated l10nkey "?([^\"]*)"?$/
Simulates that the user pressed the menu item with the label text of the l10nkey.

	Then /^I press toggle button of translated l10nkey "?([^\"]*)?"$/ 
Simulates that the user pressed the toggle button with the label text of the l10nkey.	

	Then /^I wait for the translated "?([^\"]*)"? l10nkey to appear$/ 
Waits until the text of the translated l10nkey is displayed.

Note: you can assert or press interface elements using [Android's String resources](http://developer.android.com/reference/android/R.string.html) by passing a package in a custom step:

    perform_action('press_l10n_element', 'ok', nil, 'android')

