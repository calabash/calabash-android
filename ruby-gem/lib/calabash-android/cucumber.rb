require 'calabash-android/color_helper'
require 'calabash-android/operations'

World(Calabash::Android::ColorHelper)
World(Calabash::Android::Operations)

AfterConfiguration do
  require 'calabash-android/calabash_steps'
end
