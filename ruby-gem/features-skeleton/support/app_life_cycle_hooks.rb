require 'calabash-android/management/adb'
require 'calabash-android/operations'
include Calabash::Android::Operations

AfterConfiguration do |config|
  wake_up unless config.dry_run?
end

Before do |scenario|
  start_test_server_in_background
end

After do |scenario|
  if scenario.failed?
    screenshot_embed
  end
end

After do
    shutdown_test_server
end
