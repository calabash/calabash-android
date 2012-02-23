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
  $stderr.puts "#{Time.now} - Action: #{action} - Params: #{arguments.join(', ')}"

  action = {"command" => action, "arguments" => arguments}

  Timeout.timeout(300) do
    begin
      @@client.send(action.to_json + "\n", 0) #force_encoding('UTF-8') seems to be missing from JRuby
      result = @@client.readline
    rescue Exception => e
      $stderr.puts "#{Time.now} - error communicating with test server: #{e}"
      raise e
    end
    $stderr.puts "#{Time.now} - Result:'" + result + "'"
    raise "Empty result from TestServer" if result.chomp.empty?
    result = JSON.parse(result)
    raise result["message"].to_s unless result["success"]
    #Move along
  end
rescue Timeout::Error
  raise Exception, "#{Time.now} - Step timed out"
end


Before do |scenario|
  Step_index = 0
  Step_line = scenario.raw_steps[Step_index].line
end

AfterStep do |scenario|
  #Handle multiline steps
  Step_index = Step_index + 1
  Step_line = scenario.raw_steps[Step_index].line unless scenario.raw_steps[Step_index].nil?
end
