
Then /^I long press "([^\"]*)" and select item number "([^\"]*)"$/ do |text_to_press, index|
  perform_action('press_long_on_text_and_select_with_index', text_to_press, index)
end

Then /^I long press "([^\"]*)" and select "([^\"]*)"$/ do |text_to_press, context_text|
  perform_action('press_long_on_text_and_select_with_text', text_to_press, context_text)
end

Then /^I long press "([^\"]*)"$/ do |text_to_press|
  perform_action('press_long_on_text', text_to_press)
end
