### 0.9.12

Fixes a fatal error in the Android Test Server:

* location mocking: only stop mocking if we started #95

### 0.9.11

The TestServer.apk in 0.9.10 had a fatal bug.

In 0.9.11, the TestServer.apk has been fixed.

This provides preliminary support for running against Android Q
devices.  In Android Q beta 4 we know that the test server would
not start on Android Emulators.  The workaround is to test on
devices.  We believe Q beta 5 will contain a fix for this problem,
but we have not tested this.

### 0.9.10

* add old xtc test app and tests #910
* Update build settings for integration test app #911

### 0.9.9

Users can update to ruby 2.6 and use whatever version bundler they would
like.

* gem: remove luffa dependency; add Version class #907
* Fixed typos in README.md #906
* Fixing META-INF regex #902
* Allow test APKs to be installed #865

#### Test Server 0.9.9

* Feature/fix activity monitor race #84

### 0.9.8

No behavior changes in TestServer.apk.

This release allows Calabash Android to be used with json 2.0
and cucumber 3.0.  This will also allow users to update
their ruby to 2.5.x.

Test Cloud users will need to pin their ruby version to
2.3.x and the json and cucumber versions.  To your Gemfile
add the following:

```
gem "json", "1.8.6"
gem "cucumber", "2.99.0"
```

Test submitted to Test Cloud with json > 1.8.6 and cucumber 3.x
will fail validation.

* Fix dead link on Installation guide #898
* Do not bind Calabash to any versions of json #896
* Support latest Cucumber versions #878

### 0.9.7

* Includes the latest TestServer.apk 0.9.7

### 0.9.6

* Updates the way the gem is released
* Includes the latest TestServer.apk 0.9.6

### 0.9.5

Align the gem and server version to 0.9.5

* Add TestServer.apk and AndroidManifest to git index

### 0.9.4

* Updated screenshot tool which compiled with java 8. #875

### 0.9.3

Added support for Android P (api 28) devices.

* Changed build process for server: added gradle build instead of ant.
* Removed the use of blacklisted api for latest android versions.
* Clean-up android server from external libs.

### 0.9.2

The test server bundled with this gem has the following properties:

```shell
targetSdkLevel="22"
 minSdkVersion="8"
```

* Gem: do not munge the load path #850

#### Server Changes

* Align targetSdkVersion in manifests and build script [#50](https://github.com/calabash/calabash-android-server/pull/50)

### 0.9.1

* Gem: pin cucumber to < 3.0 #847
* Remove usage tracking to comply with EU GDPR 2018 #835
* Add check for installed app before attempting uninstall #833
* Fixed: error when repeated calling funciton send\_tcp() #821
* fix order of default\_timeout initialization and usage in wait\_for method #818
* Removing done from operations to fix #722 #812
* irbrc should capture all errors: otherwise, irb silently eats them. #810
* Fix: permissions not granted on app update #808
* Escape string methods for TextHelpers module #807
* Expand paths when detecting Android SDK #799

#### Server Changes

* Views without id should not generate log messages [#46](https://github.com/calabash/calabash-android-server/pull/46)
* Travis: ensure ant is installed [#45](https://github.com/calabash/calabash-android-server/pull/45)
* Fix exceptions when app is running with different locale than english [#44](https://github.com/calabash/calabash-android-server/pull/44)
* Fix null pointer exception in getViews method [#40](https://github.com/calabash/calabash-android-server/pull/40)
