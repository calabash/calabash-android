def log(message)
  $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}"
end