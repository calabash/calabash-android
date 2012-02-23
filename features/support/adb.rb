def adb_command
  "#{ENV['ANDROID_HOME']}/platform-tools/adb #{ENV["ADB_DEVICE_ARG"]}"
end
