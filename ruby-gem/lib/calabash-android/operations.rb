require 'httpclient'
require 'json'
require 'net/http'
require 'open-uri'
require 'rubygems'
require 'json'
require 'socket'
require 'timeout'
require 'calabash-android/helpers'
require 'calabash-android/wait_helpers'
require 'calabash-android/touch_helpers'
require 'calabash-android/version'
require 'calabash-android/env'
require 'retriable'
require 'cucumber'


module Calabash module Android

module Operations
  include Calabash::Android::WaitHelpers
  include Calabash::Android::TouchHelpers

  def current_activity
    `#{default_device.adb_command} shell dumpsys window windows`.each_line.grep(/mFocusedApp.+[\.\/]([^.\/\}]+)\}/){$1}.first
  end

  def log(message)
    $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}" if (ARGV.include? "-v" or ARGV.include? "--verbose")
  end

  def macro(txt)
    if self.respond_to?(:step)
      step(txt)
    else
      Then(txt)
    end
  end

  def default_device
    unless @default_device
      @default_device = Device.new(self, ENV["ADB_DEVICE_ARG"], ENV["TEST_SERVER_PORT"], ENV["APP_PATH"], ENV["TEST_APP_PATH"])
    end
    @default_device
  end

  def set_default_device(device)
    @default_device = device
  end

  def performAction(action, *arguments)
    default_device.perform_action(action, *arguments)
  end

  def reinstall_apps
    default_device.reinstall_apps
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

  def start_test_server_in_background(options={})
    default_device.start_test_server_in_background(options)
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

  def query(uiquery, *args)
    converted_args = []
    args.each do |arg|
      if arg.is_a?(Hash) and arg.count == 1
        converted_args << {:method_name => arg.keys.first, :arguments => [ arg.values.first ]}
      else
        converted_args << arg
      end
    end
    map(uiquery,:query,*converted_args)
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

    def reinstall_apps()
      uninstall_app(package_name(@app_path))
      install_app(@app_path)
      reinstall_test_server()
    end

    def reinstall_test_server()
      uninstall_app(package_name(@test_server_path))
      install_app(@test_server_path)
    end

    def install_app(app_path)
      cmd = "#{adb_command} install \"#{app_path}\""
      log "Installing: #{app_path}"
      result = `#{cmd}`
      log result
      pn = package_name(app_path)
      succeeded = `#{adb_command} shell pm list packages`.include?("package:#{pn}")

      unless succeeded
        ::Cucumber.wants_to_quit = true
        raise "#{pn} did not get installed. Aborting!"
      end
    end

    def update_app(app_path)
      cmd = "#{adb_command} install -r \"#{app_path}\""
      log "Updating: #{app_path}"
      result = `#{cmd}`
      log "result: #{result}"
      succeeded = result.include?("Success")

      unless succeeded
        ::Cucumber.wants_to_quit = true
        raise "#{pn} did not get updated. Aborting!"
      end
    end

    def uninstall_app(package_name)
      log "Uninstalling: #{package_name}"
      log `#{adb_command} uninstall #{package_name}`
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
        rescue Exception => e
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
      raise Exception, "Step timed out"
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
      body = nil
      begin
        unless @http
          @http = init_request(options)
        end
        header = options[:header] || {}
        header["Content-Type"] = "application/json;charset=utf-8"
        options[:header] = header

        if options[:method] == :post
          body = @http.post(options[:uri], options).body
        else
          body = @http.get(options[:uri], options).body
        end
      rescue Exception => e
        if @http
          @http.reset_all
          @http=nil
        end
        raise e
      end
      body
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
        screenshot_cmd = "java -jar #{File.join(File.dirname(__FILE__), 'lib', 'screenshotTaker.jar')} #{serial} #{path}"
        log screenshot_cmd
        raise "Could not take screenshot" unless system(screenshot_cmd)
      end

      @@screenshot_count += 1
      path
    end

    def adb_command
      "#{Env.adb_path} -s #{serial}"
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
      lines = `#{Env.adb_path} devices`.split("\n")
      lines.shift
      lines.collect { |l| l.split("\t").first}
    end

    def wake_up
      wake_up_cmd = "#{adb_command} shell am start -a android.intent.action.MAIN -n #{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.WakeUp"
      log "Waking up device using:"
      log wake_up_cmd
      raise "Could not wake up the device" unless system(wake_up_cmd)

      retriable :tries => 10, :interval => 1 do
        raise "Could not remove the keyguard" if keyguard_enabled?
      end
    end

    def clear_app_data
      cmd = "#{adb_command} shell am instrument #{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.ClearAppData"
      raise "Could not clear data" unless system(cmd)
    end

    def pull(remote, local)
      cmd = "#{adb_command} pull #{remote} #{local}"
      raise "Could not pull #{remote} to #{local}" unless system(cmd)
    end

    def push(local, remote)
      cmd = "#{adb_command} push #{local} #{remote}"
      raise "Could not push #{local} to #{remote}" unless system(cmd)
    end

    def start_test_server_in_background(options={})
      raise "Will not start test server because of previous failures." if ::Cucumber.wants_to_quit

      if keyguard_enabled?
        wake_up
      end

      env_options = {:target_package => package_name(@app_path),
                     :main_activity => main_activity(@app_path),
                     :test_server_port => @test_server_port,
                     :debug => false,
                     :class => "sh.calaba.instrumentationbackend.InstrumentationBackend"}

      env_options = env_options.merge(options)

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

      retriable :tries => 10, :interval => 1 do
        raise "App did not start" unless app_running?
      end

      begin
        retriable :tries => 10, :interval => 3 do
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
      rescue Exception => e

        msg = "Unable to make connection to Calabash Test Server at http://127.0.0.1:#{@server_port}/\n"
        msg << "Please check the logcat output for more info about what happened\n"
        raise msg
      end

      log "Checking client-server version match..."
      response = perform_action('version')
      unless response['success']
        msg = ["Unable to obtain Test Server version. "]
        msg << "Please run 'reinstall_test_server' to make sure you have the correct version"
        msg_s = msg.join("\n")
        log(msg_s)
        raise msg_s
      end
      unless response['message'] == Calabash::Android::VERSION

        msg = ["Calabash Client and Test-server version mismatch."]
        msg << "Client version #{Calabash::Android::VERSION}"
        msg << "Test-server version #{response['message']}"
        msg << "Expected Test-server version #{Calabash::Android::VERSION}"
        msg << "\n\nSolution:\n\n"
        msg << "Run 'reinstall_test_server' to make sure you have the correct version"
        msg_s = msg.join("\n")
        log(msg_s)
        raise msg_s
      end
      log("Client and server versions match. Proceeding...")


    end

    def shutdown_test_server
      begin
        http("/kill")
        Timeout::timeout(3) do
          sleep 0.3 while app_running?
        end
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
      raise Exception, "Got no results for #{location}" if results.empty?

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
        cmd = "#{adb_command} shell am instrument -e logcat #{logcat_id} -e name \"#{name}\" #{params} #{package_name(@test_server_path)}/sh.calaba.instrumentationbackend.SetPreferences"

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

  def screenshot_and_raise(msg, options = nil)
    if options
      screenshot_embed options
    else
      screenshot_embed
    end
    raise(msg)
  end

  def double_tap(uiquery, options = {})
    center_x, center_y = find_coordinate(uiquery)

    performAction("double_tap_coordinate", center_x, center_y)
  end

  def long_press(uiquery, options = {})
    center_x, center_y = find_coordinate(uiquery)

    performAction("long_press_coordinate", center_x, center_y)
  end

  def touch(uiquery, options = {})
    center_x, center_y = find_coordinate(uiquery)

    performAction("touch_coordinate", center_x, center_y)
  end

  def find_coordinate(uiquery)
    raise "Cannot find nil" unless uiquery

    if uiquery.instance_of? String
      elements = query(uiquery)
      raise "No elements found. Query: #{uiquery}" if elements.empty?
      element = elements.first
    else
      element = uiquery
      element = element.first if element.instance_of?(Array)
    end

    center_x = element["rect"]["center_x"]
    center_y = element["rect"]["center_y"]

    [center_x, center_y]
  end

  def http(path, data = {}, options = {})
    default_device.http(path, data, options)
  end

  def html(q)
    query(q).map {|e| e['html']}
  end

  def set_text(uiquery, txt)
    view,arguments = uiquery.split(" ",2)
    raise "Currently queries are only supported for webviews" unless view.downcase == "webview"

    if arguments =~ /(css|xpath):\s*(.*)/
      r = performAction("set_text", $1, $2, txt)
    else
     raise "Invalid query #{arguments}"
    end
  end


  def swipe(dir,options={})
      ni
  end

  def cell_swipe(options={})
    ni
  end

  def done
    ni
  end

  def scroll(uiquery,direction)
    ni
  end

  def scroll_to_row(uiquery,number)
    query(uiquery, {:smoothScrollToPosition => number})
    puts "TODO:detect end of scroll - use sleep for now"
  end

  def pinch(in_out,options={})
    ni
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

  def backdoor(sel, arg)
    ni
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
