def calabash_build(args)
  run_setup_if_settings_does_not_exist

  @settings = expand_paths JSON.parse(IO.read(".calabash_settings"))  
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
        "-Dtested.package_name=#{@settings["package_name"]}",
        "-Dtested.main_activity=#{@settings["activity_name"]}",
        "-Dtested.project.apk=#{@settings["app_path"]}",
        "-Dandroid.api.level=#{@settings["api_level"]}",
        "-Dkey.store=#{@settings["keystore_location"]}",
        "-Dkey.store.password=#{@settings["keystore_password"]}",
        "-Dkey.alias=#{@settings["keystore_alias"]}",
        "-Dkey.alias.password=#{@settings["keystore_alias_password"]}",
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
    FileUtils.cp(test_apk, File.join(@support_dir, "Test.apk"))
    puts "Done building the test server. Moved it to features/support/Test.apk"
  end
end

def expand_paths(settings)
  settings["app_path"] = File.expand_path settings["app_path"]
  settings["keystore_location"] = File.expand_path settings["keystore_location"]
  settings
end

def is_windows?
  ENV["OS"] == "Windows_NT"
end
