def install_app(app_path)

  cmd = "#{adb_command} install #{app_path}"
  log "Installing: #{app_path}"
	result = `#{cmd}`
  if result.include? "Success"
    log "Success"
  else
    log "#Failure"
    log "'#{cmd}' said:"
    log result.strip
    raise "Could not install app #{app_path}: #{result.strip}"
  end
end

def uninstall_apps
  log "Uninstalling: #{ENV["TEST_PACKAGE_NAME"]}"
  log `#{adb_command} uninstall #{ENV["TEST_PACKAGE_NAME"]}`
  log "Uninstalling: #{ENV["PACKAGE_NAME"]}"
  log `#{adb_command} uninstall #{ENV["PACKAGE_NAME"]}`
end