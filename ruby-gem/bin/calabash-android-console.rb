def calabash_console(app_path = nil)
  test_server_path = test_server_path(app_path)


  path = ENV['CALABASH_IRBRC']
  unless path
    if File.exist?('.irbrc')
      path = File.expand_path('.irbrc')
    end
  end

  unless path
    path = File.expand_path(File.join(File.dirname(__FILE__), '..', 'irbrc'))
  end

  ENV['IRBRC'] = path

  unless ENV['APP_PATH']
    ENV['APP_PATH'] = app_path
  end

  unless ENV['TEST_APP_PATH']
    ENV['TEST_APP_PATH'] = test_server_path
  end

  build_test_server_if_needed(app_path)

  puts 'Starting calabash-android console...'
  puts "Loading #{ENV['IRBRC']}"
  puts 'Running irb...'
  exec('irb')

end