def calabash_build(app)

  config = get_config()

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
        "-Dtested.package_name=\"#{config['package_name']}\"",
        "-Dtested.main_activity=#{config['activity_name']}",
        "-Dtested.project.apk=\"#{config['app_path']}\"",
        "-Dandroid.api.level=#{config['api_level']}",
        "-Dkey.store=\"#{File.expand_path config['keystore_location']}\"",
        "-Dkey.store.password=#{config['keystore_password']}",
        "-Dkey.alias=#{config['keystore_alias']}",
        "-Dkey.alias.password=#{config['keystore_alias_password']}"
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

    FileUtils.mkdir_p "test_servers" unless File.exist? "test_servers"

    test_apk = File.join(@test_server_dir, "bin", "Test.apk")
    test_server_file_name = test_server_path(app)
    FileUtils.cp(test_apk, test_server_file_name)
    puts "Done building the test server. Moved it to #{test_server_file_name}"
  end
end
