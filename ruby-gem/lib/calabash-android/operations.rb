require 'json'
require 'net/http'
require 'open-uri'
require 'rubygems'
require 'json'
require 'socket'
require 'timeout'
require 'calabash-android/helpers'
require 'retriable'


module Calabash module Android

module Operations


  def log(message)
    $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}" if (ARGV.include? "-v" or ARGV.include? "--verbose")
  end

  def take_screenshot
    default_device.take_screenshot
  end

  def macro(txt)
    if self.respond_to?:step
      step(txt)
    else
      Then(txt)
    end
  end
  def default_device
    Device.default_device(self)
  end

  def shutdown_test_server
    default_device.shutdown_test_server
  end

  def performAction(action, *arguments)
    default_device.perform_action(action, *arguments)
  end

  def install_app(app_path)
    default_device.install_app(app_path)
  end

  def uninstall_apps
    default_device.uninstall_app(ENV["TEST_PACKAGE_NAME"])
    default_device.uninstall_app(ENV["PACKAGE_NAME"])
  end

  def start_test_server_in_background
    default_device.start_test_server_in_background()
  end

  def screenshot_embed(options={:prefix => nil, :name => nil, :label => nil})
    default_device.screenshot_embed(options)
  end

  def screenshot(options={:prefix => nil, :name => nil})
    default_device.screenshot(options)
  end

  def set_gps_coordinates_from_location(location)
    default_device.set_gps_coordinates_from_location(location)
  end

  def set_gps_coordinates(latitude, longitude)
    default_device.set_gps_coordinates(latitude, longitude)
  end

  def wait_for(timeout, &block)
    begin
      Timeout::timeout(timeout) do
        until block.call
          sleep 0.3
        end
      end
    rescue Exception => e
      take_screenshot
      raise e
    end
  end

  def query(uiquery, *args)
    raise "Currently queries are only supported for webviews" unless uiquery.start_with? "webView"

    uiquery.slice!(0, "webView".length)
    if uiquery =~ /(css|xpath):\s*(.*)/
      r = performAction("query", $1, $2)
      JSON.parse(r["message"])
    else
     raise "Invalid query #{uiquery}"
    end
  end

  def ni
    raise "Not yet implemented."
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
    @@default_device = nil

    def self.default_device(cucumber_world)
      unless @@default_device
        @@default_device = Device.new(cucumber_world, ENV["ADB_DEVICE_ARG"], ENV["TEST_SERVER_PORT"], ENV["APP_PATH"], ENV["TEST_APP_PATH"])
      end
      @@default_device
    end

    def make_default_device
      @@default_device = self
    end

    def initialize(cucumber_world, serial, server_port, app_path, test_server_path)
      @cucumber_world = cucumber_world
      @serial = serial
      @server_port = server_port
      @app_path = app_path
      @test_server_path = test_server_path

      puts "#{adb_command} forward tcp:b#{server_port} tcp:7102"
      log `#{adb_command} forward tcp:#{server_port} tcp:7102`
    end

    def reinstall_apps()
      uninstall_app(package_name(@app_path))
      install_app(@app_path)
      uninstall_app(package_name(@test_server_path))
      install_app(@test_server_path)
    end

    def install_app(app_path)
      cmd = "#{adb_command} install \"#{app_path}\""
      log "Installing: #{app_path}"
      result = `#{cmd}`
      if result.include? "Success"
        log "Success"
      else
        log "#Failure"
        log "'#{cmd}' said:"
        log result.strip
        raise "Could not install app #{app_path}: #{result.strip}"
      end
    end

    def uninstall_app(package_name)
      log "Uninstalling: #{package_name}"
      log `#{adb_command} uninstall #{package_name}`
    end

    def shutdown_test_server
      http("/kill")
    end

    def perform_action(action, *arguments)
      log "Action: #{action} - Params: #{arguments.join(', ')}"

      params = {"command" => action, "arguments" => arguments}

      Timeout.timeout(300) do
        begin
          result = http("/", params)
        rescue Exception => e
          log "Error communicating with test server: #{e}"
          raise e
        end
        log "Result:'" + result.strip + "'"
        raise "Empty result from TestServer" if result.chomp.empty?
        result = JSON.parse(result)
        if not result["success"] then
          screenshot_embed
          if result["bonusInformation"] && result["bonusInformation"].size > 0 && result["bonusInformation"][0].include?("Exception")
            log result["bonusInformation"][0]
          end
          raise "Step unsuccessful: #{result["message"]}"
        end
        return result
      end
    rescue Timeout::Error
      raise Exception, "Step timed out"
    end

    def http(path, data = {})
      retries = 0
      begin
        http = Net::HTTP.new "127.0.0.1", @server_port
        resp = http.post(path, "#{data.to_json}", {"Content-Type" => "application/json;charset=utf-8"})
        resp.body
      rescue Exception => e
        raise e if retries > 20
        sleep 0.5
        retries += 1
        retry
      end
    end

    def take_screenshot
      puts "take_screenshot is deprecated. Use screenshot_embed instead."
      path = ENV["SCREENSHOT_PATH_PREFIX"] || "results"
      FileUtils.mkdir_p path unless File.exist? path
      filename_prefix = FeatureNameMemory.feature_name.gsub(/\s+/, '_').downcase
      begin
        Timeout.timeout(30) do
          file_name = "#{path}/#{filename_prefix}_#{FeatureNameMemory.invocation}_#{StepCounter.step_line}.png"
          image = http("/screenshot")
          open(file_name ,"wb") { |file|
            file.write(image)
          }
          log "Screenshot stored in: #{file_name}!!!"
        end
      rescue Timeout::Error
        raise Exception, "take_screenshot timed out"
      end
    end

    def screenshot_embed(options={:prefix => nil, :name => nil, :label => nil})
      path = screenshot(options)
      @cucumber_world.embed(path, "image/png", options[:label] || File.basename(path))
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
      res = http("/screenshot")

      path = "#{prefix}#{name}_#{@@screenshot_count}.png"
      File.open(path, 'wb') do |f|
        f.write res
      end
      @@screenshot_count += 1
      path
    end

    def adb_command
      if is_windows?
        %Q("#{ENV["ANDROID_HOME"]}\\platform-tools\\adb.exe" #{device_args})
      else
        %Q(#{ENV["ANDROID_HOME"]}/platform-tools/adb #{device_args})
      end
    end

    def device_args
      if @serial
        "-s #{@serial}"
      else
        ""
      end
    end

    def start_test_server_in_background
      test_server_package = package_name(@test_server_path)
      cmd = "#{adb_command} shell am instrument -w -e class sh.calaba.instrumentationbackend.InstrumentationBackend #{test_server_package}/sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner"
      log "Starting test server using:"
      log cmd
      if is_windows?
        system(%Q(start /MIN cmd /C #{cmd}))
      else
        `#{cmd} 1>&2 &`
      end

      begin
        retriable :tries => 10, :interval => 3 do
            log "Checking if instrumentation backend is ready"
            ready = http("/ready")

            if ready != "true"
              log "Instrumentation backend not yet ready"
              raise "Not ready"
            else
              log "Instrumentation backend is ready!"
            end
        end

      rescue
        msg = "Unable to make connection to Calabash Test Server at http://127.0.0.1:#{@server_port}/\n"
        msg << "Please check the logcat output for more info about what happened\n"
        raise msg
      end
    end

    def log(message)
      $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}" if (ARGV.include? "-v" or ARGV.include? "--verbose")
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
  end



  def label(uiquery)
    ni
  end

  def screenshot_and_raise(msg)
    screenshot_embed
    sleep 5
    raise(msg)
  end

  def touch(uiquery,options={})
    ni
  end

  def html(q)
    query(q).map {|e| e['html']}
  end

  def set_text(uiquery, txt)
    raise "Currently queries are only supported for webviews" unless uiquery.start_with? "webView"

    uiquery.slice!(0, "webView".length)
    if uiquery =~ /(css|xpath):\s*(.*)/
      r = performAction("set_text", $1, $2, txt)
      JSON.parse(r["message"])
    else
     raise "Invalid query #{uiquery}"
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
    ni
  end

  def pinch(in_out,options={})
    ni
  end

  def rotate(dir)
    ni
  end

  def background(secs)
    ni
  end

  def element_exists(uiquery)
    !query(uiquery).empty?
  end

  def view_with_mark_exists(expected_mark)
    element_exists( "view marked:'#{expected_mark}'" )
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
  
  def map( query, method_name, *method_args )
    ni
  end

  def http(options, data=nil)
    ni
  end


  def url_for( verb )
    ni
  end

  def make_http_request( url, req )
    ni
  end

end


end end
