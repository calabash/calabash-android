Then /^I enter "([^\"]*)" as "([^\"]*)"$/ do |text, target|
  perform_action('enter_text_into_named_field', text, target)
end

Then /^I enter "([^\"]*)" into input field number (\d+)$/ do |text, number|
  perform_action('enter_text_into_numbered_field',text, number)
end

Then /^I enter "([^\"]*)" into "([^\"]*)"$/ do |text, name|
  perform_action('enter_text_into_named_field',text, name)
end

Then /^I clear "([^\"]*)"$/ do |name|
  query("EditText contentDescription:'#{name}'", setText: '')
end

Then /^I clear input field number (\d+)$/ do |number|
  query("EditText index:#{number.to_i-1}", setText: '')
end

Then /^I clear input field with id "([^\"]*)"$/ do |view_id|
  query("EditText id:'#{view_id}'", setText: '')
end

Then /^I enter text "([^\"]*)" into field with id "([^\"]*)"$/ do |text, view_id|
  perform_action('enter_text_into_id_field', text, view_id)
end
