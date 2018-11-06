require "stringio"
require 'zip'
require 'tempfile'
require 'escape'
require 'rbconfig'
require 'calabash-android/java_keystore'
require 'calabash-android/environment'
require 'calabash-android/logging'
require 'calabash-android/dependencies'
require 'calabash-android/version'
require 'calabash-android/utils'

def package_name(app)
  unless File.exist?(app)
    raise "Application '#{app}' does not exist"
  end

  package_line = aapt_dump(app, "package").first
  raise "'package' not found in aapt output" unless package_line
  m = package_line.match(/name='([^']+)'/)
  raise "Unexpected output from aapt: #{package_line}" unless m
  m[1]
end

def main_activity(app)
  unless File.exist?(app)
    raise "Application '#{app}' does not exist"
  end

  begin
    log("Trying to find launchable activity")
    launchable_activity_line = aapt_dump(app, "launchable-activity").first
    raise "'launchable-activity' not found in aapt output" unless launchable_activity_line
    m = launchable_activity_line.match(/name='([^']+)'/)
    raise "Unexpected output from aapt: #{launchable_activity_line}" unless m
    log("Found launchable activity '#{m[1]}'")
    m[1]
  rescue => e
    log("Could not find launchable activity, trying to parse raw AndroidManifest. #{e.message}")

    manifest_data = `"#{Calabash::Android::Dependencies.aapt_path}" dump xmltree "#{app}" AndroidManifest.xml`
    regex = /^\s*A:[\s*]android:name\(\w+\)\=\"android.intent.category.LAUNCHER\"/
    lines = manifest_data.lines.collect(&:strip)
    indicator_line = nil

    lines.each_with_index do |line, index|
      match = line.match(regex)

      unless match.nil?
        raise 'More than one launchable activity in AndroidManifest' unless indicator_line.nil?
        indicator_line = index
      end
    end

    raise 'No launchable activity found in AndroidManifest' unless indicator_line

    intent_filter_found = false

    (0..indicator_line).reverse_each do |index|
      if intent_filter_found
        match = lines[index].match(/\s*E:\s*activity-alias/)

        raise 'Could not find target activity in activity alias' if match

        match = lines[index].match(/^\s*A:\s*android:targetActivity\(\w*\)\=\"([^\"]+)/){$1}

        if match
          log("Found launchable activity '#{match}'")

          return match
        end
      else
        unless lines[index].match(/\s*E: intent-filter/).nil?
          log("Read intent filter")
          intent_filter_found = true
        end
      end
    end

    raise 'Could not find launchable activity'
  end
end

def aapt_dump(app, key)
  lines = `"#{Calabash::Android::Dependencies.aapt_path}" dump badging "#{app}"`.lines.collect(&:strip)
  lines.select { |l| l.start_with?("#{key}:") }
end

def checksum(file_path)
  require 'digest/md5'
  Digest::MD5.file(file_path).hexdigest
end

def test_server_path(apk_file_path)
  "test_servers/#{checksum(apk_file_path)}_#{Calabash::Android::VERSION}.apk"
end

def build_test_server_if_needed(app_path)
  unless File.exist?(test_server_path(app_path))
    if ARGV.include? "--no-build"
      puts "No test server found for this combination of app and calabash version. Exiting!"
      exit 1
    else
      puts "No test server found for this combination of app and calabash version. Recreating test server."
      require 'calabash-android/operations'
      require File.join(File.dirname(__FILE__), '..', '..', 'bin', 'calabash-android-build')
      calabash_build(app_path)
    end
  end
end

def resign_apk(app_path)
  Dir.mktmpdir do |tmp_dir|
    log "Resign apk"
    unsigned_path = File.join(tmp_dir, 'unsigned.apk')
    unaligned_path = File.join(tmp_dir, 'unaligned.apk')
    FileUtils.cp(app_path, unsigned_path)
    unsign_apk(unsigned_path)
    sign_apk(unsigned_path, unaligned_path)
    zipalign_apk(unaligned_path, app_path)
  end
end

def unsign_apk(path)
  meta_files = `"#{Calabash::Android::Dependencies.aapt_path}" list "#{path}"`.lines.collect(&:strip).grep(/^META-INF\//)

  signing_file_names = ['.mf', '.rsa', '.dsa', '.ec', '.sf']

  files_to_remove = meta_files.select do |file|
    # other will be:
    # META-INF/foo/bar
    #  other #=> bar
    directory, file_name, other = file.split('/')

    if other != nil || file_name.nil?
      false
    else
      if signing_file_names.include?(File.extname(file_name).downcase)
        true
      end
    end
  end

  if files_to_remove.empty?
    log "App wasn't signed. Will not try to unsign it."
  else
    system("\"#{Calabash::Android::Dependencies.aapt_path}\" remove \"#{path}\" #{files_to_remove.join(" ")}")
  end
end

def zipalign_apk(inpath, outpath)
  cmd = %Q("#{Calabash::Android::Dependencies.zipalign_path}" -f 4 "#{inpath}" "#{outpath}")
  log "Zipaligning using: #{cmd}"
  system(cmd)
end

def sign_apk(app_path, dest_path)
  java_keystore = JavaKeystore.get_keystores.first

  if java_keystore.nil?
    raise 'No keystores found. You can specify the keystore location and credentials using calabash-android setup'
  end

  java_keystore.sign_apk(app_path, dest_path)
end

def fingerprint_from_apk(app_path)
  app_path = File.expand_path(app_path)
  Dir.mktmpdir do |tmp_dir|
    Dir.chdir(tmp_dir) do
      FileUtils.cp(app_path, "app.apk")
      FileUtils.mkdir("META-INF")

      Calabash::Utils.with_silent_zip do
        Zip::File.foreach("app.apk") do |z|
          z.extract if /^META-INF\/\w+\.(rsa|dsa)$/i =~ z.name
        end
      end

      signature_files = Dir["#{tmp_dir}/META-INF/*"]

      log 'Signature files:'

      signature_files.each do |signature_file|
        log signature_file
      end

      raise "No signature files found in META-INF. Cannot proceed." if signature_files.empty?
      raise "More than one signature file (DSA or RSA) found in META-INF. Cannot proceed." if signature_files.length > 1

      cmd = "\"#{Calabash::Android::Dependencies.keytool_path}\" -v -printcert -J\"-Dfile.encoding=utf-8\" -file \"#{signature_files.first}\""
      log cmd
      fingerprints = `#{cmd}`
      md5_fingerprint = extract_sha1_fingerprint(fingerprints)
      log "SHA1 fingerprint for signing cert (#{app_path}): #{md5_fingerprint}"
      md5_fingerprint
    end
  end
end

def extract_md5_fingerprint(fingerprints)
  m = fingerprints.scan(/MD5.*((?:[a-fA-F\d]{2}:){15}[a-fA-F\d]{2})/).flatten
  raise "No MD5 fingerprint found:\n #{fingerprints}" if m.empty?
  m.first
end

def extract_sha1_fingerprint(fingerprints)
  m = fingerprints.scan(/SHA1.*((?:[a-fA-F\d]{2}:){15}[a-fA-F\d]{2})/).flatten
  raise "No SHA1 fingerprint found:\n #{fingerprints}" if m.empty?
  m.first
end

def extract_signature_algorithm_name(fingerprints)
  m = fingerprints.scan(/Signature algorithm name: (.*)/).flatten
  raise "No signature algorithm names found:\n #{fingerprints}" if m.empty?
  m.first
end

def log(message, error = false)
  if error or ARGV.include? "-v" or ARGV.include? "--verbose" or ENV["DEBUG"] == "1"
    $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}"
  end
end
