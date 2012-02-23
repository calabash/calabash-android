Before do |scenario|

  system("#{adb_command} shell am instrument -w -e class sh.calaba.instrumentationbackend.InstrumentationBackend #{ENV['TEST_PACKAGE_NAME']}/android.test.InstrumentationTestRunner 1>&2 &")
  sleep 2
  begin
    connect_to_test_server
    $stderr.puts "#{Time.now} - Connection established"
  rescue Exception => e
    $stderr.puts "#{Time.now} - Exception:#{e.backtrace}"
  end
end



After do |scenario| 
  disconnect_from_test_server
end