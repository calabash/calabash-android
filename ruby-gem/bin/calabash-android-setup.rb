require "calabash-android/version"

def calabash_setup
  unless File.exists?(ENV["ANDROID_HOME"] || "")
    puts "Please set ANDROID_HOME to point to the Android SDK"
    exit 1
  end
  @settings = {}

  puts "To use Calabash-Android we need a bit of information about the app you want to test"
  puts "When you are through this setup your settings will be saved to .calabash_settings. You can edit this file if you have the need."
  ask_for_setting(:package_name, "What is the package name of the app? You can find the package name in AndroidManifest.xml")

  ask_for_setting(:activity_name, "What is the fully qualified name of the main activity?")
  ask_for_setting(:app_path, "What is the path to the app?")
  puts "Which api level do you want to use?"
  puts "It looks like you have the following versions installed:"
  ask_for_setting(:api_level, platform_versions.join(", "))

  @settings[:keystore_location] = "#{ENV["HOME"]}/.android/debug.keystore"
  @settings[:keystore_password] = "android"
  @settings[:keystore_alias] = "androiddebugkey"
  @settings[:keystore_alias_password] = "android"

  puts "Do you want to specify a keystore for signing the test app?"
  puts "If not we will be using #{@settings[:keystore_location]}"
  puts "Please answer yes (y) or no (n)"

  if ['yes', 'y'].include? STDIN.gets.chomp
    ask_for_setting(:keystore_location, "Please enter keystore location")
    ask_for_setting(:keystore_password, "Please enter the password for the keystore")
    ask_for_setting(:keystore_alias, "Please enter the alias")
    ask_for_setting(:keystore_alias_password, "Please enter the password for the alias")
  end


  open('.calabash_settings', 'w') do |f|
    f.puts @settings.to_json
  end
  puts "Saved your settings to .calabash_settings. You can edit the settings manually or run this setup script again"
end

def ask_for_setting(key, msg)
  puts msg
  @settings[key] = STDIN.gets.chomp
end

def platform_versions
  Dir["#{ENV["ANDROID_HOME"]}/platforms/android-*"].collect{|platform| platform.split("-").last.to_i}.sort
end