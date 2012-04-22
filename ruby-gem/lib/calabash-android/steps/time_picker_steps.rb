
Given /^I set the time to "(\d\d:\d\d)" on TimePicker with index "([^\"]*)"$/ do |time, index|
  performAction('set_time_with_index', time, index)
end

Given /^I set the "([^\"]*)" time to "(\d\d:\d\d)"$/ do |content_description, time|
  performAction('set_time_with_description', time, content_description)
end
