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


To get started with Calabash-Android you can either download a zip from the [Downloads sections](https://github.com/calabash/calabash-android/zipball/master) or clone it using the following command

    git clone git://github.com/calabash/calabash-android.git


Configuration
-------------
We assume that you already have the Android SDK installed and that `$ANDROID_HOME` is pointing to it.

Change the following value in the `build.properties` file:

* `tested.package_name` the package name from your AndroidManifest.xml.
* `tested.main_activity` the fully qualified name of your main activity.
* `tested.project.apk` path to the APK you want to test.

**Notice:** Make sure that the app you are trying to test is signed with the `key.store` certificate.


Writing a test
--------------
The Cucumber features goes in the `features` library and should have the ".feature" extension.

You can start out by looking at `features/test_dummy.feature`. You can extend this feature or make your own using some of the [predefined steps](features/step_definitions/canned_steps.md) that comes with Calabash.


Running test
------------
To run your test:

    ant test

To specify on which device the test should run (if you have both an emulator running and a device attached), pass the `adb.device.arg` system property (`-e` or `-d`) like so:

    ant test -Dadb.device.arg=-e


Predefined steps
-----------------

The predefined steps are located in the `features/step_definitions` folder. A compiled list of predefined steps with comments is available [here](blob/master/features/step_definitions/canned_steps.md)