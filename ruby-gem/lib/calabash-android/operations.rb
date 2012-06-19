require 'json'
require 'rubygems'
require 'json'
require 'socket'
require 'timeout'


module Calabash module Android

module Operations


  def log(message)
    $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}" if (ARGV.include? "-v" or ARGV.include? "--verbose")
  end

  def take_screenshot
    path = ENV["SCREENSHOT_PATH_PREFIX"] || "results"
    FileUtils.mkdir_p path unless File.exist? path
    filename_prefix = FeatureNameMemory.feature_name.gsub(/\s+/, '_').downcase
    begin
      Timeout.timeout(30) do
        file_name = "#{path}/#{filename_prefix}_#{StepCounter.step_line}.png"
        log "Taking screenshoot to #{file_name} from device: #{ENV['ADB_DEVICE_ARG']}"
        system("java -jar #{File.dirname(__FILE__)}/lib/screenShotTaker.jar #{file_name} #{ENV['ADB_DEVICE_ARG']}")
        log "Screenshot stored in: #{file_name}"
      end
    rescue Timeout::Error
      raise Exception, "take_screenshot timed out"
    end
  end

  def macro(txt)
    if self.respond_to?:step
      step(txt)
    else
      Then(txt)
    end
  end

  def performAction(action, *arguments)
    log "Action: #{action} - Params: #{arguments.join(', ')}"

    action = {"command" => action, "arguments" => arguments}

    Timeout.timeout(300) do
      begin
        @@client.send(action.to_json + "\n", 0) #force_encoding('UTF-8') seems to be missing from JRuby
        result = @@client.readline
      rescue Exception => e
        log "Error communicating with test server: #{e}"
        raise e
      end
      log "Result:'" + result.strip + "'"
      raise "Empty result from TestServer" if result.chomp.empty?
      result = JSON.parse(result)
      if not result["success"] then
        take_screenshot
        raise result["message"].to_s
      end
      return result
    end
  rescue Timeout::Error
    raise Exception, "Step timed out"
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


  ##adb
  def adb_command
    if is_windows?
      %Q("#{ENV["ANDROID_HOME"]}\\platform-tools\\adb.exe" #{ENV["ADB_DEVICE_ARG"]})
    else
      %Q(#{ENV["ANDROID_HOME"]}/platform-tools/adb #{ENV["ADB_DEVICE_ARG"]})
    end
  end

  def is_windows?
     ENV["OS"] == "Windows_NT"
  end
  ###

  ### app life cycle
  def connect_to_test_server
    log `#{adb_command} forward tcp:#{ENV["TEST_SERVER_PORT"]} tcp:7101`

    end_time = Time.now + 60
    begin
      Timeout.timeout(10) do
        @@client = TCPSocket.open('127.0.0.1',ENV["TEST_SERVER_PORT"])
        @@client.send("Ping!\n",0)
        log "Got '#{@@client.readline.strip}' from testserver"
      end
    rescue Exception => e
      log "Got exception:#{e}. Retrying!"
      sleep(1)
      retry unless Time.now > end_time
    end
  end

  def disconnect_from_test_server
    log "Closing connection to test"
    @@client.close
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
    performAction('set_gps_coordinates', latitude, longitude)
  end



  def label(uiquery)
    ni
  end

  def screenshot_and_raise(msg)
    take_screenshot
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

  def screenshot
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
