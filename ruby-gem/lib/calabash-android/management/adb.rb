def adb_command
  if is_windows?
    %Q("#{ENV["ANDROID_HOME"]}\\platform-tools\\adb.exe" #{ENV["ADB_DEVICE_ARG"]})
  else
    %Q(#{ENV["ANDROID_HOME"]}/platform-tools/adb #{ENV["ADB_DEVICE_ARG"]})
  end
end

def is_windows?
   ENV["OS"] == "Windows_NT"
end