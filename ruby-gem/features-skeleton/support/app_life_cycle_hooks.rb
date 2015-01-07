require 'calabash-android/management/adb'
require 'calabash-android/operations'

Before do |scenario|
  calabash_start_app
end

After do |scenario|
  if scenario.failed?
    screenshot_embed
  end
  calabash_stop_app
end
