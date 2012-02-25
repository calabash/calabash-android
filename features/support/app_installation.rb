def install_app
	system("#{adb_command} install #{ENV["TEST_APP_PATH"]}")
	system("#{adb_command} install #{ENV["APP_PATH"]}")
end

def uninstall_app
	system("#{adb_command} uninstall #{ENV["TEST_PACKAGE_NAME"]}")
	system("#{adb_command} uninstall #{ENV["PACKAGE_NAME"]}")
end