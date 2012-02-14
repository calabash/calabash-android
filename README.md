Calabash-Android
================
Calabash-Android lets you run [Cucumber](http://cukes.info) features on you Android device or emulator.

    Feature: Login feature
      Scenario: As a valid user I can log into my app
        Given I am a valid user
        And I enter my username
        And I enter my password
        And I press "Login"
        Then I see "Welcome to coolest app ever"

You can run your Calabash features on multiple devices using [LessPainful.com](httpw://www.lesspainful.com)


To get get started with Calabash-Android you can either download a zip from [here](https://github.com/calabash/calabash-android/zipball/master) or clone it using the following command.

    git clone git://github.com/calabash/calabash-android.git


Configuration
-------------
Change the following value in the `build.properties` file:

* `tested.package_name` the package name from your AndroidManifest.xml.
* `tested.main_activity` the fully qualified name of your main activity.
* `tested.project.apk` path to the APK you want to test.


Writing a test
--------------
The Cucumber features goes in the `features` library and should have the ".feature" extension.

You can start out by looking at `features/test_dummy.feature`. You can extend this feature or make your own using some of the predefined steps that comes with Calabash


Running test
------------
To run your test:

    ant test

To specify on which device the test should run (if you have both an emulator running and a device attached), pass the `adb.device.arg` system property (`-e` or `-d`) like so:

    ant test -Dadb.device.arg=-e


Predefined steps
-----------------

The predefined steps are located in the `features/step_definitions` folder. For now you can look through these files to see which is available.

Comming...

