def calabash_run(app_path = nil)

  old_runner = "android.test.InstrumentationTestRunner"
  new_rummer = "sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner"
  f = "features/support/app_life_cycle_hooks.rb"

  if File.exist?(f) and IO.read(f).include? old_runner
    puts "Calabash has been updated"
    puts "Please do the following to update your project:"
    puts "1) Open #{f} in a text editor"
    puts "2) Replace #{old_runner} with #{new_rummer}"
    exit 1
  end

  if app_path
    build_test_server_if_needed(app_path)

    test_server_path = test_server_path(app_path)
    env = "MAIN_ACTIVITY=#{main_activity(app_path)} "\
          "APP_PATH=\"#{app_path}\" "\
          "TEST_APP_PATH=\"#{test_server_path}\""
  else
    env = ""
  end

  STDOUT.sync = true
  arguments = ARGV - ["--no-build"]
  cmd = "#{RbConfig.ruby} -S cucumber #{arguments.join(" ")} #{env}"
  log cmd
  exit_code = system(cmd)

  sleep(1)
  exit_code
end