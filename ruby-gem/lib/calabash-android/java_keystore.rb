class JavaKeystore
  attr_reader :errors, :location, :keystore_alias, :password, :fingerprint
  def initialize(location, keystore_alias, password)
    raise "No such file #{location}" unless File.exists?(File.expand_path(location))

    keystore_data = system_with_stdout_on_success(Env.keytool_path, '-list', '-v', '-alias', keystore_alias, '-keystore', location, '-storepass', password)
    unless keystore_data
      error = "Could not list certificates in keystore. Probably because the password was incorrect."
      @errors = [message: error]
      log error
      #TODO: Handle the case where password is correct but the alias is missing.
    end
    @location = location
    @keystore_alias = keystore_alias
    @password = password
    @fingerprint = extract_md5_fingerprint(keystore_data)
  end

  def sign_apk(apk_path, dest_path)
    raise "Cannot sign with a miss configured keystore" if errors
    raise "No such file: #{apk_path}" unless File.exists?(apk_path)

    unless system_with_stdout_on_success(Env.jarsigner_path, '-sigalg', 'MD5withRSA', '-digestalg', 'SHA1', '-signedjar', dest_path, '-storepass', password, '-keystore',  location, apk_path, keystore_alias)
      raise "Could not sign app: #{apk_path}"
    end
  end

  private
  def system_with_stdout_on_success(cmd, *args)
    args = args.clone.unshift cmd

    out, err = nil, nil
    cmd = Escape.shell_command(args)
    log "Command: #{cmd}"
    status = POpen4::popen4(cmd) do |stdout, stderr, stdin, pid|
      out = stdout.read
      err = stderr.read
    end
    if status.exitstatus == 0
      out
    else
      nil
    end
  end
end

def get_keystore
  if File.exist? ".calabash_settings"
    keystore = JSON.parse(IO.read(".calabash_settings"))
    fail_if_key_missing(keystore, "keystore_location")
    fail_if_key_missing(keystore, "keystore_password")
    fail_if_key_missing(keystore, "keystore_alias")
    keystore["keystore_location"] = File.expand_path(keystore["keystore_location"])
    JavaKeystore.new(keystore["keystore_location"], keystore["keystore_alias"], keystore["keystore_password"])
  else
    JavaKeystore.new(File.expand_path(File.join(ENV["HOME"], "/.android/debug.keystore")), 'androiddebugkey', 'android')
  end
end

def fail_if_key_missing(map, key)
  raise "Found .calabash_settings but no #{key} defined." unless map[key]
end
