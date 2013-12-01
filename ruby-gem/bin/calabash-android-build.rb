def calabash_build(app)
  apk_fingerprint = fingerprint_from_apk(app)
  log "#{app} was signed with a certificate with fingerprint #{apk_fingerprint}"

  keystores = JavaKeystore.get_keystores
  if keystores.empty?
    puts "No keystores found."
    puts "Please create one or run calabash-android setup to configure calabash-android to use an existing keystore."
    exit 1
  end
  keystore = keystores.find { |k| k.fingerprint == apk_fingerprint}

  unless keystore
    puts "#{app} is not signed with any of the available keystores."
    puts "Tried the following keystores:"
    keystores.each do |k|
      puts k.location
    end
    puts ""
    puts "You can resign the app with #{keystores.first.location} by running:
    calabash-android resign #{app}"

    puts ""
    puts "Notice that resigning an app might break some functionality."
    puts "Getting a copy of the certificate used when the app was build will in general be more reliable."

    exit 1
  end

  test_server_file_name = test_server_path(app)
  FileUtils.mkdir_p File.dirname(test_server_file_name) unless File.exist? File.dirname(test_server_file_name)

  unsigned_test_apk = File.join(File.dirname(__FILE__), '..', 'lib/calabash-android/lib/TestServer.apk')

  android_platform = Env.android_platform_path
  Dir.mktmpdir do |workspace_dir|
    Dir.chdir(workspace_dir) do
      FileUtils.cp(unsigned_test_apk, "TestServer.apk")
      FileUtils.cp(File.join(File.dirname(__FILE__), '..', 'test-server/AndroidManifest.xml'), "AndroidManifest.xml")

      unless system %Q{"#{RbConfig.ruby}" -pi.bak -e "gsub(/#targetPackage#/, '#{package_name(app)}')" AndroidManifest.xml}
        raise "Could not replace package name in manifest"
      end

       unless system %Q{"#{RbConfig.ruby}" -pi.bak -e "gsub(/#testPackage#/, '#{package_name(app)}.test')" AndroidManifest.xml}
        raise "Could not replace test package name in manifest"
      end

      unless system %Q{"#{Env.tools_dir}/aapt" package -M AndroidManifest.xml  -I "#{android_platform}/android.jar" -F dummy.apk}
        raise "Could not create dummy.apk"
      end

      Zip::ZipFile.new("dummy.apk").extract("AndroidManifest.xml","customAndroidManifest.xml")
      Zip::ZipFile.open("TestServer.apk") do |zip_file|
        zip_file.add("AndroidManifest.xml", "customAndroidManifest.xml")
      end
    end
    keystore.sign_apk("#{workspace_dir}/TestServer.apk", test_server_file_name)
    begin

    rescue Exception => e
      log e
      raise "Could not sign test server"
    end
  end
  puts "Done signing the test server. Moved it to #{test_server_file_name}"
end
