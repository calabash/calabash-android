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

def start_test_server_in_background
  cmd = "#{adb_command} shell am instrument -w -e class sh.calaba.instrumentationbackend.InstrumentationBackend #{ENV['TEST_PACKAGE_NAME']}/sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner"
  log "Starting test server using:"
  log cmd
  if is_windows?
    system(%Q(start /MIN cmd /C #{cmd}))
  else
    `#{cmd} 1>&2 &`
  end
end