Calabash-Android
================
Calabash-Android lets you run [Cucumber](http://cukes.info) features on your Android device or emulator.

    Feature: Login feature
      Scenario: As a valid user I can log into my app
        Given I am a valid user
        And I enter my username
        And I enter my password
        And I press "Login"
        Then I see "Welcome to coolest app ever"

You can run your Calabash features on multiple devices using [LessPainful.com](https://www.lesspainful.com).

Installation
------------
### Prerequisites
You need to have Ruby installed. Verify your installation by running ruby -v in a terminal - it should print "ruby 1.8.7" (or higher)

You should have the Android SDK installed and the environment variable `ANDROID_HOME` should be pointing to it.

### Installation

Install `calabash-android` by running

- `gem install calabash-android`
- You might have to run `sudo gem install calabash-android` if you do not have the right permissions.

Configuration
-------------
To configure calabash run `calabash-android setup`. You will be asked a series of questions about your app and environment.
Here is an example (without any answers):
  
    When you are through this setup your settings will be saved to .calabash_settings. You can edit this file if you have the need.
    What is the package name of the app? You can find the package name in AndroidManifest.xml

    What is the fully qualified name of the main activity?

    What is the path to the app?

    Which api level do you want to use?
    It looks like you have the following versions installed:
    4, 7, 8, 10, 15

    Do you want to specify a keystore for signing the test app?
    If now we will be using /Users/jml/.android/debug.keystore
    Please answer yes (y) or no (n)
    y
    Please enter keystore location
     
    Please enter the password for the keystore

    Please enter the alias

    Please enter the password for the alias

    Saved your settings to .calabash_settings. You can edit the settings manually or run this setup script again

You can always run `calabash-android setup` again or change the file manually.


**Notice:** Make sure that the app you are trying to test is signed with the key store you just selected in the setup.


Generate a Cucumber skeleton
------------------------
To get started with calabash it might be a good idea to run `calabash-android gen`. It will create a Cucumber skeleton
in the current folder like this:

    features
    |_support
    | |_app_installation_hooks.rb
    | |_app_life_cycle_hooks.rb
    | |_env.rb
    | |_hooks.rb
    |_step_definitions
    | |_calabash_steps.rb
    |_my_first.feature

In this skeleton you find all the predefined steps that comes with calabash. Try to take a look `my_first.feature` and change it to fit your app.

Writing a test
--------------
The Cucumber features goes in the `features` library and should have the ".feature" extension.

You can start out by looking at `features/test_dummy.feature`. You can extend this feature or make your own using some of the [predefined steps](https://github.com/calabash/calabash-android/blob/master/features/step_definitions/canned_steps.md) that comes with Calabash.


Building the test server
------------------------
Calabash will install an instrumentation along with your app on the device to run the test. Because of some app specific information we need to build the test server based on the input you provided during setup. Please note that you need to rebuild
the test server everytime you change the app.

You build the test server like this:

    cucumber-android build

Running test
------------
To run your test:

    cucumber-android run

Predefined steps
-----------------

The predefined steps are located in the `features/step_definitions` folder. A compiled list of predefined steps with comments is available [here](https://github.com/calabash/calabash-android/blob/master/features/step_definitions/canned_steps.md)
