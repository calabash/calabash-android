class Env

  def self.keytool_path
    if is_windows?
      "\"#{ENV["JAVA_HOME"]}/bin/keytool.exe\""
    else
      "keytool"
    end
  end

  def self.jarsigner_path
    if is_windows?
      "\"#{ENV["JAVA_HOME"]}/bin/jarsigner.exe\""
    else
      "jarsigner"
    end
  end

  def self.ant_path
    is_windows? ? "ant.bat" : "ant"
  end

  def self.is_windows?
    (RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/)
  end

  def self.tools_dir
    Dir.chdir(android_home_path) do
      dirs = Dir["build-tools/*"] + Dir["platform-tools"]
      raise "Could not find tools directory in #{android_home_path}" if dirs.empty?
      File.expand_path(dirs.first)
    end
  end

  def self.adb
    %Q("#{android_home_path}/platform-tools/adb")
  end

  def self.android_home_path
    return ENV["ANDROID_HOME"] if ENV["ANDROID_HOME"]
    monodroid_config_file = File.expand_path("~/.config/xbuild/monodroid-config.xml")
    if File.exists?(monodroid_config_file)
      require 'rexml/document'
      begin
        return REXML::Document.new(IO.read(monodroid_config_file)).elements["//android-sdk"].attributes["path"]
      rescue
      end
    end
    nil
  end

  def self.android_platform_path
    Dir.chdir(android_home_path) do
      platforms = Dir["platforms/android-*"].sort_by { |item| '%08s' % item.split('-').last }
      raise "No Android SDK found in #{android_home_path}/platforms/" if platforms.empty?
      File.expand_path(platforms.last)
    end
  end

end