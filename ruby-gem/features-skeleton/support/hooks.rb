
Before do |scenario|
  StepCounter.step_index = 0
  StepCounter.step_line = scenario.raw_steps[StepCounter.step_index].line
end

AfterStep do |scenario|
  #Handle multiline steps
  StepCounter.step_index = StepCounter.step_index + 1
  StepCounter.step_line = scenario.raw_steps[StepCounter.step_index].line unless scenario.raw_steps[StepCounter.step_index].nil?
end

at_exit do
  $stdout.puts "Hint: You can run cucumber directly using this command:"
  cucumber_cmd = "PACKAGE_NAME=#{ENV["PACKAGE_NAME"]} TEST_PACKAGE_NAME=#{ENV["TEST_PACKAGE_NAME"]} APP_PATH=#{ENV["APP_PATH"]} TEST_APP_PATH=#{ENV["TEST_APP_PATH"]} TEST_SERVER_PORT=#{ENV["TEST_SERVER_PORT"]} ADB_DEVICE_ARG=#{ENV["ADB_DEVICE_ARG"]} cucumber"
  $stdout.puts cucumber_cmd
  features = Dir["features/*.feature"]
  unless features.empty?
    $stdout.puts "To only run #{features.first}:"
    puts "#{cucumber_cmd} #{features.first}"
  end
end



StepCounter = Class.new
class << StepCounter
  @step_index = 0
  @step_line = 0
  attr_accessor :step_index, :step_line
end