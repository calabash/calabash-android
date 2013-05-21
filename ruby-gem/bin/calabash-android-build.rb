def calabash_build(app)
  keystore = read_keystore_info()
  if fingerprint_from_keystore != fingerprint_from_apk(app)
    puts "#{app} is not signed with the configured keystore '#{keystore["keystore_location"]}' Aborting!"
    exit 1
  end



  test_server_file_name = test_server_path(app)
  FileUtils.mkdir_p File.dirname(test_server_file_name) unless File.exist? File.dirname(test_server_file_name)

  unsigned_test_apk = File.join(File.dirname(__FILE__), '..', 'lib/calabash-android/lib/TestServer.apk')
  platforms = Dir["#{android_home_path}/platforms/android-*"].sort_by! { |item| '%08s' % item.split('-').last }
  android_platform = platforms.last
  raise "No Android SDK found in #{ENV["ANDROID_HOME"].gsub("\\", "/")}/platforms/" unless android_platform
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

      unless system %Q{"#{tools_dir}/aapt" package -M AndroidManifest.xml  -I "#{android_platform}/android.jar" -F dummy.apk}
        raise "Could not create dummy.apk"
      end

      Zip::ZipFile.new("dummy.apk").extract("AndroidManifest.xml","customAndroidManifest.xml")
      Zip::ZipFile.open("TestServer.apk") do |zip_file|
        zip_file.add("AndroidManifest.xml", "customAndroidManifest.xml")
      end
    end
    sign_apk("#{workspace_dir}/TestServer.apk", test_server_file_name)
    begin

    rescue Exception => e
      log e
      raise "Could not sign test server"
    end
  end
  puts "Done signing the test server. Moved it to #{test_server_file_name}"
end
