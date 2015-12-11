#!/usr/bin/env bash

cd ruby-gem
bundle update

touch lib/calabash-android/lib/TestServer.apk
gem build calabash-android.gemspec

bundle exec rake unit

