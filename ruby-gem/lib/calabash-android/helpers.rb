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
  out_path = manifest_path(app)
  manifest_file = File.join(out_path, 'AndroidManifest.xml')
  unless File.exist?(manifest_file)
    manifest_extractor = File.join(File.expand_path(File.dirname(__FILE__)),'lib', 'apktool-cli-1.5.3-SNAPSHOT.jar')
    output = `java -jar "#{manifest_extractor}" d -s "#{app}" #{out_path} 2>&1`
    raise "Unable to extract manifest: #{output}" unless File.size?(manifest_file)
    # Tidy up a bit. It would be nice if apktool could just dump the manifest alone.
    FileUtils.rm_rf(%w{res assets classes.dex}.map {|f| File.join(out_path, f) })
  end
  File.read(manifest_file)
end

def checksum(file_path)
  require 'digest/md5'
  Digest::MD5.file(file_path).hexdigest
end

def test_server_path(apk_file_path)
  "test_servers/#{checksum(apk_file_path)}_#{Calabash::Android::VERSION}.apk"
end

def manifest_path(apk_file_path)
  "test_servers/#{checksum(apk_file_path)}_#{Calabash::Android::VERSION}.res"
end


def build_test_server_if_needed(app_path)
  unless File.exist?(test_server_path(app_path))
    if ARGV.include? "--no-build"
      puts "No test server found for this combination of app and calabash version. Exiting!"
      exit 1
    else
      puts "No test server found for this combination of app and calabash version. Recreating test server."
      calabash_build(app_path)
    end
  end
end

def resign_apk(app_path)
  Dir.mktmpdir do |tmp_dir|
    log "Resign apk"
    unsigned_path = File.join(tmp_dir, 'unsigned.apk')
    FileUtils.cp(app_path, unsigned_path)

    `java -jar "#{File.dirname(__FILE__)}/lib/unsign.jar" "#{unsigned_path}"`

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

  cmd = "#{jarsigner_path} -sigalg MD5withRSA -digestalg SHA1 -signedjar #{dest_path} -storepass #{keystore["keystore_password"]} -keystore #{keystore["keystore_location"]} #{app_path} #{keystore["keystore_alias"]}"
  log cmd
  unless system(cmd)
    puts "jarsigner command: #{cmd}"
    raise "Could not sign app (#{app_path}"
  end
end

def tools_dir
  dirs = Dir["#{android_home_path}/build-tools/*/"] + Dir["#{android_home_path}/platform-tools/"]
  raise "Could not find tools directory in ANDROID_HOME" if dirs.empty?
  dirs.first
end

def android_home_path
  ENV["ANDROID_HOME"].gsub("\\", "/")
end

def read_keystore_info
  keystore = default_keystore

  if File.exist? ".calabash_settings"
    keystore = JSON.parse(IO.read(".calabash_settings"))
    fail_if_key_missing(keystore, "keystore_location")
    fail_if_key_missing(keystore, "keystore_password")
    fail_if_key_missing(keystore, "keystore_alias")
    keystore["keystore_location"] = File.expand_path(keystore["keystore_location"])
  end
  keystore["keystore_location"] = put_in_quotes(remove_quotes(keystore["keystore_location"]))
  keystore
end

def default_keystore
  {
    "keystore_location" => File.expand_path(File.join(ENV["HOME"], "/.android/debug.keystore")),
    "keystore_password" => "android",
    "keystore_alias" => "androiddebugkey",
  }
end

def fail_if_key_missing(map, key)
  raise "Found .calabash_settings but no #{key} defined." unless map[key]
end

def remove_quotes(s)
  s.gsub(/"/, "")
end

def put_in_quotes(s)
  %Q{"#{s}"}
end

def keytool_path
  if is_windows?
    "\"#{ENV["JAVA_HOME"]}/bin/keytool.exe\""
  else
    "keytool"
  end
end

def fingerprint_from_keystore
  keystore_info = read_keystore_info
  cmd = "#{keytool_path} -v -list -alias #{keystore_info["keystore_alias"]} -keystore #{keystore_info["keystore_location"]} -storepass #{keystore_info["keystore_password"]}"

  log cmd
  fingerprints = `#{cmd}`
  md5_fingerprint = extract_md5_fingerprint(fingerprints)
  log "MD5 fingerprint for keystore (#{keystore_info["keystore_location"]}): #{md5_fingerprint}"
  md5_fingerprint
end

def fingerprint_from_apk(app_path)
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

      cmd = "#{keytool_path} -v -printcert -file #{rsa_files.first}"
      log cmd
      fingerprints = `#{cmd}`
      md5_fingerprint = extract_md5_fingerprint(fingerprints)
      log "MD5 fingerprint for signing cert (#{app_path}): #{md5_fingerprint}"
      md5_fingerprint
    end
  end
end

def extract_md5_fingerprint(fingerprints)
  log fingerprints

  m = fingerprints.scan(/MD5.*((?:[a-fA-F\d]{2}:){15}[a-fA-F\d]{2})/).flatten
  raise "No MD5 fingerprint found:\n #{fingerprints}" if m.empty?
  m.first
end

def is_windows?
  require 'rbconfig'
  (RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/)
end

def log(message, error = false)
  $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}" if (error or ARGV.include? "-v" or ARGV.include? "--verbose")
end
