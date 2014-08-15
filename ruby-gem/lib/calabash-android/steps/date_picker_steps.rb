
Given /^I set the date to "(\d\d-\d\d-\d\d\d\d)" on DatePicker with index ([^\"]*)$/ do |date, index|
  set_date("android.widget.DatePicker index:#{index.to_i-1}", date)
end

Given /^I set the "([^\"]*)" date to "(\d\d-\d\d-\d\d\d\d)"$/ do |content_description, date|
  set_date("android.widget.DatePicker {contentDescription LIKE[c] '#{content_description}'}", date)
end