class JavaKeystore
  attr_reader :errors, :location, :keystore_alias, :password, :fingerprint
  def initialize(location, keystore_alias, password)
    raise "No such file #{location}" unless File.exists?(File.expand_path(location))

    keystore_data = system_with_stdout_on_success(Env.keytool_path, '-list', '-v', '-alias', keystore_alias, '-keystore', location, '-storepass', password, '-J-Dfile.encoding=utf-8')
    if keystore_data.nil?
      error = "Could not list certificates in keystore. Probably because the password was incorrect."
      @errors = [{:message => error}]
      log error
      raise error
      #TODO: Handle the case where password is correct but the alias is missing.
    end
    @location = location
    @keystore_alias = keystore_alias
    @password = password
    log "Key store data:"
    log keystore_data
    @fingerprint = extract_md5_fingerprint(keystore_data)
  end

  def sign_apk(apk_path, dest_path)
    raise "Cannot sign with a miss configured keystore" if errors
    raise "No such file: #{apk_path}" unless File.exists?(apk_path)

    unless system_with_stdout_on_success(Env.jarsigner_path, '-sigalg', 'MD5withRSA', '-digestalg', 'SHA1', '-signedjar', dest_path, '-storepass', password, '-keystore',  location, apk_path, keystore_alias)
      raise "Could not sign app: #{apk_path}"
    end
  end

  def system_with_stdout_on_success(cmd, *args)
    a = Escape.shell_command(args)
    cmd = "#{cmd} #{a.gsub("'", '"')}"
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
        read_keystore_with_default_password_and_alias(File.join(ENV["HOME"], ".local/share/Xamarin/Mono\ for\ Android/debug.keystore")),
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
      JavaKeystore.new(keystore["keystore_location"], keystore["keystore_alias"], keystore["keystore_password"])
  end

  def self.fail_if_key_missing(map, key)
    raise "Found .calabash_settings but no #{key} defined." unless map[key]
  end

end