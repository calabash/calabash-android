Welcome to Calabash for Android
===========================
[![Build Status](https://secure.travis-ci.org/calabash/calabash-android.png?branch=master)](http://travis-ci.org/calabash/calabash-android)

>After delivering support for the final releases of iOS 11 and Android 8 operating systems, Microsoft will discontinue our contributions to developing Calabash, the open-source mobile app testing tool. We hope that the community will continue to fully adopt and maintain it. As part of our transition on the development of Calabash, we've provided an overview of mobile app UI and end-to-end testing frameworks as a starting point for teams who are looking to re-evaluate their testing strategy. Please see our [Mobile App Testing Frameworks Overview](https://docs.microsoft.com/en-us/appcenter/migration/test-cloud/frameworks) document.

Calabash is an automated testing technology for Android and iOS native and hybrid applications.

Calabash is a free open source project that is looking for a maintainer.

### Documentation
The documentation is split into the following sections:

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

Every time you test a new binary or use an upgraded version of calabash a new test server will be build. The test server is an intrumentation that will run along with your app on the device to execute the test.

Using UIAutomator2
------------------

To benefit from UIAutomator2 test capabilities (like interacting with other apps or system elements), you must start the test server with:
`start_test_server_in_background(with_uiautomator: true)`

Then you will be able to use the command perform_action('*method*', *{parameters}*) which includes the following actions.

| Method | Parameter | Function |
|--------|----------|-----------|
| uiautomator_touch_text | Text | Tap on element with text |
| uiautomator_touch_partial_text | Text | Tap on element containing text |
| pull_notification | none | Open the notification shutter |
| clear_notifications | none | Clear notifications and close the shutter |
| uiautomator_text_dump | none | Dumps text from elements in the UI hierarchy |
| uiautomator_ui_dump | Text | Dumps elements in the UI hierarchy |
| wifi | 'on' or 'off' | Switch device wifi on or off |
| wait_for_idle_sync | none | Wait for idle state |

And the more advanced command `uiautomator_execute` which takes 4 arguments.

`perform_action('uiautomator_execute', 'strategy', 'locator', element index, 'action')`

This will return true, and the result of the query if succeeds or an exception if the strategy, or action are invalid.
If an element is not found, it will return false.

Where **strategy** and **action** can be:

| strategy | description |
|----------|-------------|
| class | class type of ui element |
| res | element resource |
| desc | ui element description |
| descContains | ui element description contains |
| descEndsWith | ui element description ends with |
| descStartWith | ui element description starts with text |
| text | ui element with text |
| textContains | ui element contains text  |
| textEndsWith | ui element ends with text |
| textStartWith | ui element starts with text |
| pkg | package name from application of ui element |

| action | description |
|----------|-------------|
| click | tap on element |
| longClick | long tap on element |
| getText | get text from element |
| getContentDescription | get description |
| getClassName | get class name |
| getResourceName | get resource name |
| getVisibleBounds | get coords for visible bounds of element |
| getVisibleCenter | get coords for visible center of element |
| getApplicationPackage | get package name of application |
| getChildCount | number of children elements |
| clear | clear text from element |
| isCheckable | can the element be checked? |
| isChecked | is the element checked? |
| isClickable | is the element clickable? |
| isEnabled | is the element enabled? |
| isFocusable | can the element be focused? |
| isFocused | is the element focused? |
| isLongClickable | can the element be long clicked? |
| isScrollable | is the element scrollable? |
| isSelected | is the element selected? |

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
