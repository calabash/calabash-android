require 'httpclient'
require 'json'
require 'net/http'
require 'open-uri'
require 'rubygems'
require 'json'
require 'socket'
require 'timeout'
require 'calabash-android/gestures'
require 'calabash-android/helpers'
require 'calabash-android/environment_helpers'
require 'calabash-android/text_helpers'
require 'calabash-android/touch_helpers'
require 'calabash-android/drag_helpers'
require 'calabash-android/wait_helpers'
require 'calabash-android/version'
require 'calabash-android/env'
require 'calabash-android/environment'
require 'calabash-android/dot_dir'
require 'calabash-android/logging'
require 'calabash-android/retry'
require 'calabash-android/store/preferences'
require 'calabash-android/usage_tracker'
require 'calabash-android/dependencies'
require 'cucumber'
require 'date'
require 'time'
require 'shellwords'
require 'digest'

Calabash::Android::Dependencies.setup

module Calabash module Android

  module Operations
    include Calabash::Android::EnvironmentHelpers
    include Calabash::Android::TextHelpers
    include Calabash::Android::TouchHelpers
    include Calabash::Android::WaitHelpers
    include Calabash::Android::DragHelpers

    def self.extended(base)
      if (class << base; included_modules.map(&:to_s).include?('Cucumber::RbSupport::RbWorld'); end)
        unless instance_methods.include?(:embed)
          original_embed = base.method(:embed)
          define_method(:embed) do |*args|
            original_embed.call(*args)
          end
        end
      end
    end

    def current_activity
      `#{default_device.adb_command} shell dumpsys window windows`.force_encoding('UTF-8').each_line.grep(/mFocusedApp.+[\.\/]([^.\s\/\}]+)/){$1}.first
    end

    def log(message)
      $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}" if (ARGV.include? "-v" or ARGV.include? "--verbose" or ENV["DEBUG"] == "1")
    end

    def macro(txt)
      if self.respond_to?(:step)
        step(txt)
      else
        Then(txt)
      end
    end

    def default_device
      @@default_device ||= Device.new(self, ENV["ADB_DEVICE_ARG"], ENV["TEST_SERVER_PORT"], ENV["APP_PATH"], ENV["TEST_APP_PATH"])
    end

    def set_default_device(device)
      @@default_device = device
    end

    def performAction(action, *arguments)
      puts "Warning: The method performAction is deprecated. Please use perform_action instead."

      perform_action(action, *arguments)
    end

    def perform_action(action, *arguments)
      if removed_actions.include?(action)
        puts "\e[31mError: The action '#{action}' was removed in calabash-android 0.5\e[0m"
        puts 'Solutions that do not require the removed action can be found on:'
        puts "\e[36mhttps://github.com/calabash/calabash-android/blob/master/migrating_to_calabash_0.5.md\##{action}\e[0m"
      elsif deprecated_actions.has_key?(action)
        puts "\e[31mWarning: The action '#{action}' is deprecated\e[0m"
        puts "\e[32mUse '#{deprecated_actions[action]}' instead\e[0m"
      end

      default_device.perform_action(action, *arguments)
    end

    def removed_actions
      @removed_actions ||= File.readlines(File.join(File.dirname(__FILE__), 'removed_actions.txt')).map(&:chomp)
    end

    def deprecated_actions
      @deprecated_actions ||= Hash[
          *File.readlines(File.join(File.dirname(__FILE__), 'deprecated_actions.map')).map{|e| e.chomp.split(',', 2)}.flatten
      ]
    end

    def reinstall_apps
      default_device.reinstall_apps
    end

    # Ensures that the application and the test-server are installed.
    #
    # If the application has already been installed, it does nothing.
    # If the test-server has already been installed, it does nothing.
    def ensure_app_installed
      default_device.ensure_apps_installed
    end

    def reinstall_test_server
      default_device.reinstall_test_server
    end

    def install_app(app_path)
      default_device.install_app(app_path)
    end

    def update_app(app_path)
      default_device.update_app(app_path)
    end

    def uninstall_apps
      default_device.uninstall_app(package_name(default_device.test_server_path))
      default_device.uninstall_app(package_name(default_device.app_path))
    end

    def wake_up
      default_device.wake_up()
    end

    def clear_app_data
      default_device.clear_app_data
    end

    def pull(remote, local)
      default_device.pull(remote, local)
    end

    def push(local, remote)
      default_device.push(local, remote)
    end

    def start_test_server_in_background(options={}, &block)
      default_device.start_test_server_in_background(options, &block)
    end

    def shutdown_test_server
      default_device.shutdown_test_server
    end

    def screenshot_embed(options={:prefix => nil, :name => nil, :label => nil})
      path = default_device.screenshot(options)
      embed(path, "image/png", options[:label] || File.basename(path))
    end

    def screenshot(options={:prefix => nil, :name => nil})
      default_device.screenshot(options)
    end

    def client_version
      default_device.client_version
    end

    def server_version
      default_device.server_version
    end

    def fail(msg="Error. Check log for details.", options={:prefix => nil, :name => nil, :label => nil})
      screenshot_and_raise(msg, options)
    end

    def set_gps_coordinates_from_location(location)
      default_device.set_gps_coordinates_from_location(location)
    end

    def set_gps_coordinates(latitude, longitude)
      default_device.set_gps_coordinates(latitude, longitude)
    end

    def get_preferences(name)
      default_device.get_preferences(name)
    end

    def set_preferences(name, hash)
      default_device.set_preferences(name, hash)
    end

    def clear_preferences(name)
      default_device.clear_preferences(name)
    end

    def set_activity_orientation(orientation)
      unless orientation.is_a?(Symbol)
        raise ArgumentError, "Orientation is not a symbol"
      end

      unless orientation == :landscape || orientation == :portrait ||
          orientation == :reverse_landscape || orientation == :reverse_portrait
        raise ArgumentError, "Invalid orientation given. Use :landscape, :portrait, :reverse_landscape, or :reverse_portrait"
      end

      perform_action("set_activity_orientation", orientation.to_s)
    end

    # Note: Android 2.2 will always return either portrait or landscape, not reverse_portrait or reverse_landscape
    def get_activity_orientation
      perform_action("get_activity_orientation")
    end

    def query(uiquery, *args)
      converted_args = []
      args.each do |arg|
        if arg.is_a?(Hash) and arg.count == 1
          if arg.values.is_a?(Array) && arg.values.count == 1
            values = arg.values.flatten
          else
            values = [arg.values]
          end

          converted_args << {:method_name => arg.keys.first, :arguments => values}
        else
          converted_args << arg
        end
      end
      map(uiquery,:query,*converted_args)
    end

    def flash(query_string)
      map(query_string, :flash)
    end

    def each_item(opts={:query => "android.widget.ListView", :post_scroll => 0.2}, &block)
      uiquery = opts[:query] || "android.widget.ListView"
      skip_if = opts[:skip_if] || lambda { |i| false }
      stop_when = opts[:stop_when] || lambda { |i| false }
      check_element_exists(uiquery)
      num_items = query(opts[:query], :adapter, :count).first
      num_items.times do |item|
        next if skip_if.call(item)
        break if stop_when.call(item)

        scroll_to_row(opts[:query], item)
        sleep(opts[:post_scroll]) if opts[:post_scroll] and opts[:post_scroll] > 0
        yield(item)
      end
    end

    def set_date(query_string, year_or_datestring, month=nil, day=nil)
      wait_for_element_exists(query_string)

      if month.nil? && day.nil? && year_or_datestring.is_a?(String)
        date = Date.parse(year_or_datestring)
        set_date(query_string, date.year, date.month, date.day)
      else
        year = year_or_datestring
        query(query_string, updateDate: [year, month-1, day])
      end
    end

    def set_time(query_string, hour_or_timestring, minute=nil)
      wait_for_element_exists(query_string)

      if minute.nil? && hour_or_timestring.is_a?(String)
        time = Time.parse(hour_or_timestring)
        set_time(query_string, time.hour, time.min)
      else
        hour = hour_or_timestring
        query(query_string, setCurrentHour: hour)
        query(query_string, setCurrentMinute: minute)
      end
    end

    def classes(query_string, *args)
      query(query_string, :class, *args)
    end

    def ni
      raise "Not yet implemented."
    end

    ###

    ### simple page object helper

    def page(clz, *args)
      clz.new(self, *args)
    end

    ###

    ### app life cycle
    def connect_to_test_server
      puts "Explicit calls to connect_to_test_server should be removed."
      puts "Please take a look in your hooks file for calls to this methods."
      puts "(Hooks are stored in features/support)"
    end

    def disconnect_from_test_server
      puts "Explicit calls to disconnect_from_test_server should be removed."
      puts "Please take a look in your hooks file for calls to this methods."
      puts "(Hooks are stored in features/support)"
    end

    class Device
      attr_reader :app_path, :test_server_path, :serial, :server_port, :test_server_port

      def initialize(cucumber_world, serial, server_port, app_path, test_server_path, test_server_port = 7102)

        @cucumber_world = cucumber_world
        @serial = serial || default_serial
        @server_port = server_port || default_server_port
        @app_path = app_path
        @test_server_path = test_server_path
        @test_server_port = test_server_port

        forward_cmd = "#{adb_command} forward tcp:#{@server_port} tcp:#{@test_server_port}"
        log forward_cmd
        log `#{forward_cmd}`
      end

      def _sdk_version
        `#{adb_command} shell getprop ro.build.version.sdk`.to_i
      end

      def reinstall_apps
        uninstall_app(package_name(@app_path))
        uninstall_app(package_name(@test_server_path))
        install_app(@app_path)
        install_app(@test_server_path)
      end

      def reinstall_test_server
        uninstall_app(package_name(@test_server_path))
        install_app(@test_server_path)
      end

      @@installed_apps ||= {}

      def ensure_apps_installed
        apps = [@app_path, @test_server_path]

        apps.each do |app|
          package = package_name(app)
          md5 = Digest::MD5.file(File.expand_path(app))

          if !application_installed?(package) || (!@@installed_apps.keys.include?(package) || @@installed_apps[package] != md5)
            log "MD5 checksum for app '#{app}' (#{package}): #{md5}"
            uninstall_app(package)
            install_app(app)
            @@installed_apps[package] = md5
          end
        end
      end

      def install_app(app_path)
        if _sdk_version >= 23
          cmd = "#{adb_command} install -g -t \"#{app_path}\""
        else
          cmd = "#{adb_command} install -t \"#{app_path}\""
        end

        log "Installing: #{app_path}"
        result = `#{cmd}`
        log result
        pn = package_name(app_path)
        succeeded = `#{adb_command} shell pm list packages`.lines.map{|line| line.chomp.sub("package:", "")}.include?(pn)

        unless succeeded
          ::Cucumber.wants_to_quit = true
          raise "#{pn} did not get installed. Reason: '#{result.lines.last.chomp}'. Aborting!"
        end

        # Enable GPS location mocking on Android Marshmallow+
        if _sdk_version >= 23
          cmd = "#{adb_command} shell appops set #{package_name(app_path)} 58 allow"
          log("Enabling GPS mocking using '#{cmd}'")
          `#{cmd}`
        end

        true
      end

      def update_app(app_path)
        if _sdk_version >= 23
          cmd = "#{adb_command} install -r -g \"#{app_path}\""
        else
          cmd = "#{adb_command} install -r \"#{app_path}\""
        end

        log "Updating: #{app_path}"
        result = `#{cmd}`
        log "result: #{result}"
        succeeded = result.include?("Success")

        unless succeeded
          ::Cucumber.wants_to_quit = true
          pn = package_name(app_path)
          raise "#{pn} did not get updated. Aborting!"
        end
      end

      def uninstall_app(package_name)
        exists = application_installed?(package_name)
        
        if exists
          log "Uninstalling: #{package_name}"
          log `#{adb_command} uninstall #{package_name}`

          succeeded = !application_installed?(package_name)

          unless succeeded
            ::Cucumber.wants_to_quit = true
            raise "#{package_name} was not uninstalled. Aborting!"
          end
        else
          log "Package not installed: #{package_name}. Skipping uninstall."
        end
      end

      def application_installed?(package_name)
        (`#{adb_command} shell pm list packages`.lines.map{|line| line.chomp.sub("package:", "")}.include?(package_name))
      end

      def app_running?
        begin
          http("/ping") == "pong"
        rescue
          false
        end
      end

      def keyguard_enabled?
        dumpsys = `#{adb_command} shell dumpsys window windows`
        #If a line containing mCurrentFocus and Keyguard exists the keyguard is enabled
        dumpsys.lines.any? { |l| l.include?("mCurrentFocus") and l.include?("Keyguard")}
      end

      def perform_action(action, *arguments)
        log "Action: #{action} - Params: #{arguments.join(', ')}"

        params = {"command" => action, "arguments" => arguments}

        Timeout.timeout(300) do
          begin
            result = http("/", params, {:read_timeout => 350})
          rescue => e
            log "Error communicating with test server: #{e}"
            raise e
          end
          log "Result:'" + result.strip + "'"
          raise "Empty result from TestServer" if result.chomp.empty?
          result = JSON.parse(result)
          if not result["success"] then
            raise "Action '#{action}' unsuccessful: #{result["message"]}"
          end
          result
        end
      rescue Timeout::Error
        raise "Step timed out"
      end

      def http(path, data = {}, options = {})
        begin

          configure_http(@http, options)
          make_http_request(
              :method => :post,
              :body => data.to_json,
              :uri => url_for(path),
              :header => {"Content-Type" => "application/json;charset=utf-8"})

        rescue HTTPClient::TimeoutError,
            HTTPClient::KeepAliveDisconnected,
            Errno::ECONNREFUSED, Errno::ECONNRESET, Errno::ECONNABORTED,
            Errno::ETIMEDOUT => e
          log "It looks like your app is no longer running. \nIt could be because of a crash or because your test script shut it down."
          raise e
        end
      end

      def http_put(path, data = {}, options = {})
        begin

          configure_http(@http, options)
          make_http_request(
              :method => :put,
              :body => data,
              :uri => url_for(path),
              :header => {"Content-Type" => "application/octet-stream"})

        rescue HTTPClient::TimeoutError,
            HTTPClient::KeepAliveDisconnected,
            Errno::ECONNREFUSED, Errno::ECONNRESET, Errno::ECONNABORTED,
            Errno::ETIMEDOUT => e
          log "It looks like your app is no longer running. \nIt could be because of a crash or because your test script shut it down."
          raise e
        end
      end

      def set_http(http)
        @http = http
      end

      def url_for(method)
        url = URI.parse(ENV['DEVICE_ENDPOINT']|| "http://127.0.0.1:#{@server_port}")
        path = url.path
        if path.end_with? "/"
          path = "#{path}#{method}"
        else
          path = "#{path}/#{method}"
        end
        url.path = path
        url
      end



      def make_http_request(options)
        begin
          unless @http
            @http = init_request(options)
          end
          header = options[:header] || {}
          header["Content-Type"] = "application/json;charset=utf-8"
          options[:header] = header


          response = if options[:method] == :post
                       @http.post(options[:uri], options)
                     elsif options[:method] == :put
                       @http.put(options[:uri], options)
                     else
                       @http.get(options[:uri], options)
                     end
          raise Errno::ECONNREFUSED if response.status_code == 502
          response.body
        rescue => e
          if @http
            @http.reset_all
            @http=nil
          end
          raise e
        end
      end

      def init_request(options)
        http = HTTPClient.new
        configure_http(http, options)
      end

      def configure_http(http, options)
        return unless http
        http.connect_timeout = options[:open_timeout] || 15
        http.send_timeout = options[:send_timeout] || 15
        http.receive_timeout = options[:read_timeout] || 15
        if options.has_key?(:debug) && options[:debug]
          http.debug_dev= $stdout
        else
          if ENV['DEBUG_HTTP'] and (ENV['DEBUG_HTTP'] != '0')
            http.debug_dev = $stdout
          else
            http.debug_dev= nil
          end
        end
        http
      end

      def screenshot(options={:prefix => nil, :name => nil})
        prefix = options[:prefix] || ENV['SCREENSHOT_PATH'] || ""
        name = options[:name]

        if name.nil?
          name = "screenshot"
        else
          if File.extname(name).downcase == ".png"
            name = name.split(".png")[0]
          end
        end

        @@screenshot_count ||= 0
        path = "#{prefix}#{name}_#{@@screenshot_count}.png"

        if ENV["SCREENSHOT_VIA_USB"] == "false"
          begin
            res = http("/screenshot")
          rescue EOFError
            raise "Could not take screenshot. App is most likely not running anymore."
          end
          File.open(path, 'wb') do |f|
            f.write res
          end
        else
          screenshot_cmd = "java -jar \"#{File.join(File.dirname(__FILE__), 'lib', 'screenshotTaker.jar')}\" #{serial} \"#{path}\""
          log screenshot_cmd
          raise "Could not take screenshot" unless system(screenshot_cmd)
        end

        @@screenshot_count += 1
        path
      end

      def client_version
        Calabash::Android::VERSION
      end

      def server_version
        begin
          response = perform_action('version')
          raise 'Invalid response' unless response['success']
        rescue => e
          log("Could not contact server")
          log(e && e.backtrace && e.backtrace.join("\n"))
          raise "The server did not respond. Make sure the server is running."
        end

        response['message']
      end

      def adb_command
        "\"#{Calabash::Android::Dependencies.adb_path}\" -s #{serial}"
      end

      def default_serial
        devices = connected_devices
        log "connected_devices: #{devices}"
        raise "No connected devices" if devices.empty?
        raise "More than one device connected. Specify device serial using ADB_DEVICE_ARG" if devices.length > 1
        devices.first
      end

      def default_server_port
        require 'yaml'
        File.open(File.expand_path(server_port_configuration), File::RDWR|File::CREAT) do |f|
          f.flock(File::LOCK_EX)
          state = YAML::load(f) || {}
          ports = state['server_ports'] ||= {}
          return ports[serial] if ports.has_key?(serial)

          port = 34777
          port += 1 while ports.has_value?(port)
          ports[serial] = port

          f.rewind
          f.write(YAML::dump(state))
          f.truncate(f.pos)

          log "Persistently allocated port #{port} to #{serial}"
          return port
        end
      end

      def server_port_configuration
        File.expand_path(ENV['CALABASH_SERVER_PORTS'] || "~/.calabash.yaml")
      end

      def connected_devices
        # Run empty ADB command to remove eventual first-run messages
        `"#{Calabash::Android::Dependencies.adb_path}" devices`

        lines = `"#{Calabash::Android::Dependencies.adb_path}" devices`.split("\n")
        start_index = lines.index{ |x| x =~ /List of devices attached/ } + 1
        lines[start_index..-1].collect { |l| l.split("\t").first }
      end

      def wake_up
        wake_up_cmd = "#{adb_command} shell am start -a android.intent.action.MAIN -n #{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.WakeUp"
        log "Waking up device using:"
        log wake_up_cmd
        raise "Could not wake up the device" unless system(wake_up_cmd)

        Calabash::Android::Retry.retry :tries => 10, :interval => 1 do
          raise "Could not remove the keyguard" if keyguard_enabled?
        end
      end

      def clear_app_data
        unless application_installed?(package_name(@app_path))
          raise "Cannot clear data, application #{package_name(@app_path)} is not installed"
        end

        unless application_installed?(package_name(@test_server_path))
          raise "Cannot clear data, test-server #{package_name(@test_server_path)} is not installed"
        end

        cmd = "#{adb_command} shell am instrument #{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.ClearAppData2"
        raise "Could not clear data" unless system(cmd)

        # Wait for the cleanup activity to finish. This is a hard sleep for now
        sleep 2

        true
      end

      def pull(remote, local)
        cmd = "#{adb_command} pull #{remote} #{local}"
        raise "Could not pull #{remote} to #{local}" unless system(cmd)
      end

      def push(local, remote)
        cmd = "#{adb_command} push #{local} #{remote}"
        raise "Could not push #{local} to #{remote}" unless system(cmd)
      end

      def start_test_server_in_background(options={}, &block)
        raise "Will not start test server because of previous failures." if ::Cucumber.wants_to_quit

        if keyguard_enabled?
          wake_up
        end

        env_options = options.clone
        env_options.delete(:intent)

        env_options[:main_activity] ||= ENV['MAIN_ACTIVITY'] || 'null'
        env_options[:test_server_port] ||= @test_server_port
        env_options[:class] ||= "sh.calaba.instrumentationbackend.InstrumentationBackend"

        cmd_arr = [adb_command, "shell am instrument"]

        env_options.each_pair do |key, val|
          cmd_arr << "-e"
          cmd_arr << key.to_s
          cmd_arr << val.to_s
        end

        cmd_arr << "#{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner"

        cmd = cmd_arr.join(" ")

        log "Starting test server using:"
        log cmd
        raise "Could not execute command to start test server" unless system("#{cmd} 2>&1")

        Calabash::Android::Retry.retry :tries => 600, :interval => 0.1 do
          raise "App did not start see adb logcat for details" unless app_running?
        end

        begin
          Calabash::Android::Retry.retry :tries => 300, :interval => 0.1 do
            log "Checking if instrumentation backend is ready"

            log "Is app running? #{app_running?}"
            ready = http("/ready", {}, {:read_timeout => 1})
            if ready != "true"
              log "Instrumentation backend not yet ready"
              raise "Not ready"
            else
              log "Instrumentation backend is ready!"
            end
          end
        rescue => e

          msg = "Unable to make connection to Calabash Test Server at http://127.0.0.1:#{@server_port}/\n"
          msg << "Please check the logcat output for more info about what happened\n"
          raise msg
        end

        begin
          server_version = server_version()
        rescue
          msg = ["Unable to obtain Test Server version. "]
          msg << "Please run 'reinstall_test_server' to make sure you have the correct version"
          msg_s = msg.join("\n")
          log(msg_s)
          raise msg_s
        end

        client_version = client_version()

        if Calabash::Android::Environment.skip_version_check?
          log(%Q[
     Client version #{client_version}
Test-server version #{server_version}

])
          $stdout.flush
        else
          log "Checking client-server version match..."

          if server_version != client_version
             raise(%Q[
Calabash Client and Test-server version mismatch.

              Client version #{client_version}
         Test-server version #{server_version}
Expected Test-server version #{client_version}

Solution:

Run 'reinstall_test_server' to make sure you have the correct version

])
          else
            log("Client and server versions match (client: #{client_version}, server: #{server_version}). Proceeding...")
          end
        end

        block.call if block

        start_application(options[:intent])

        # What was Calabash tracking? Read this post for information
        # No private data (like ip addresses) were collected
        # https://github.com/calabash/calabash-android/issues/655
        #
        # Removing usage tracking to avoid problems with EU General Data
        # Protection Regulation which takes effect in 2018.
        # Calabash::Android::UsageTracker.new.post_usage_async
      end

      def start_application(intent)
        begin
          result = JSON.parse(http("/start-application", {intent: intent}, {read_timeout: 60}))
        rescue HTTPClient::ReceiveTimeoutError => e
          raise "Failed to start application. Starting took more than 60 seconds: #{e.class} - #{e.message}"
        end

        if result['outcome'] != 'SUCCESS'
          raise result['detail']
        end

        result['result']
      end

      def shutdown_test_server
        begin
          http("/kill")
          Timeout::timeout(3) do
            sleep 0.3 while app_running?
          end
        rescue HTTPClient::KeepAliveDisconnected
          log ("Server not responding. Moving on.")
        rescue Timeout::Error
          log ("Could not kill app. Waited to 3 seconds.")
        rescue EOFError
          log ("Could not kill app. App is most likely not running anymore.")
        end
      end

      ##location
      def set_gps_coordinates_from_location(location)
        require 'geocoder'
        results = Geocoder.search(location)
        raise "Got no results for #{location}" if results.empty?

        best_result = results.first
        set_gps_coordinates(best_result.latitude, best_result.longitude)
      end

      def set_gps_coordinates(latitude, longitude)
        perform_action('set_gps_coordinates', latitude, longitude)
      end

      def get_preferences(name)

        log "Get preferences: #{name}, app running? #{app_running?}"
        preferences = {}

        if app_running?
          json = perform_action('get_preferences', name);
        else

          logcat_id = get_logcat_id()
          cmd = "#{adb_command} shell am instrument -e logcat #{logcat_id} -e name \"#{name}\" #{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.GetPreferences"

          raise "Could not get preferences" unless system(cmd)

          logcat_cmd = get_logcat_cmd(logcat_id)
          logcat_output = `#{logcat_cmd}`

          json = get_json_from_logcat(logcat_output)

          raise "Could not get preferences" unless json != nil and json["success"]
        end

        # at this point we have valid json, coming from an action
        # or instrumentation, but we don't care, just parse
        if json["bonusInformation"].length > 0
          json["bonusInformation"].each do |item|
            json_item = JSON.parse(item)
            preferences[json_item["key"]] = json_item["value"]
          end
        end

        preferences
      end

      def set_preferences(name, hash)

        log "Set preferences: #{name}, #{hash}, app running? #{app_running?}"

        if app_running?
          perform_action('set_preferences', name, hash);
        else

          params = hash.map {|k,v| "-e \"#{k}\" \"#{v}\""}.join(" ")

          logcat_id = get_logcat_id()
          am_cmd = Shellwords.escape("am instrument -e logcat #{logcat_id} -e name \"#{name}\" #{params} #{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.SetPreferences")
          cmd = "#{adb_command} shell #{am_cmd}"

          raise "Could not set preferences" unless system(cmd)

          logcat_cmd = get_logcat_cmd(logcat_id)
          logcat_output = `#{logcat_cmd}`

          json = get_json_from_logcat(logcat_output)

          raise "Could not set preferences" unless json != nil and json["success"]
        end
      end

      def clear_preferences(name)

        log "Clear preferences: #{name}, app running? #{app_running?}"

        if app_running?
          perform_action('clear_preferences', name);
        else

          logcat_id = get_logcat_id()
          cmd = "#{adb_command} shell am instrument -e logcat #{logcat_id} -e name \"#{name}\" #{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.ClearPreferences"
          raise "Could not clear preferences" unless system(cmd)

          logcat_cmd = get_logcat_cmd(logcat_id)
          logcat_output = `#{logcat_cmd}`

          json = get_json_from_logcat(logcat_output)

          raise "Could not clear preferences" unless json != nil and json["success"]
        end
      end

      def get_json_from_logcat(logcat_output)

        logcat_output.split(/\r?\n/).each do |line|
          begin
            json = JSON.parse(line)
            return json
          rescue
            # nothing to do here, just discarding logcat rubbish
          end
        end

        return nil
      end

      def get_logcat_id()
        # we need a unique logcat tag so we can later
        # query the logcat output and filter out everything
        # but what we are interested in

        random = (0..10000).to_a.sample
        "#{Time.now.strftime("%s")}_#{random}"
      end

      def get_logcat_cmd(tag)
        # returns raw logcat output for our tag
        # filtering out everthing else

        "#{adb_command} logcat -d -v raw #{tag}:* *:S"
      end
    end

    def label(uiquery)
      ni
    end

    def screenshot_and_raise(e, options = nil)
      if options
        screenshot_embed options
      else
        screenshot_embed
      end
      raise e
    end

    def hide_soft_keyboard
      perform_action('hide_soft_keyboard')
    end

    def execute_uiquery(uiquery)
      if uiquery.instance_of? String
        elements = query(uiquery)

        return elements.first unless elements.empty?
      else
        elements = uiquery

        return elements.first if elements.instance_of?(Array)
        return elements if elements.instance_of?(Hash)
      end

      nil
    end

    def step_deprecated
      puts 'Warning: This predefined step is deprecated.'
    end

    def http(path, data = {}, options = {})
      default_device.http(path, data, options)
    end

    def http_put(path, data = {}, options = {})
      default_device.http_put(path, data, options)
    end

    # @return [String] The path of the uploaded file on the device
    def upload_file(file_path)
      name = File.basename(file_path)
      device_tmp_path = http_put('/add-file', File.binread(file_path))
      http('/move-cache-file-to-public', {from: device_tmp_path, name: name})
    end

    # @param [String] file_path Path of the file to load (.apk or .jar)
    # @param [Array<String>] classes A list of classes to load from the file
    def load_dylib(file_path, classes = [])
      uploaded_file = upload_file(file_path)
      http('/load-dylib', {path: uploaded_file, classes: classes})
    end

    def html(q)
      query(q).map {|e| e['html']}
    end

    def set_text(uiquery, txt)
      puts "set_text is deprecated. Use enter_text instead"
      enter_text(uiquery, txt)
    end

    def press_user_action_button(action_name=nil)
      wait_for_keyboard

      if action_name.nil?
        perform_action("press_user_action_button")
      else
        perform_action("press_user_action_button", action_name)
      end
    end

    def press_button(key)
      perform_action('press_key', key)
    end

    def press_back_button
      press_button('KEYCODE_BACK')
    end

    def press_menu_button
      press_button('KEYCODE_MENU')
    end

    def press_down_button
      press_button('KEYCODE_DPAD_DOWN')
    end

    def press_up_button
      press_button('KEYCODE_DPAD_UP')
    end

    def press_left_button
      press_button('KEYCODE_DPAD_LEFT')
    end

    def press_right_button
      press_button('KEYCODE_DPAD_RIGHT')
    end

    def press_enter_button
      press_button('KEYCODE_ENTER')
    end

    def select_options_menu_item(identifier, options={})
      press_menu_button
      tap_when_element_exists("DropDownListView * marked:'#{identifier}'", options)
    end

    def select_context_menu_item(view_uiquery, menu_item_query_string)
      long_press(view_uiquery)

      container_class = 'com.android.internal.view.menu.ListMenuItemView'
      wait_for_element_exists(container_class)

      combined_query_string = "#{container_class} descendant #{menu_item_query_string}"
      touch(combined_query_string)
    end

    def select_item_from_spinner(item_query_string, options={})
      spinner_query_string = options[:spinner] || "android.widget.AbsSpinner"
      direction = options[:direction] || :down
      count = query(spinner_query_string, :getCount).first
      scroll_view_query_string = options[:scroll_view] || "android.widget.AbsListView index:0"

      unless direction == :up || direction == :down
        raise "Invalid direction '#{direction}'. Only upwards and downwards scrolling is supported"
      end

      touch(spinner_query_string)

      change_direction = false

      wait_for({retry_frequency: 0}.merge(options)) do
        if query(item_query_string).empty?
          scroll(scroll_view_query_string, direction)

          if change_direction
            direction = direction == :up ? :down : :up
            change_direction = false
          else
            # Because getLastVisiblePosition returns the last element even though it is not visible,
            # we have to scroll one more time to make sure we do not change direction before the last
            # element is fully visible
            if direction == :down
              change_direction = true if query(scroll_view_query_string, :getLastVisiblePosition).first+1 == count
            elsif direction == :up
              change_direction = true if query(scroll_view_query_string, :getFirstVisiblePosition).first == 0
            end
          end

          false
        else
          true
        end
      end

      touch(item_query_string)
    end

    def swipe(query_string, options={})
      raise 'Swipe not implemented. Use flick or pan instead.'
    end

    def cell_swipe(options={})
      ni
    end

    def find_scrollable_view(options={})
      timeout = options[:timeout] || 30

      begin
        Timeout.timeout(timeout, WaitError) do
          scroll_view_query_string = "android.widget.ScrollView index:0"
          list_view_query_string = "android.widget.AbsListView index:0"
          web_view_query_string = "android.webkit.WebView index:0"

          loop do
            if element_exists(scroll_view_query_string)
              return scroll_view_query_string
            elsif element_exists(list_view_query_string)
              return list_view_query_string
            elsif element_exists(web_view_query_string)
              return web_view_query_string
            end
          end
        end
      rescue WaitError
        raise WaitError.new('Could not find any scrollable views')
      end
    end

    def scroll_up(options={})
      scroll(find_scrollable_view(options), :up)
    end

    def scroll_down(options={})
      scroll(find_scrollable_view(options), :down)
    end

    def scroll(query_string, direction)
      if direction != :up && direction != :down
        raise 'Only upwards and downwards scrolling is supported for now'
      end

      action = lambda do
        elements = query(query_string)
        raise "No elements found. Query: #{query_string}" if elements.empty?

        if elements.length > 1
          query_string = "#{query_string} index:0"
        end

        element = elements.first

        response = query(query_string, :getFirstVisiblePosition).first

        if response.is_a?(Hash) && response.has_key?("error") # View is not of type android.widget.AbsListView
          scroll_x = 0
          scroll_y = 0
          width = element['rect']['width']
          height = element['rect']['height']

          if direction == :up
            scroll_y = -height/2
          else
            scroll_y = height/2
          end

          query(query_string, {scrollBy: [scroll_x.to_i, scroll_y.to_i]})
        else # View is of type android.widget.AbsListView
          first_position = response.to_i
          last_position = query(query_string, :getLastVisiblePosition).first.to_i

          selection_index = if direction == :up
                              [first_position + [first_position - last_position + 1, -1].min, 0].max
                            elsif direction == :down
                              first_position + [last_position - first_position, 1].max
                            end

          query(query_string, setSelection: selection_index)
        end
      end

      when_element_exists(query_string, action: action)
    end

    def scroll_to(query_string, options={})
      options[:action] ||= lambda {}

      all_query_string = query_string

      unless all_query_string.chomp.downcase.start_with?('all')
        all_query_string = "all #{all_query_string}"
      end

      wait_for_element_exists(all_query_string)

      element = query(all_query_string).first
      raise "No elements found. Query: #{all_query_string}" if element.nil?
      element_y = element['rect']['y']
      element_height = element['rect']['height']
      element_bottom = element_y + element_height

      scroll_view_query_string = options[:container] || if element.has_key?('html')
                                                          "android.webkit.WebView id:'#{element['webView']}'"
                                                        else
                                                          "#{all_query_string} parent android.widget.ScrollView index:0"
                                                        end

      scroll_element = query(scroll_view_query_string).first

      raise "Could not find parent scroll view. Query: '#{escape_quotes(scroll_view_query_string)}'" if scroll_element.nil?

      scroll_element_y = scroll_element['rect']['y']
      scroll_element_height = scroll_element['rect']['height']

      if element_bottom > scroll_element_y + scroll_element_height
        scroll_by_y = element_bottom - (scroll_element_y + scroll_element_height)
      elsif element_y < scroll_element_y
        scroll_by_y = element_y - scroll_element_y
      else
        scroll_by_y = 0
      end

      if scroll_by_y != 0
        result = query(scroll_view_query_string, {scrollBy: [0, scroll_by_y]}).first
        raise 'Could not scroll parent view' if result != '<VOID>'
      end

      visibility_query_string = all_query_string[4..-1]
      when_element_exists(visibility_query_string, options)
    end

    def scroll_to_row(uiquery,number)
      query(uiquery, {:smoothScrollToPosition => number})
      puts "TODO:detect end of scroll - use sleep for now"
    end

    def rotate(dir)
      ni
    end

    def app_to_background(secs)
      ni
    end

    def element_does_not_exist(uiquery)
      query(uiquery).empty?
    end

    def element_exists(uiquery)
      not element_does_not_exist(uiquery)
    end

    def view_with_mark_exists(expected_mark)
      element_exists( "android.view.View marked:'#{expected_mark}'" )
    end

    def check_element_exists( query )
      if not element_exists( query )
        screenshot_and_raise "No element found for query: #{query}"
      end
    end

    def check_element_does_not_exist( query )
      if element_exists( query )
        screenshot_and_raise "Expected no elements to match query: #{query}"
      end
    end

    def check_view_with_mark_exists(expected_mark)
      check_element_exists( "view marked:'#{expected_mark}'" )
    end

    # a better name would be element_exists_and_is_not_hidden
    def element_is_not_hidden(uiquery)
      ni
    end


    def load_playback_data(recording,options={})
      ni
    end

    def playback(recording, options={})
      ni
    end

    def interpolate(recording, options={})
      ni
    end

    def record_begin
      ni
    end

    def record_end(file_name)
      ni
    end

    def evaluate_javascript(query_string, javascript, opt={})
      wait_for_elements_exist(query_string, {timeout: Calabash::Android::Defaults.query_timeout})
      result = JSON.parse(http("/map", {query: query_string, operation: {method_name: 'execute-javascript'}, javascript: javascript}))

      if result['outcome'] != 'SUCCESS' || result['results'].nil?
        parsed_result = result['results'].map {|r| "\"#{r}\","}.join("\n")
        raise "Could not evaluate javascript: \n#{parsed_result}"
      end

      result['results']
    end

    def backdoor(method_name, arguments = [], options={})
      arguments = [arguments] unless arguments.is_a?(Array)

      result = JSON.parse(http('/backdoor', {method_name: method_name, arguments: arguments}))

      if result['outcome'] != 'SUCCESS'
        raise result.to_s
      end

      result['result']
    end

    def map(query, method_name, *method_args)
      operation_map = {
          :method_name => method_name,
          :arguments => method_args
      }
      res = http("/map",
                 {:query => query, :operation => operation_map})
      res = JSON.parse(res)
      if res['outcome'] != 'SUCCESS'
        screenshot_and_raise "map #{query}, #{method_name} failed because: #{res['reason']}\n#{res['details']}"
      end

      res['results']
    end

    def url_for( method )
      default_device.url_for(method)
    end

    def make_http_request(options)
      default_device.make_http_request(options)
    end
  end


end end
