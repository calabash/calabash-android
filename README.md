# Calabash Android

[![Build Status](https://travis-ci.org/calabash/calabash-android.svg?branch=master)](https://travis-ci.org/calabash/calabash-android)

[http://calaba.sh/](http://calaba.sh/)

Calabash is an automated testing technology for Android and iOS native and hybrid applications.

Calabash is a free-to-use open source project that is developed and maintained by [Xamarin](http://xamarin.com).

While Calabash is completely free, Xamarin provides a number of commercial services centered around Calabash and quality assurance for mobile, namely Xamarin Test Cloud consisting of hosted test-execution environments which let you execute Calabash tests on a large number of Android and iOS devices.  For more information about the Xamarin Test Cloud visit [http://xamarin.com/test-cloud](http://xamarin.com/test-cloud).

If you have any questions, please use the [calabash-android Google group](http://groups.google.com/group/calabash-android)

### Documentation

The documention is split into the following sections:

* [Installation](documentation/installation.md)
* [Ruby API](documentation/ruby_api.md)
* [Wiki (which includes information on constructing uiquery strings )](https://github.com/calabash/calabash-android/wiki/05-Query-Syntax)

calabash-android requires ruby >= 2.0 (latest stable release is preferred).

#### Ruby on MacOS

On MacOS, we recommend using a managed Ruby like
[rbenv](https://github.com/sstephenson/rbenv) or [rvm](https://rvm.io/)).  If
you are just getting started or don't want to commit to a managed Ruby, you
should install and use the [Calabash Sandbox](https://github.com/calabash/install).

```
# Installs the Calabash Sandbox
$ curl -sSL https://raw.githubusercontent.com/calabash/install/master/install-osx.sh | bash
```

Please do **not** install gems with `sudo`

For more information about ruby on MacOS, see these Wiki pages:

* [Ruby on MacOS](https://github.com/calabash/calabash-ios/wiki/Ruby-on-MacOS)
* [Best Practice: Never install gems with sudo](https://github.com/calabash/calabash-ios/wiki/Best-Practice%3A--Never-install-gems-with-sudo)

#### Upgrading to calabash-android 0.5

calabash-android 0.5 introduced new features and removed a lot of actions. If your test project is dependent on some of the removed actions, you will have to reimplement the actions using ruby wrappers, queries and gestures. [This document describes all changes needed to migrate to calabash-android 0.5](migrating_to_calabash_0.5.md)

#### Generate a Cucumber skeleton

To get started with Calabash it might be a good idea to run `calabash-android gen`. It will create a Cucumber skeleton
in the current folder like this:

    features
    ├── my_first.feature
    ├── step_definitions
    │   └── calabash_steps.rb
    └── support
        ├── app_installation_hooks.rb
        ├── app_life_cycle_hooks.rb
        ├── env.rb
        └── hooks.rb

In this skeleton you find all the predefined steps that come with Calabash. Try to take a look `my_first.feature` and change it to fit your app.

#### Writing a test

The Cucumber features goes in the `features` library and should have the ".feature" extension.

You can start out by looking at `features/my_first.feature`. You can extend this feature or make your own using some of the [predefined steps](https://github.com/calabash/calabash-android/blob/master/ruby-gem/lib/calabash-android/canned_steps.md) that come with Calabash.

#### Running test

To run your test:

    calabash-android run <apk>

calabash-android will install an instrumentation along with your app when executing the app. We call this instrumentation for "test server". The "test server" has special permission that allows it to interact very closely with your app during test.
Everytime you test a new binary or use an upgraded version of Calabash a new test server will be build.
The test server is an intrumentation that will run along with your app on the device to execute the test.

#### Screenshot location

Screenshots are placed in the current working directory by default. The location can be changed by setting the `SCREENSHOT_PATH` environment variable.

    SCREENSHOT_PATH=/tmp/foo/ calabash-android run

would cause the first screenshot to appear at `/tmp/foo/screenshot_0.png`.

#### Predefined steps

The predefined steps are located in the `features/step_definitions` folder. A compiled list of predefined steps with comments is available [here](https://github.com/calabash/calabash-android/blob/master/ruby-gem/lib/calabash-android/canned_steps.md)

### Troubleshooting

#### Problems starting the tests

If your app instantaneously crashes right after being started, bear in mind that it needs the permission `android.permission.INTERNET` in order for Calabash tests to run ( [Issue #278](https://github.com/calabash/calabash-android/issues/278) ). Add the following xml to your AndroidManifest.xml if you don't have it already:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

#### Problems clicking on buttons and text

If it seems that buttons/text aren't being clicked properly, you need to add the following xml to your AndroidManifest.xml:

```xml
<uses-sdk android:targetSdkVersion="SDK_VERSION" />
```

Where SDK_VERSION is the version of the Android SDK you are using. Version numbers can be found [here](http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)

For example, Android 4.0 uses version 14, Android 4.0.3 uses version 15 and Android 4.1 uses version 16.

## License

    Copyright (c) LessPainful APS. All rights reserved.
    The use and distribution terms for this software are covered by the
    Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
    which can be found in the file epl-v10.html at the root of this distribution.
    By using this software in any fashion, you are agreeing to be bound by
    the terms of this license. You must not remove this notice, or any other,
    from this software.
