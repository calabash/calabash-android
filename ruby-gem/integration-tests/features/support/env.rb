require 'calabash-android/color_helper'
require 'calabash-android/operations'

World(Calabash::Android::ColorHelper)
World(Calabash::Android::Operations)

ENV['TEST_APP_PATH'] = test_server_path(ENV['APP_PATH'])