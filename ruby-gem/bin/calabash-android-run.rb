def calabash_run(args)
  run_build_if_test_server_does_not_exist


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