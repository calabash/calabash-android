Before do |scenario|

  return if scenario.failed? #No need to start the server is anything before this has failed.
  cmd = "#{adb_command} shell am instrument -w -e class sh.calaba.instrumentationbackend.InstrumentationBackend #{ENV['TEST_PACKAGE_NAME']}/android.test.InstrumentationTestRunner 1>&2 &"
  log "Starting test server using:"
  log cmd
  `#{cmd}`
  sleep 2
  begin
    connect_to_test_server
    log "Connection established"
  rescue Exception => e
    log "Exception:#{e.backtrace}"
  end
end



After do |scenario| 
  disconnect_from_test_server
end