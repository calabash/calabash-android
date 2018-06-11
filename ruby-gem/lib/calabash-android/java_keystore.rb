class JavaKeystore
  attr_reader :errors, :location, :keystore_alias, :password, :fingerprint
  attr_reader :signature_algorithm_name

  def initialize(location, keystore_alias, password, key_password = nil)
    raise "No such keystore file '#{location}'" unless File.exists?(File.expand_path(location))
    log "Reading keystore data from keystore file '#{File.expand_path(location)}'"

    keystore_data = system_with_stdout_on_success(Calabash::Android::Dependencies.keytool_path, '-list', '-v', '-alias', keystore_alias, '-keystore', location, '-storepass', password, '-J"-Dfile.encoding=utf-8"', '-J"-Duser.language=en-US"')

    if keystore_data.nil?
      if keystore_alias.empty?
        log "Could not obtain keystore data. Will try to extract alias automatically"

        keystore_data = system_with_stdout_on_success(Calabash::Android::Dependencies.keytool_path, '-list', '-v', '-keystore', location, '-storepass', password, '-J"-Dfile.encoding=utf-8"', '-J"-Duser.language=en-US"')
        aliases = keystore_data.scan(/Alias name\:\s*(.*)/).flatten

        if aliases.length == 0
          raise 'Could not extract alias automatically. Please specify alias using calabash-android setup'
        elsif aliases.length > 1
          raise 'Multiple aliases found in keystore. Please specify alias using calabash-android setup'
        else
          keystore_alias = aliases.first
          log "Extracted keystore alias '#{keystore_alias}'. Continuing"

          return initialize(location, keystore_alias, password)
        end
      else
        error = "Could not list certificates in keystore. Probably because the password was incorrect."
        @errors = [{:message => error}]
        log error
        raise error
      end
    end

    @location = location
    @keystore_alias = keystore_alias
    @password = password
    @key_password = key_password
    log "Key store data:"
    log keystore_data
    @fingerprint = extract_sha1_fingerprint(keystore_data)
    @signature_algorithm_name = extract_signature_algorithm_name(keystore_data)
    log "Fingerprint: #{fingerprint}"
    log "Signature algorithm name: #{signature_algorithm_name}"
  end

  def sign_apk(apk_path, dest_path)
    raise "Cannot sign with a miss configured keystore" if errors
    raise "No such file: #{apk_path}" unless File.exists?(apk_path)

    # E.g. MD5withRSA or MD5withRSAandMGF1
    encryption = signature_algorithm_name.split('with')[1].split('and')[0]
    signing_algorithm = "SHA1with#{encryption}"
    digest_algorithm = 'SHA1'

    log "Signing using the signature algorithm: '#{signing_algorithm}'"
    log "Signing using the digest algorithm: '#{digest_algorithm}'"

    cmd_args = {
      '-sigfile' => 'CERT',
      '-sigalg' => signing_algorithm,
      '-digestalg' => digest_algorithm,
      '-signedjar' => dest_path,
      '-storepass' => password,
      '-keystore' =>  location,
    }

    unless @key_password.nil?
      cmd_args['-keypass'] = @key_password
    end

    cmd_args = cmd_args.flatten
    cmd_args << apk_path
    cmd_args << keystore_alias

    result = system_with_stdout_on_success(Calabash::Android::Dependencies.jarsigner_path, *cmd_args)

    unless result
      raise "Could not sign app: #{apk_path}"
    end
  end

  def system_with_stdout_on_success(cmd, *args)
    a = Escape.shell_command(args)
    cmd = "\"#{cmd}\" #{a.gsub("'", '"')}"
    log cmd
    out = `#{cmd}`
    if $?.exitstatus == 0
      out
    else
      nil
    end
  end

  def self.read_keystore_with_default_password_and_alias(path)
    path = File.expand_path path

    if File.exists? path
      keystore = JavaKeystore.new(path, 'androiddebugkey', 'android')
      if keystore.errors
        log "Trying to "
        nil
      else
        log "Unlocked keystore at #{path} - fingerprint: #{keystore.fingerprint}"
        keystore
      end
    else
      log "Trying to read keystore from: #{path} - no such file"
      nil
    end
  end

  def self.get_keystores
    if keystore = keystore_from_settings
      [ keystore ]
    else
      [
        read_keystore_with_default_password_and_alias(File.join(ENV["HOME"], "/.android/debug.keystore")),
        read_keystore_with_default_password_and_alias("debug.keystore"),
        read_keystore_with_default_password_and_alias(File.join(ENV["HOME"], ".local/share/Xamarin/Mono\\ for\\ Android/debug.keystore")),
        read_keystore_with_default_password_and_alias(File.join(ENV["HOME"], "AppData/Local/Xamarin/Mono for Android/debug.keystore")),
      ].compact
    end
  end

  def self.keystore_from_settings
      keystore = JSON.parse(IO.read(".calabash_settings")) if File.exist? ".calabash_settings"
      keystore = JSON.parse(IO.read("calabash_settings")) if File.exist? "calabash_settings"
      return unless keystore
      fail_if_key_missing(keystore, "keystore_location")
      fail_if_key_missing(keystore, "keystore_password")
      fail_if_key_missing(keystore, "keystore_alias")
      keystore["keystore_location"] = File.expand_path(keystore["keystore_location"])
      log("Keystore location specified in #{File.exist?(".calabash_settings") ? ".calabash_settings" : "calabash_settings"}.")
      JavaKeystore.new(keystore["keystore_location"], keystore["keystore_alias"], keystore["keystore_password"], keystore['key_password'])
  end

  def self.fail_if_key_missing(map, key)
    raise "Found .calabash_settings but no #{key} defined." unless map[key]
  end

end
