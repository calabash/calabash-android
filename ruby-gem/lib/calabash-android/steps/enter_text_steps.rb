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
  perform_action('clear_named_field',name)
end

Then /^I clear input field number (\d+)$/ do |number|
  perform_action('clear_numbered_field',number)
end

Then /^I clear input field with id "([^\"]*)"$/ do |view_id|
  perform_action('clear_id_field', view_id)
end

Then /^I enter text "([^\"]*)" into field with id "([^\"]*)"$/ do |text, view_id|
  perform_action('enter_text_into_id_field', text, view_id)
end
