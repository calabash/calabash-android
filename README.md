Welcome to Calabash for Android
===========================

Calabash is an automated testing technology for Android and iOS native and hybrid applications.
This repository contains support for Android, for iOS, see [Calabash Landing Page](http://calaba.sh/).

Calabash is a free open source project, developed and maintained by [Xamarin](http://xamarin.com).

While Calabash is completely free, Xamarin provides a number of commercial services centered around Calabash and quality assurance for mobile, namely Xamarin Test Cloud consisting of hosted test-execution environments which let you execute Calabash tests on a large number of Android and iOS devices.

Please see [xamarin.com/test-cloud](http://xamarin.com/test-cloud).

If you have any questions on Calabash-Android, please use the Google group [http://groups.google.com/group/calabash-android](http://groups.google.com/group/calabash-android)

### Documentation
The documention is split into the following sections:
* [Installation](documentation/installation.md)
* [Ruby API](documentation/ruby_api.md)


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

You can start out by looking at `features/my_first.feature`. You can extend this feature or make your own using some of the [predefined steps](https://github.com/calabash/calabash-android/blob/master/ruby-gem/lib/calabash-android/canned_steps.md) that comes with Calabash.

Running test
------------
To run your test:

    calabash-android run <apk>

Calabash-android will install an instrumentation along with your app when executing the app. We call this instrumentation for "test server". The "test server" has special permission that allows it to interact very closely with your app during test.
Everytime you test a new binary or use an upgraded version of calabash a new test server will be build.
The test server is an intrumentation that will run along with your app on the device to execute the test.

### Screenshot location
Screenshots are placed in the current working directory by default. The location can be changed by setting the `SCREENSHOT_PATH` environment variable.

    SCREENSHOT_PATH=/tmp/foo/ calabash-android run

would cause the first screenshot to appear at `/tmp/foo/screenshot_0.png`.

Predefined steps
-----------------

The predefined steps are located in the `features/step_definitions` folder. A compiled list of predefined steps with comments is available [here](https://github.com/calabash/calabash-android/blob/master/ruby-gem/lib/calabash-android/canned_steps.md)

Troubleshooting
---------------

### Problems clicking on buttons and text

If it seems that buttons/text aren't being clicked properly, you need to add the following xml to your AndroidManifest.xml:

```
<uses-sdk android:targetSdkVersion="SDK_VERSION" />
```

Where SDK_VERSION is the version of the Android SDK you are using. Version numbers can be found [here](http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)

For example, Android 4.0 uses version 14, Android 4.0.3 uses version 15 and Android 4.1 uses version 16.
