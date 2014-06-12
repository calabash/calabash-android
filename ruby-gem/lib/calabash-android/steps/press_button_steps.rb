Given /^I press the "([^\"]*)" button$/ do |button_text|
  tap_when_element_exists("android.widget.Button {text CONTAINS[c] '#{button_text}'}")
end

Then /^I press button number (\d+)$/ do |button_number|
  tap_when_element_exists("android.widget.Button index:#{button_number.to_i-1}")
end

Then /^I press image button number (\d+)$/ do |button_number|
  tap_when_element_exists("android.widget.ImageButton index:#{button_number.to_i-1}")
end

Then /^I press view with id "([^\"]*)"$/ do |view_id|
  tap_when_element_exists("* id:'#{view_id}'")
end

Then /^I press "([^\"]*)"$/ do |identifier|
  tap_when_element_exists("* marked:'#{identifier}'")
end

Then /^I click on screen (\d+)% from the left and (\d+)% from the top$/ do |x, y|
  perform_action('click_on_screen',x, y)
end

Then /^I touch the "([^\"]*)" text$/ do |text|
  tap_when_element_exists("* {text LIKE[c] '#{text}'}")
end

Then /^I press list item number (\d+)$/ do |line_index|
  step_deprecated

  tap_when_element_exists("android.widget.ListView index:0 android.widget.TextView index:#{line_index.to_i-1}")
end

Then /^I long press list item number (\d+)$/ do |line_index|
  step_deprecated

  long_press_when_element_exists("android.widget.ListView index:0 android.widget.TextView index:#{line_index.to_i-1}")
end
