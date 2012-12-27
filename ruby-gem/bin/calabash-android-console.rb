def calabash_console(app_path = nil)
  test_server_path = test_server_path(app_path)

  unless ENV["TEST_SERVER_PORT"]
    ENV["TEST_SERVER_PORT"] = "34777"
  end

  ENV["IRBRC"] = File.join(File.dirname(__FILE__), '..', 'irbrc')

  unless ENV["PACKAGE_NAME"]
    ENV["PACKAGE_NAME"] = package_name(app_path)
  end

  unless ENV["MAIN_ACTIVITY"]
    ENV["MAIN_ACTIVITY"] = main_activity(app_path)
  end

  unless ENV["APP_PATH"]
    ENV["APP_PATH"] = app_path
  end

  unless ENV["TEST_APP_PATH"]
    ENV["TEST_APP_PATH"] = test_server_path
  end

  system "irb"
end