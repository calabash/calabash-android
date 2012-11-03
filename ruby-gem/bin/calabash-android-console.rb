def calabash_console(app_path = nil)
  test_server_path = test_server_path(app_path)

  unless ENV["TEST_SERVER_PORT"]
    ENV["TEST_SERVER_PORT"] = "34777"
  end

  unless ENV["IRBRC"]
    ENV["IRBRC"] = File.join(File.dirname(__FILE__), '..', 'irbrc')
  end

  unless ENV["PACKAGE_NAME"]
    ENV["PACKAGE_NAME"] = package_name(app_path)  
  end

  unless ENV["MAIN_ACTIVITY"]
    ENV["MAIN_ACTIVITY"] = main_activity(app_path)  
  end

  system "irb"
end