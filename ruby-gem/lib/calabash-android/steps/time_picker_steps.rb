
Given /^I set the time to "(\d\d:\d\d)" on TimePicker with index "([^\"]*)"$/ do |time, index|
  perform_action('set_time_with_index', time, index)
end

Given /^I set the "([^\"]*)" time to "(\d\d:\d\d)"$/ do |content_description, time|
  perform_action('set_time_with_description', content_description, time)
end
