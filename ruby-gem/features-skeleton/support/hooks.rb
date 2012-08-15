
Before do |scenario|
  # https://groups.google.com/forum/?fromgroups#!topic/calabash-ios/ICA4f24eSsY
  @scenario_is_outline = (scenario.class == Cucumber::Ast::OutlineTable::ExampleRow) 
  if @scenario_is_outline 
    scenario = scenario.scenario_outline 
  end 

  StepCounter.step_index = 0
  # https://github.com/calabash/calabash-android/issues/58#issuecomment-6745642
  if scenario.respond_to? :raw_steps
    StepCounter.step_line = scenario.raw_steps[StepCounter.step_index].line
  else
    StepCounter.step_line = 0
  end
end

AfterStep do |scenario|
  #Handle multiline steps
  StepCounter.step_index = StepCounter.step_index + 1
  # https://github.com/calabash/calabash-android/issues/58#issuecomment-6745642
  if scenario.respond_to? :raw_steps
    StepCounter.step_line = scenario.raw_steps[StepCounter.step_index].line unless scenario.raw_steps[StepCounter.step_index].nil?
  else
    StepCounter.step_line = StepCounter.step_line + 1
  end
end

StepCounter = Class.new
class << StepCounter
  @step_index = 0
  @step_line = 0
  attr_accessor :step_index, :step_line
end