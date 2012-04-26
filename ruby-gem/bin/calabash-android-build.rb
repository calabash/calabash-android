def calabash_build(args)
  @settings = JSON.parse(IO.read(".calabash_settings"))  
    test_server_template_dir = File.join(File.dirname(__FILE__), '..', 'test-server')
  
  Dir.mktmpdir do |workspace_dir|
    
    @test_server_dir = File.join(workspace_dir, 'test-server')
    FileUtils.cp_r(test_server_template_dir, workspace_dir)
    puts `ls #{workspace_dir}`

    Dir.chdir(@test_server_dir) {
      puts `ant clean package -Dtested.project.apk=#{@settings["app_path"]} -Dandroid.api.level=#{@settings["api_level"]} -Dkey.store=#{@settings["keystore_location"]} -Dkey.store.password=#{@settings["keystore_password"]} -Dkey.alias=#{@settings["keystore_alias"]} -Dkey.alias.password=#{@settings["keystore_alias_password"]}`
    }
    puts "ls: " + `ls #{@test_server_dir}/bin`
    #{File.join(File.dirname(__FILE__), 'instrumentation_backend')}`
  end
end
