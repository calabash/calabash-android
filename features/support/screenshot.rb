def take_screenshot
  require 'timeout'
  begin
    Timeout.timeout(10) do
      file_name = "#{ENV['SCREENSHOT_PATH_PREFIX']}screenshot_#{StepCounter.step_line}.png"
      $stdout.puts "Taking screenshoot to #{file_name} from device: #{ENV['ADB_DEVICE_ARG']}"
      cmd = "java -jar #{File.dirname(__FILE__)}/screenShotTaker.jar #{file_name} #{ENV['ADB_DEVICE_ARG']}"
      $stdout.puts cmd
      puts `#{cmd}`
    end
  rescue Timeout::Error
    raise Exception, "take_screenshot timed out"
  end
end