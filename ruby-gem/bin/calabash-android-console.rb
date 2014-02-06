def calabash_console(app_path = nil)
  test_server_path = test_server_path(app_path)

  unless ENV['IRBRC']
    ENV['IRBRC'] = File.join(File.dirname(__FILE__), '..', 'irbrc')
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

  build_test_server_if_needed(app_path)

  system "#{RbConfig.ruby} -S irb"
end