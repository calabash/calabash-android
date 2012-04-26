def calabash_run(args)
  unless File.exists?(".calabash_settings")
    puts "Could not find .calabash_settings"
    puts "Please run: calabash-android setup"
    exit 1
  end
  settings = JSON.parse(IO.read(".calabash_settings"))
  
  IO.popen("PACKAGE_NAME=#{settings["package_name"]} TEST_PACKAGE_NAME=#{settings["package_name"]}.test APP_PATH=#{settings["app_path"]} cucumber #{ARGV}") do |io|
    io.each { |s| print s }
  end
end