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
You need to have `cucumber`, Ruby 1.9.2+ installed, and a recent RubyGems installation.

You can install `cucumber` like this:

    gem install cucumber

The `json` gem is also required

    gem install json

<<<<<<< HEAD
You should have the Android SDK installed and `$ANDROID_HOME` should be pointing to it.
=======
You should have the Android SDK installed and the environment variable `ANDROID_HOME` should be pointing to it.

Finally, you need the `git` tool to obtain the source.

* [Installing git on windows](http://msysgit.github.com/)

* [Installing git on Linux](http://help.github.com/linux-set-up-git/) (you only need the first step: First: Download and Install Git).

* [Installing git on MacOS](http://git-scm.com/)
>>>>>>> 6f33153e3cd6d76b56fb5c114f6c2e090a85ba15

### Installation

You can obtain the newest version of Calabash-Android by cloning it using the following command

    git clone git://github.com/calabash/calabash-android.git

Change directory to into the cloned directory: calabash-android. Fetch `calabash-js` which is a shared project with [calabash-ios](https://github.com/calabash/calabash-ios):

    git submodule init
    git submodule update


Configuration
-------------
Change the following value in the `build.properties` file:

* `tested.package_name` the package name from your AndroidManifest.xml.
* `tested.main_activity` the fully qualified name of your main activity.
* `tested.project.apk` path to the APK you want to test.
* Optionally, set the `TEST_ARTIFACTS_DIR` to point to where you want test artifacts such as screenshots to be put. If this is not set, test artifacts are put in a directory called "./results". The TEST_ARTIFACTS_DIR directory is DELETED on every test run and replaced with the new test artifacts.

You might have to change `android.api.level` if you do not have api level 8 installed.

**Notice:** Make sure that the app you are trying to test is signed with the `key.store` certificate.


Writing a test
--------------
The Cucumber features goes in the `features` library and should have the ".feature" extension.

You can start out by looking at `features/test_dummy.feature`. You can extend this feature or make your own using some of the [predefined steps](https://github.com/calabash/calabash-android/blob/master/features/step_definitions/canned_steps.md) that comes with Calabash.


Running test
------------
To run your test:

    ant clean test

To specify on which device the test should run (if you have both an emulator running and a device attached), pass the `adb.device.arg` system property (`-e` or `-d`) like so:

    ant clean test -Dadb.device.arg=-e


Predefined steps
-----------------

The predefined steps are located in the `features/step_definitions` folder. A compiled list of predefined steps with comments is available [here](https://github.com/calabash/calabash-android/blob/master/features/step_definitions/canned_steps.md)
