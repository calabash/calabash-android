Given /^I press the "([^\"]*)" button$/ do |buttonText|
  performAction('press_button_with_text', buttonText)
end

Then /^I press button number (\d+)$/ do |buttonNumber|
  performAction('press_button_number', buttonNumber) 
end

Then /^I press image button number (\d+)$/ do |buttonNumber|
  performAction('press_image_button_number', buttonNumber) 
end

Then /^I press view with id "([^\"]*)"$/ do |view_id|
  performAction('click_on_view_by_id',view_id)
end

Then /^I press "([^\"]*)"$/ do |identifier|
  performAction('press',identifier)
end

Then /^I click on screen (\d+)% from the left and (\d+)% from the top$/ do |x, y|
  performAction('click_on_screen',x, y)
end

Then /^I touch the "([^\"]*)" text$/ do |text|
  performAction('click_on_text',text)
end

Then /^I press list item number (\d+)$/ do |line_index|
  performAction('press_list_item', line_index, 0)
end

Then /^I long press list item number (\d+)$/ do |line_index|
  performAction('long_press_list_item', line_index, 0)
end
