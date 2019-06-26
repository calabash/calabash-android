## Integration Tests

The sources for NativeTestApp were originally found here:

https://github.com/xamarin/test-cloud-test-apps/tree/82cadde95fbbff78ce834642d98a049254bc2ef5/XTCAndroidSampleProject

The app's original name was: XTCAndroidSample.

## Building

```
$ gradlew clean assemble
```

## Testing Calabash Android

```
$ bundle update
$ bundle exec calabash-android run app/build/outputs/apk/debug/NativeTestApp.apk
```

## Submit to App Center

Work in progress.  The Gemfile will need a concrete version for
calabash-android - :path => will not work in Test Cloud.  I recommend a
script that copies the apk, features, and config artifacts into a new
directory: app-center-submit.  The script should generate a new Gemfile
with the correct version of the calabash-android.  To test a local
change in the gem on App Center:

```
# make sure your gem version is up-to-date
$ gem update --system

# uninstall all versions of calabash-android
$ gem uninstall -Vax --force --no-abort-on-dependent

# If you need a new version the TestServer
$ rake build_server

# install calabash-android gem locally
$ rake install

# submit to App Center; the test-cloud gem will pick up the correct
# calabash-android sources.
$ ????
```
