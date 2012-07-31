Calabash-Android
================
Calabash-Android lets you run [Cucumber](http://cukes.info) features on your Android device or emulator. A Cucumber feature could look something like this:

    Feature: Login feature
      Scenario: As a valid user I can log into my app
        Given I am a valid user
        And I enter my username
        And I enter my password
        And I press "Login"
        Then I see "Welcome to coolest app ever"

You can run your Calabash features on multiple devices using [LessPainful.com](https://www.lesspainful.com).

If you have any questions on Calabash-Android, please use the Google group

[http://groups.google.com/group/calabash-android](http://groups.google.com/group/calabash-android)

Installation
------------
### Prerequisites
You need to have Ruby installed. Verify your installation by running ruby -v in a terminal - it should print "ruby 1.8.7" (or higher).

If you are on Windows you can get Ruby from [RubyInstaller.org](http://rubyinstaller.org/)

You should have the Android SDK installed and the environment variable `ANDROID_HOME` should be pointing to it.

You also need to have Ant installed and added to your path

### Installation

Install `calabash-android` by running

- `gem install calabash-android`
- You might have to run `sudo gem install calabash-android` if you do not have the right permissions.


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

You can start out by looking at `features/test_dummy.feature`. You can extend this feature or make your own using some of the [predefined steps](https://github.com/calabash/calabash-android/blob/master/ruby-gem/lib/calabash-android/canned_steps.md) that comes with Calabash.

Running test
------------
To run your test:

    calabash-android run <apk>

Calabash-android will install an intrumentation next along with your app when executing the app. We call this instrumentation for "test server". The "test server" has special permission that allows it to interact very closely with your app during test.
Everytime you test a new binary or use an upgraded version of calabash a new test server will be build.
The test server is an intrumentation that will run along with your app on the device to execute the test.

### Screenshot location
Screenshots are stored in the `results` folder by default. The location can be changed by setting the `SCREENSHOT_PATH_PREFIX` environment variable.

    SCREENSHOT_PATH_PREFIX=/tmp/foo calabash-android run

Predefined steps
-----------------

The predefined steps are located in the `features/step_definitions` folder. A compiled list of predefined steps with comments is available [here](https://github.com/calabash/calabash-android/blob/master/ruby-gem/lib/calabash-android/canned_steps.md)
