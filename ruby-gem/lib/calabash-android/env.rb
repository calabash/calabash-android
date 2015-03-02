require_relative 'helpers'

class Env
  require 'win32/registry' if RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/

  def self.exit_if_env_not_set_up
    exit_unless_jdk_is_available
    exit_unless_android_sdk_is_available
  end

  def self.exit_unless_android_sdk_is_available
    if android_home_path
      log "Android SDK found at: #{android_home_path}"
      return
    end
    puts "Could not find an Android SDK please make sure it is installed."
    puts "You can read about how Calabash-Android is searching for an Android SDK and how you can help here:"
    puts "https://github.com/calabash/calabash-android/blob/master/documentation/installation.md#prerequisites"
    exit 1
  end

  def self.keytool_path
    find_executable_on_path(keytool_executable) ||
    "\"#{jdk_path}/bin/#{keytool_executable}\""
  end

  def self.exit_unless_jdk_is_available
    jdk = jdk_path
    if find_executable_on_path(keytool_executable) || jdk
      log "JDK found on PATH." if find_executable_on_path(keytool_executable)
      log "JDK found at: #{jdk}" if jdk
      return
    end
    puts "Could not find Java Development Kit please make sure it is installed."
    puts "You can read about how Calabash-Android is searching for a JDK and how you can help here:"
    puts "https://github.com/calabash/calabash-android/blob/master/documentation/installation.md#prerequisites"
    exit 1
  end

  def self.jarsigner_path
    find_executable_on_path(jarsigner_executable) ||
    "\"#{jdk_path}/bin/#{jarsigner_executable}\""
  end

  def self.java_path
    find_executable_on_path(java_executable) ||
    "\"#{jdk_path}/bin/#{java_executable}\""
  end

  def self.jdk_path
    path_if_jdk(ENV['JAVA_HOME']) ||
    if is_windows?
      path_if_jdk(read_registry(::Win32::Registry::HKEY_LOCAL_MACHINE, 'SOFTWARE\\JavaSoft\\Java Development Kit\\1.8', 'JavaHome')) ||
      path_if_jdk(read_registry(::Win32::Registry::HKEY_LOCAL_MACHINE, 'SOFTWARE\\JavaSoft\\Java Development Kit\\1.7', 'JavaHome')) ||
      path_if_jdk(read_registry(::Win32::Registry::HKEY_LOCAL_MACHINE, 'SOFTWARE\\JavaSoft\\Java Development Kit\\1.6', 'JavaHome'))
    else
      path_if_jdk(read_attribute_from_monodroid_config('java-sdk', 'path'))
    end
  end

  def self.android_home_path
    path_if_android_home(ENV["ANDROID_HOME"]) ||
    if is_windows?
      path_if_android_home(read_registry(::Win32::Registry::HKEY_LOCAL_MACHINE, 'SOFTWARE\\Android SDK Tools', 'Path')) ||
      path_if_android_home("C:\\Android\\android-sdk")
    else
      path_if_android_home(read_attribute_from_monodroid_config('android-sdk', 'path'))
    end
  end

  def self.find_executable_on_path(executable)
    path_elements.each do |x|
      f = File.join(x, executable)
      return "\"#{f}\"" if File.exists?(f)
    end
    nil
  end

  def self.path_if_jdk(path)
    path if path && File.exists?(File.join(path, 'bin', jarsigner_executable))
  end

  def self.zipalign_path
    zipalign_path = File.join(android_home_path, 'tools', zipalign_executable)

    unless File.exists?(zipalign_path)
      log "Did not find zipalign at '#{zipalign_path}'. Trying to find zipalign in tools directories."

      tools_directories.each do |dir|
        zipalign_path = File.join(dir, zipalign_executable)
        break if File.exists?(zipalign_path)
      end
    end

    if File.exists?(zipalign_path)
      log "Found zipalign at '#{zipalign_path}'"
      zipalign_path
    else
      log("Did not find zipalign in any of '#{tools_directories.join("','")}'.", true)
      raise 'Could not find zipalign'
    end
  end

  def self.zipalign_executable
    is_windows? ? 'zipalign.exe' : 'zipalign'
  end

  def self.jarsigner_executable
    is_windows? ? 'jarsigner.exe' : 'jarsigner'
  end

  def self.java_executable
    is_windows? ? 'java.exe' : 'java'
  end
  
  def self.keytool_executable
    is_windows? ? 'keytool.exe' : 'keytool'
  end

  def self.adb_executable
    is_windows? ? 'adb.exe' : 'adb'
  end

  def self.ant_path
    is_windows? ? "ant.bat" : "ant"
  end

  def self.is_windows?
    (RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/)
  end

  def self.tools_dir
    tools_dir = tools_directories.first
    log "Found tools directory at '#{tools_dir}'"
    tools_dir
  end

  def self.tools_directories
    Dir.chdir(android_home_path) do
      dirs = Dir["build-tools/*"] + Dir["platform-tools"]
      raise "Could not find tools directory in #{android_home_path}" if dirs.empty?
      dirs.map {|dir| File.expand_path(dir)}
    end
  end

  def self.adb_path
    %Q("#{android_home_path}/platform-tools/#{adb_executable}")
  end  

  def self.path_if_android_home(path)
    path if path && File.exists?(File.join(path, 'platform-tools', adb_executable))
  end

  def self.path_elements
    return [] unless ENV['PATH']
    ENV['PATH'].split (/[:;]/)
  end

  def self.read_attribute_from_monodroid_config(element, attribute)
    monodroid_config_file = File.expand_path("~/.config/xbuild/monodroid-config.xml")
    if File.exists?(monodroid_config_file)
      require 'rexml/document'
      begin
        return REXML::Document.new(IO.read(monodroid_config_file)).elements["//#{element}"].attributes[attribute]
      rescue
      end
    end
  end

  def self.android_platform_path
    Dir.chdir(android_home_path) do
      platforms = Dir["platforms/android-*"].sort_by { |item| '%08s' % item.split('-').last }
      raise "No Android SDK found in #{android_home_path}/platforms/" if platforms.empty?
      File.expand_path(platforms.last)
    end
  end

  def self.read_registry(root_key, key, value)
    begin
      root_key.open(key)[value]
    rescue
      nil
    end
  end

end

