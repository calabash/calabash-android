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

  def self.is_windows?
    (RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/)
  end
end