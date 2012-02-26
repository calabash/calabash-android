def connect_to_test_server
  $stderr.puts `#{adb_command} forward tcp:#{ENV["TEST_SERVER_PORT"]} tcp:7101`
  
  end_time = Time.now + 60
  begin 
    Timeout.timeout(5) do
      @@client = TCPSocket.open('127.0.0.1',ENV["TEST_SERVER_PORT"])
      @@client.send("Ping!\n",0)
      $stderr.puts "#{Time.now} - Got '#" + @@client.readline + "' from testserver"
    end
  rescue Exception => e
    $stderr.puts "#{Time.now} - Got exception:#{e}. Retrying!"
    sleep(1)
    retry unless Time.now > end_time
  end
end

def disconnect_from_test_server
  $stderr.puts "#{Time.now} - Closing connection to test"
  @@client.close
end

