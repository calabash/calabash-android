def ensure_app_installed
  unless $app_installed
    reinstall_apps
  end

  $app_installed = true
end