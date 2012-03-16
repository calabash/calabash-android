def install_app(app_path)

  cmd = "#{adb_command} install #{app_path}"
  $stdout.puts "Installing: #{app_path}"
	result = `#{cmd}`
  if result.include? "Success"
    $stdout.puts "Success"
  else
    $stdout.puts "#Failure"
    $stdout.puts "'#{cmd}' said:"
    $stdout.puts result.strip
    raise "Could not install app #{app_path}"
  end
end

def uninstall_apps
  $stdout.puts "Uninstalling: #{ENV["TEST_PACKAGE_NAME"]}"
	system("#{adb_command} uninstall #{ENV["TEST_PACKAGE_NAME"]}")
  $stdout.puts "Uninstalling: #{ENV["PACKAGE_NAME"]}"
  system("#{adb_command} uninstall #{ENV["PACKAGE_NAME"]}")
end