$_calabash_installed_app = false

def go_back_to_see(query, tries)
  tries.times do |i|
    if i == tries-1
      raise "Could not find '#{query}' by going back"
    end

    if element_exists(query)
      break
    end

    sleep 0.1
    press_back_button
  end
end

Before do
  unless $_calabash_installed_app
    reinstall_apps
    $_calabash_installed_app = true
  end

  start_test_server_in_background
end
