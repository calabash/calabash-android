def take_screenshot
  file_name = "#{ENV['SCREENSHOT_PATH_PREFIX']}screenshot_#{StepCounter.step_line}.png"
  $stdout.puts "Taking screenshoot to #{file_name} from device: #{ENV['ADB_DEVICE_ARG']}"
  system("java -jar #{File.dirname(__FILE__)}/screenShotTaker.jar #{file_name} '#{ENV['ADB_DEVICE_ARG']}'")
end