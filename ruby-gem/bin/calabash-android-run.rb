def calabash_run(app_path = nil)

  old_runner = "android.test.InstrumentationTestRunner"
  new_rummer = "sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner"
  f = "features/support/app_life_cycle_hooks.rb"

  if File.exist?(f) and IO.read(f).include? old_runner
    puts "Calabash has been updated"
    puts "Please do the following to update your project:"
    puts "1) Open #{f} in a text editor"
    puts "2) Replace #{old_runner} with #{new_rummer}"
    exit
  end

  if app_path
    unless File.exist?(test_server_path(app_path))
      puts "No test server found for this combination of app and calabash version. Rebuilding test server."
      calabash_build(app_path)
    end

    test_server_path = test_server_path(app_path)
    env = "PACKAGE_NAME=#{package_name(app_path)} "\
          "TEST_PACKAGE_NAME=#{package_name(test_server_path)} "\
          "APP_PATH=\"#{app_path}\" "\
          "TEST_APP_PATH=\"#{test_server_path}\" "\
          "TEST_SERVER_PORT=34777"
  else
    env = ""
  end

  STDOUT.sync = true
  arguments = ARGV - ["--google-maps-support"]
  cmd = "cucumber #{arguments.join(" ")} #{env} #{"-c" unless is_windows?}"
  puts cmd
  IO.popen(cmd) do |io|
    io.each { |s| print s }
  end

  sleep(1)
end