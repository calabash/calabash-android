def take_screenshot
  file_name = "#{ENV['SCREENSHOT_PATH_PREFIX']}screenshot_#{Step_line}.png"
  $stdout.puts "Taking screenshoot to #{file_name} from device: #{ENV['DEVICE_SERIAL']}"
  system("java -jar #{File.dirname(__FILE__)}/screenShotTaker.jar #{file_name} #{ENV['DEVICE_SERIAL']}")
end