def take_screenshot
  require 'timeout'
  begin
    Timeout.timeout(30) do
      file_name = "#{ENV['SCREENSHOT_PATH_PREFIX']}screenshot_#{StepCounter.step_line}.png"
      log "Taking screenshoot to #{file_name} from device: #{ENV['ADB_DEVICE_ARG']}"
      system("java -jar #{File.dirname(__FILE__)}/screenShotTaker.jar #{file_name} #{ENV['ADB_DEVICE_ARG']}")
      log "Screenshot taken"
    end
  rescue Timeout::Error
    raise Exception, "take_screenshot timed out"
  end
end