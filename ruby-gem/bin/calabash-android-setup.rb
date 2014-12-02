require "calabash-android/version"
require "io/console"

def calabash_setup
  @settings = {}

  puts "Please enter keystore information to use a custom keystore instead of the default"

  ask_for_setting(:keystore_location, "Please enter keystore location")
  ask_for_setting(:keystore_password, "Please enter the password for the keystore", true)
  ask_for_setting(:keystore_alias, "Please enter the alias")

  open('.calabash_settings', 'w') do |f|
    f.puts @settings.to_json
  end
  puts "Saved your settings to .calabash_settings. You can edit the settings manually or run this setup script again"
end

def ask_for_setting(key, msg, secure = false)
  puts msg
  @settings[key] = (!secure ? STDIN.gets : STDIN.noecho(&:gets)).chomp
end
