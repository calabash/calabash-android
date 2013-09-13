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
    dirs = Dir["#{android_home_path}/build-tools/*/"] + Dir["#{android_home_path}/platform-tools/"]
    raise "Could not find tools directory in ANDROID_HOME" if dirs.empty?
    dirs.first
  end

  def self.android_home_path
    ENV["ANDROID_HOME"].gsub("\\", "/")
  end

  def self.android_platform_path
    platforms = Dir["#{android_home_path}/platforms/android-*"].sort_by { |item| '%08s' % item.split('-').last }
    raise "No Android SDK found in #{android_home_path}/platforms/" if platforms.empty?
    platforms.last
  end

end