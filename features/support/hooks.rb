# -*- coding: utf-8 -*-
require 'rubygems'
require 'json'
require 'socket'
require 'timeout'


def macro(txt)
  if self.respond_to? :step
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
  end
rescue Timeout::Error
  raise Exception, "Step timed out"
end


Before do |scenario|
  StepCounter.step_index = 0
  StepCounter.step_line = scenario.raw_steps[StepCounter.step_index].line
end

AfterStep do |scenario|
  #Handle multiline steps
  StepCounter.step_index = StepCounter.step_index + 1
  StepCounter.step_line = scenario.raw_steps[StepCounter.step_index].line unless scenario.raw_steps[StepCounter.step_index].nil?
end

StepCounter = Class.new
class << StepCounter
  @step_index = 0
  @step_line = 0
  attr_accessor :step_index, :step_line
end