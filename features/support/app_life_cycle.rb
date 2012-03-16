def connect_to_test_server
  log `#{adb_command} forward tcp:#{ENV["TEST_SERVER_PORT"]} tcp:7101`
  
  end_time = Time.now + 60
  begin 
    Timeout.timeout(5) do
      @@client = TCPSocket.open('127.0.0.1',ENV["TEST_SERVER_PORT"])
      @@client.send("Ping!\n",0)
      log "Got '#{@@client.readline.strip}' from testserver"
    end
  rescue Exception => e
    log "Got exception:#{e}. Retrying!"
    sleep(1)
    retry unless Time.now > end_time
  end
end

def disconnect_from_test_server
  log "Closing connection to test"
  @@client.close
end

