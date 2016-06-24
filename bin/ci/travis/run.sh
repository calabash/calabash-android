#!/usr/bin/env bash

cd ruby-gem

touch lib/calabash-android/lib/TestServer.apk
touch lib/calabash-android/lib/AndroidManifest.xml

bundle update

gem build calabash-android.gemspec

bundle exec rake unit

