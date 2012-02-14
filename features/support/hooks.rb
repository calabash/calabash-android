# -*- coding: utf-8 -*-
require 'rubygems'
require 'json'
require 'socket'
require 'timeout'


Step_line = 0
def performAction(action, *arguments)
  puts "#{Time.now} - Action: #{action} - Params: #{arguments.join(', ')}"

  action = {"command" => action, "arguments" => arguments}

  Timeout.timeout(300) do
    begin
      @@client.send(action.to_json + "\n", 0) #force_encoding('UTF-8') seems to be missing from JRuby
      result = @@client.readline
    rescue Exception => e
      puts "#{Time.now} - error communicating with test server: #{e}"
      raise e
    end
    puts "#{Time.now} - Result:'" + result + "'"
    raise "Empty result from TestServer" if result.chomp.empty?
    result = JSON.parse(result)
    raise result["message"].to_s unless result["success"]
    #Move along
  end
rescue Timeout::Error
  raise Exception, "#{Time.now} - Step timed out"
end

Before do |scenario|

  system("#{adb_command} shell am instrument -w -e class sh.calaba.instrumentationbackend.InstrumentationBackend #{ENV['TEST_PACKAGE_NAME']}/android.test.InstrumentationTestRunner &")
  sleep 2
  begin
    establish_connection_to_test_server
    puts "#{Time.now} - Connection established"
  rescue Exception => e
    puts "#{Time.now} - Exception:#{e.backtrace}"
  end
end

After do |scenario| 
  close_connection_to_test_server
end

def establish_connection_to_test_server
  create_port_forward_to_test_server
  end_time = Time.now + 60
  begin 
    Timeout.timeout(5) do
      @@client = TCPSocket.open('127.0.0.1',7101)
      @@client.send("Ping!\n",0)
      puts "#{Time.now} - Got '#" + @@client.readline + "' from testserver"
    end
  rescue Exception => e
    puts "#{Time.now} - Got exception:#{e}. Retrying!"
    sleep(1)
    retry unless Time.now > end_time
  end
end

def close_connection_to_test_server
  $stdout.puts "#{Time.now} - Closing connection to test"
  @@client.close
end

def create_port_forward_to_test_server
  puts `#{adb_command} forward tcp:7101 tcp:7101`
end

def adb_command
  "#{ENV['ANDROID_HOME']}/platform-tools/adb #{ENV["ADB_DEVICE_ARG"]}"
end
