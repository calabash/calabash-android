require 'calabash-android/management/adb'

Before do |scenario|
  start_test_server_in_background
end

After do
    shutdown_test_server
end
