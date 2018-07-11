Welcome to Calabash for Android
===========================
[![Build Status](https://secure.travis-ci.org/calabash/calabash-android.png?branch=master)](http://travis-ci.org/calabash/calabash-android)

>After delivering support for the final releases of iOS 11 and Android 8 operating systems, Microsoft will discontinue our contributions to developing Calabash, the open-source mobile app testing tool. We hope that the community will continue to fully adopt and maintain it. As part of our transition on the development of Calabash, we've provided an overview of mobile app UI and end-to-end testing frameworks as a starting point for teams who are looking to re-evaluate their testing strategy. Please see our [Mobile App Testing Frameworks Overview](https://docs.microsoft.com/en-us/appcenter/migration/test-cloud/frameworks) document.

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
* [Wiki (which includes information on constructing uiquery strings )](https://github.com/calabash/calabash-android/wiki/05-Query-Syntax)

Calabash Android requires ruby >= 2.0 (latest stable release is preferred).

#### Ruby on MacOS

On MacOS, we recommend using a managed Ruby like
[rbenv](https://github.com/sstephenson/rbenv) or [rvm](https://rvm.io/)).

Please do **not** install gems with `sudo`

For more information about ruby on MacOS, see these Wiki pages:

* [Ruby on MacOS](https://github.com/calabash/calabash-ios/wiki/Ruby-on-MacOS)
* [Best Practice: Never install gems with sudo](https://github.com/calabash/calabash-ios/wiki/Best-Practice%3A--Never-install-gems-with-sudo)


#### Upgrading to Calabash-android 0.5

Calabash-android 0.5 introduced new features and removed a lot of actions. If your test project is dependent on some of the removed actions, you will have to reimplement the actions using ruby wrappers, queries and gestures. [This document describes all changes needed to migrate to calabash-android 0.5](migrating_to_calabash_0.5.md)

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

### Problems starting the tests

If your app instantaneously crashes right after being started, bear in mind that it needs the permission `android.permission.INTERNET` in order for calabash tests to run ( [Issue #278](https://github.com/calabash/calabash-android/issues/278) ). Add the following xml to your AndroidManifest.xml if you don't have it already:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### Problems clicking on buttons and text

If it seems that buttons/text aren't being clicked properly, you need to add the following xml to your AndroidManifest.xml:

```xml
<uses-sdk android:targetSdkVersion="SDK_VERSION" />
```

Where SDK_VERSION is the version of the Android SDK you are using. Version numbers can be found [here](http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)

For example, Android 4.0 uses version 14, Android 4.0.3 uses version 15 and Android 4.1 uses version 16.
