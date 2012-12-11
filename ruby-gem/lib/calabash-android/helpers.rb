require 'rexml/document'
require 'rexml/xpath'
require 'zip/zip'
require 'tempfile'

include REXML

def package_name(app)
  require 'rexml/document'
  require 'rexml/xpath'

  manifest = Document.new(manifest(app))
  manifest.root.attributes['package']
end

def main_activity(app)
  manifest = Document.new(manifest(app))
  main_activity = manifest.elements["//action[@name='android.intent.action.MAIN']/../.."].attributes['name']
  #Handle situation where main activity is on the form '.class_name'
  if main_activity.start_with? "."
    main_activity = package_name(app) + main_activity
  elsif not main_activity.include? "." #This is undocumentet behaviour but Android seems to accept shorthand naming that does not start with '.'
    main_activity = "#{package_name(app)}.#{main_activity}"
  end
  main_activity
end

def manifest(app)
  `java -jar "#{File.dirname(__FILE__)}/lib/manifest_extractor.jar" "#{app}"`
end

def checksum(file_path)
  require 'digest/md5'
  Digest::MD5.file(file_path).hexdigest
end

def test_server_path(apk_file_path)
  "test_servers/#{checksum(apk_file_path)}_#{Calabash::Android::VERSION}.apk"
end

def resign_apk(app_path)
  Dir.mktmpdir do |tmp_dir|
    log "Resign apk"
    unsigned_path = File.join(tmp_dir, 'unsigned.apk')
    FileUtils.cp(app_path, unsigned_path)

    #Delete META-INF/*
    to_remove = Zip::ZipFile.foreach(unsigned_path).find_all { |e| /^META-INF\// =~ e.name}.collect &:name

    Zip::ZipFile.open(unsigned_path) do |zip_file|
      to_remove.each do |x|
        log "Removing #{x}"
        zip_file.remove x
      end
      zip_file.commit
    end
    sign_apk(unsigned_path, app_path)
  end
end

def sign_apk(app_path, dest_path)
  keystore = read_keystore_info()

  if is_windows?
    jarsigner_path = "\"#{ENV["JAVA_HOME"]}/bin/jarsigner.exe\""
  else
    jarsigner_path = "jarsigner"
  end

  cmd = "#{jarsigner_path} -sigalg MD5withRSA -digestalg SHA1 -signedjar #{dest_path} -storepass #{keystore["keystore_password"]} -keystore \"#{File.expand_path keystore["keystore_location"]}\" #{app_path} #{keystore["keystore_alias"]}"
  log cmd
  unless system(cmd)
    puts "jarsigner command: #{cmd}"
    raise "Could not sign app (#{app_path}"
  end
end

def read_keystore_info
  if File.exist? ".calabash_settings"
    JSON.parse(IO.read(".calabash_settings"))
  else
    {
    "keystore_location" => "#{ENV["HOME"]}/.android/debug.keystore",
    "keystore_password" => "android",
    "keystore_alias" => "androiddebugkey",
    }
  end
end

def sha1_fingerprint fingerprints
  return nil if fingerprints.nil?
  m = fingerprints.match(/SHA1\):\s+((?:\h\h:){19}\h\h)/)
  return nil if m.nil? || m.length < 2
  m[1]
end

def md5_fingerprint fingerprints
  return nil if fingerprints.nil?
  m = fingerprints.match(/MD5\):\s+((?:\h\h:){15}\h\h)/)
  return nil if m.nil? || m.length < 2
  m[1]
end

def fingerprint_from_keystore
  keystore_info = read_keystore_info

  command = "keytool -list -alias #{keystore_info["keystore_alias"]} -keystore #{keystore_info["keystore_location"]} -storepass #{keystore_info["keystore_password"]}"

  fingerprints = `#{command}`
  log 'fingerprint_from_keystore'
  log command
  log fingerprints

  fingerprint = md5_fingerprint(fingerprints) || sha1_fingerprint(fingerprints)

  log "fingerprint for #{keystore_info["keystore_location"]}: #{fingerprint}"
  fingerprint
end

def fingerprint_from_apk_match_keystore(app_path)
  Dir.mktmpdir do |tmp_dir|
    Dir.chdir(tmp_dir) do
      FileUtils.cp(app_path, "app.apk")
      FileUtils.mkdir("META-INF")
      Zip::ZipFile.foreach("app.apk") do |z|
        z.extract if /^META-INF\/\w+.(RSA|rsa)/ =~ z.name
      end
      rsa_files = Dir["#{tmp_dir}/META-INF/*"]
      raise "No RSA file found in META-INF. Cannot proceed." if rsa_files.empty?
      raise "More than one RSA file found in META-INF. Cannot proceed." if rsa_files.length > 1

      command = "keytool -printcert -file #{rsa_files.first}"

      fingerprints = `#{command}`

      log 'fingerprint_from_apk_match_keystore'
      log command
      log fingerprints

      # keystore is MD5 or SHA1
      keystore = fingerprint_from_keystore

      # APK includes MD5, SHA1, SHA256
      # SHA256 is not currently supported
      apk_md5  = md5_fingerprint(fingerprints)
      apk_sha1 = sha1_fingerprint(fingerprints)

      log "MD5 fingerprint for signing cert (#{app_path}): #{apk_md5}"
      log "SHA1 fingerprint for signing cert (#{app_path}): #{apk_sha1}"

      return keystore == apk_md5 || keystore == apk_sha1
    end
  end
end

def is_windows?
  require 'rbconfig'
  (RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/)
end

def log(message, error = false)
  $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}" if (error or ARGV.include? "-v" or ARGV.include? "--verbose")
end
