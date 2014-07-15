
Given /^I set the time to "(\d\d:\d\d)" on TimePicker with index ([^\"]*)$/ do |time, index|
  set_time("android.widget.TimePicker index:#{index.to_i-1}", time)
end

Given /^I set the "([^\"]*)" time to "(\d\d:\d\d)"$/ do |content_description, time|
  set_time("android.widget.TimePicker {contentDescription LIKE[c] '#{content_description}'}", time)
end
