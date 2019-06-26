## Integration Tests

The sources for NativeTestApp were originally found here:

https://github.com/xamarin/test-cloud-test-apps/tree/82cadde95fbbff78ce834642d98a049254bc2ef5/XTCAndroidSampleProject

The app's original name was: XTCAndroidSample.

The package name is still: com.xamarin.xtcandroidsample.

## Building

```
$ gradlew clean assemble
```

## Testing Calabash Android

Work in progress.

Some test are failing.

```
$ bundle update
$ bundle exec calabash-android run app/build/outputs/apk/debug/NativeTestApp.apk

cucumber features/webview.feature:4 # Scenario: Querying for elements inside a non-cross domain iframe
```

There are more tests in `ruby-gem/api` against the NativeTestApp, but
the binary has been committed to git - find it in
ruby-gem/api/features/support/apps/general.apk.  That is an older
version of NativeTestApp.  The ruby-gem/api is probably a more
comprehensive set of tests against the calabash-android API that the
features in this directory.  Someone should copy the features, steps,
and hooks, from ruby-gem/api to here.  It might be trick to merge the
feature/support/hooks.rb.

## Submit to App Center

Work in progress.

The Gemfile will need a concrete version for calabash-android - :path => will
not work in Test Cloud.  I recommend a script that copies the apk, features,
and config artifacts into a new directory: app-center-submit.  The script
should generate a new Gemfile with the correct version of calabash-android.

Further, the Gemfile used must use json 1.8.7, bundler 1.17.3, and
cucumber < 3.0.


To test a local change in the gem on App Center:

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
