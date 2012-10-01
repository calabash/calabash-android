require 'zip/zip'
def calabash_build(app)
  keystore = read_keystore_info()

  test_server_file_name = test_server_path(app)
  unsigned_test_apk = File.join(File.dirname(__FILE__), '..', 'lib/calabash-android/lib/TestServer.apk')
  android_platform = Dir["#{ENV["ANDROID_HOME"].gsub("\\", "/")}/platforms/android-*"].last
  Dir.mktmpdir do |workspace_dir|
    Dir.chdir(workspace_dir) do
      FileUtils.cp(unsigned_test_apk, "TestServer.apk")
      FileUtils.cp(File.join(File.dirname(__FILE__), '..', 'test-server/AndroidManifest.xml'), "AndroidManifest.xml")

      unless system  %Q{ruby -pi.bak -e "gsub(/#targetPackage#/, '#{package_name(app)}')" AndroidManifest.xml}
        raise "Could not replace package name in manifest"
      end

      unless system("#{ENV["ANDROID_HOME"]}/platform-tools/aapt package -M AndroidManifest.xml  -I #{android_platform}/android.jar -F dummy.apk")
        raise "Could not create dummy.apk"
      end

      Zip::ZipFile.new("dummy.apk").extract("AndroidManifest.xml","customAndroidManifest.xml")
      Zip::ZipFile.open("TestServer.apk") do |zip_file|
        zip_file.add("AndroidManifest.xml", "customAndroidManifest.xml")  
      end
    end
    if is_windows?
      jarsigner_path = "#{ENV["JAVA_HOME"]}/bin/jarsigner.exe"
    else
      jarsigner_path = "jarsigner"
    end

    cmd = "#{jarsigner_path} -sigalg MD5withRSA -digestalg SHA1 -signedjar #{test_server_file_name} -storepass #{keystore["keystore_password"]} -keystore \"#{File.expand_path keystore["keystore_location"]}\" #{workspace_dir}/TestServer.apk #{keystore["keystore_alias"]}"           
    unless system(cmd)
      raise "Could not sign test server"
    end
  end
  puts "Done signing the test server. Moved it to #{test_server_file_name}"
end


def read_keystore_info
  if File.exist? ".calabash_settings"
    JSON.parse(IO.read(".calabash_settings"))
  else
    {
    "keystore_location" => "#{ENV["HOME"]}/.android/debug.keystore",
    "keystore_password" => "android",
    "keystore_alias" => "androiddebugkey",
    "keystore_alias_password" => "android"
    }
  end
end