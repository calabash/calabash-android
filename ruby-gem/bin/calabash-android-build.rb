def calabash_build(app)



  keystore = read_keystore_info()

  test_server_template_dir = File.join(File.dirname(__FILE__), '..', 'test-server')
  
  Dir.mktmpdir do |workspace_dir|
    
    @test_server_dir = File.join(workspace_dir, 'test-server')
    FileUtils.cp_r(test_server_template_dir, workspace_dir)
    
    ant_executable = (is_windows? ? "ant.bat" : "ant")
    Dir.chdir(@test_server_dir) {
      args = [
        ant_executable,
        "clean", 
        "package",
        "-Dtested.package_name=#{package_name(app)}",
        "-Dtested.main_activity=#{main_activity(app)}",
        "-Dtested.project.apk=#{app}",
        "-Dandroid.api.level=#{api_level}",
        "-Dkey.store=#{keystore["keystore_location"]}",
        "-Dkey.store.password=#{keystore["keystore_password"]}",
        "-Dkey.alias=#{keystore["keystore_alias"]}",
        "-Dkey.alias.password=#{keystore["keystore_alias_password"]}",
      ]
      STDOUT.sync = true
      IO.popen(args.join(" ")) do |io|
        io.each { |s| print s }
      end
      if $?.exitstatus != 0
        puts "Could not build the test server. Please see the output above."
        exit $?.exitstatus
      end
    }

    test_apk = File.join(@test_server_dir, "bin", "Test.apk")
    test_server_file_name = "#{checksum(app)}.apk"
    FileUtils.cp(test_apk, File.join(@support_dir, test_server_file_name))
    puts "Done building the test server. Moved it to features/support/#{test_server_file_name}"
  end
end

def read_keystore_info
  if File.exist? ".calabash_settings"
    JSON.parse(IO.read(".calabash_settings"))
  else
    {
    "keystore_location" => "#{ENV["HOME"]}/.android/debug.keystore",
    "keystore_password" => "android",
    "keystore_alias" => "androiddebugkey",
    "keystore_alias_password" => "android"
    }
  end
end

def is_windows?
  ENV["OS"] == "Windows_NT"
end