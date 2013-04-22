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

    Then /^I toggle checkbox number (\d+)$/ do |checkboxNumber|
Toggles checkout with the specified index.

    Then /^I long press "([^\"]*)"$/ do |text_to_press|
Long presses the text if found.

    Then /^I long press "([^\"]*)" and select item number "([^\"]*)"$/ do |text_to_press, index|

Long presses the text (if found) and select the specified number in the context menu that appear.

    Then /^I long press "([^\"]*)" and select "([^\"]*)"$/ do |text_to_press, context_text|
Long presses the text (if found) and select the specified number in the context menu that appear. 


    Given /^I set the date to "(\d\d-\d\d-\d\d\d\d)" on DatePicker with index "([^\"]*)"$/ do |date, index|
Finds the datepicker with the specified index and changes the date.


    Given /^I set the time to "(\d\d:\d\d)" on TimePicker with index "([^\"]*)"$/
    Given /^I set the "([^\"]*)" time to "(\d\d:\d\d)"$/
Finds the timepicker with the specified index and changes the time.

    Given /^I set the "([^\"]*)" date to "(\d\d-\d\d-\d\d\d\d)"$/ do |content_description, date|
Find the datepicker by content description and changes the date.

    Then /^I enter "([^\"]*)" into input field number (\d+)$/ do |text, number|
Enters the specified text into the input field with index `number`.

    Then /^I enter text "([^\"]*)" into field with id "([^\"]*)"$/ do |text, view_id|
Enters Text into the input field with id `view_id`.

    Then /^I enter "([^\"]*)" as "([^\"]*)"$/ do |text, target|
    Then /^I enter "([^\"]*)" into "([^\"]*)"$/ do |text, target|
Enters the specified text into the input field that has a content desciption that matches the provided target.



    Then /^I clear input field number (\d+)$/ do |number|
Finds a input field by index and blanks its value.

    Then /^I clear "([^\"]*)"$/ do |name|
Finds a input field with matching content description and blanks its value.

    Then /^I clear input field with id "([^\"]*)"$/ do |view_id|
Finds the input field with id `view_id` and clears the text from it.

    Then /^I select "([^\"]*)" from "([^\"]*)"$/ do |item_text, spinner_content_description|
Finds the Spinner by 'spinner_content_description' and selects the item with the matching 'item_text'

Buttons
-------

Simulates that the user pressed the back button.

    Then /^I go back$/

Simulates that the user pressed the menu button.    

    Then /^I press the menu key$/

Simulates that the user pressed the enter button on the keyboard.    

    Then /^I press the enter button$/ do

Gestures
--------
To swipe left

    Then /^I swipe left$/

To swipe right

    Then /^I swipe right$/

To scroll down
    
    Then /^I scroll down$/

To scroll up
    
    Then /^I scroll up$/

To open the menu and press the specified text

    Then /^I select "([^\"]*)" from the menu$/
    
To drag from one point on the screen to another. 

	Then /^I drag from (\d+):(\d+) to (\d+):(\d+) moving with (\d+) steps$/ 
Note: x:y co-ordinates are expressed as percentages of the screen width:height
    
Touching
--------
    
    Given /^I press the "([^\"]*)" button$/

Search for a button with the provided text and press it if found.

    Then /^I press button number (\d+)$/

Presses the button by index.

    Then /^I press image button number (\d+)$/

Presses the image button by index.

    Then /^I press view with id "([^\"]*)"$/

Looks for a view with the provided id. If it is found and visible tries to click it.
Note that use the short name and not the fully quantified name. That means if your id
is 'com.foo.R.id.bar_label' you would use 'I press view with id "bar_label"'.

    Then /^I press "([^\"]*)"$/ do |identifier|
Will look for a view in the following order:

1. Looks for a visible button with matching text.
2. Look for a visible view with a matching content description.
3. Look for a visible view with class name that matches the provided indentifier.

If a view is found we will try to click it.

    Then /^I touch the "([^\"]*)" text$/
Will look for the specified text and press it if found.

    Then /^I press list item number (\d+)$/
Will press the specified list item in the first visible list.

    Then /^I long press list item number (\d+)$/

Will long press the specified list item in the first visible list.

    Then /^I click on screen (\d+)% from the left and (\d+)% from the top$/
Simulates a touch on the screen at the specified location.

Waiting
-------

    Then /^I wait for progress$/ do
Will wait till there is no more progress bars.

    Then /^I wait for dialog to close$/
Waits for the current dialog to close.


    Then /^I wait for "([^\"]*)" to appear$/
    Then /^I wait to see "([^\"]*)"$/
Waits for the specified text to appear.

    Then /^I wait up to (\d+) seconds for "([^\"]*)" to appear$/
    Then /^I wait up to (\d+) seconds to see "([^\"]*)"$/
Waits for the specified text to appear, with a custom timeout

    Then /^I wait for the "([^\"]*)" button to appear$/
Waits for a button with the specified text to appear.

    Then /^I wait for the "([^\"]*)" screen to appear$/ 
Waits for a particular screen (Android Activity) to appear.

    Then /^I wait for the view with id "([^\"]*)" to appear$/ do |text|
Waits for a view view that id to appear.
Note that use the short name and not the fully quantified name. That means if your id
is 'com.foo.R.id.bar_label' you would use 'I press view with id "bar_label"'.

    Then /^I wait up to (\d+) seconds for the "([^\"]*)" screen to appear$/ 
    Then /^I wait upto (\d+) seconds for the "([^\"]*)" screen to appear$/ 
Waits for a particular screen (Android Activity) to appear with a timeout.

    Then /^I wait for 1 second$/
    Then /^I wait for a second$/
Waits for one second.

    Then /^I wait$/
Waits for two seonds.
  
    Then /^I wait for (\d+) seconds$/
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

	Then /^I press text of translated l10key (\d+)$/ 
Simulates that the user pressed the text of the l10nkey.	

	Then /^I press button of translated l10key (\d+)$/
Simulates that the user pressed the button with the label text of the l10nkey.

	Then /^I press menu item of translated l10key (\d+)$/
Simulates that the user pressed the menu item with the label text of the l10nkey.

	Then /^I press toggle button of translated l10key (\d+)$/ 
Simulates that the user pressed the toggle button with the label text of the l10nkey.	

	Then /^I wait for the translated "([^\"]*)" l10nkey to appear$/ 
Waits until the text of the translated l10nkey is displayed.

Note: you can assert or press interface elements using [Android's String resources](http://developer.android.com/reference/android/R.string.html) by passing a package in a custom step:

    performAction('press_l10n_element', 'ok', nil, 'android')

Rotation
--------

These steps do nothing if you run them locally. 
If you run the test on [LessPainful](https://www.lesspainful.com) they will actually rotate the physical device.

    Then /^I rotate the device to landscape$/
    Then /^I rotate the device to portrait$/

Manual Steps
------------

These steps are useful for allowing mixed manual and automated tests and to be documented 
and kept together. These steps do nothing when the tests are run automatically but are still
documented in Cucumber output formatters such as the HTML report. This allows a 
manual tester to perform the same test case but with extra manual steps such as manual image verification.

To manually request a manual tester to compare the screen with a reference image

    Then /^I compare the current screen with the reference image "([^\"]*) manually"$/ do |name|

    For example:

    Then I compare the current screen with the reference image "features/ref1.png" manually

To manually perform a custom step    

    Then /^I manually (.*)$/ do |action|

    For example:

    Then I manually check that the list scrolls smoothly


    


