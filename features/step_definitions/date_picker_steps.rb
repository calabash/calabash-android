
Given /^I set the date to "(\d\d-\d\d-\d\d\d\d)" on DatePicker with index "([^\"]*)"$/ do |date, index|
  performAction('set_date_with_index', date, index)
end

Given /^I set the "([^\"]*)" date to "(\d\d-\d\d-\d\d\d\d)"$/ do |content_description, date|
  performAction('set_date_with_description', content_description, date)
end
