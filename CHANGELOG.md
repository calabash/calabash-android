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
