def calabash_run(args)
  run_build_if_test_server_does_not_exist

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

  settings = JSON.parse(IO.read(".calabash_settings"))
  
  env ="PACKAGE_NAME=#{settings["package_name"]} "\
        "TEST_PACKAGE_NAME=#{settings["package_name"]}.test "\
        "APP_PATH=#{settings["app_path"]} "\
        "TEST_APP_PATH=features/support/Test.apk "\
        "TEST_SERVER_PORT=34777"
      
  STDOUT.sync = true
  cmd = "cucumber -c #{ARGV.join(" ")} #{env}"
  puts cmd
  IO.popen(cmd) do |io|
    io.each { |s| print s }
  end

  sleep(1)
end