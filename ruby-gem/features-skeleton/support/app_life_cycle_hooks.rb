require 'calabash-android/management/adb'

Before do |scenario|
  # John Gallagher provided the "scenario_is_outline" fix: https://groups.google.com/forum/?fromgroups#!topic/calabash-ios/ICA4f24eSsY
  # ...there may be a better way of doing this...
  @scenario_is_outline = (scenario.class == Cucumber::Ast::OutlineTable::ExampleRow) 
  if @scenario_is_outline 
    scenario = scenario.scenario_outline
    # Still need to call connect_to_test_server...
  elsif scenario.failed?
    return #No need to start the server is anything before this has failed.
  end

  cmd = "#{adb_command} shell am instrument -w -e class sh.calaba.instrumentationbackend.InstrumentationBackend #{ENV['TEST_PACKAGE_NAME']}/sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner"
  log "Starting test server using:"
  log cmd
  if is_windows?
    system(%Q(start /MIN cmd /C #{cmd}))
  else
    `#{cmd} 1>&2 &`
  end
  
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