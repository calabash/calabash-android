def take_screenshot
  require 'timeout'
  begin
    Timeout.timeout(30) do
      file_name = File.join(TestArtifactsMemory.feature_artifacts_dir, 'screenshots')
      FileUtils.mkdir_p file_name if !Dir.exists? file_name
      file_name = File.join(file_name, "screenshot_#{StepCounter.step_line}.png")
      log "Taking screenshot to #{file_name} from device: #{ENV['ADB_DEVICE_ARG']}"
      system("java -jar #{File.dirname(__FILE__)}/screenShotTaker.jar #{file_name} #{ENV['ADB_DEVICE_ARG']}")
      log "Screenshot taken"
    end
  rescue Timeout::Error
    raise Exception, "take_screenshot timed out"
  end
end
