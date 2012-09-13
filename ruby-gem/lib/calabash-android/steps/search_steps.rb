Then /^I enter "([^\"]*)" into search field$/ do |text|
  performAction('enter_query_into_numbered_field', text, 1)
end

Then /^I enter "([^\"]*)" into search field number (\d+)$/ do |text, number|
  performAction('enter_query_into_numbered_field', text, number)
end
