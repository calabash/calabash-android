Given /^I press the "([^\"]*)" button$/ do |button_text|
  touch("android.widget.Button {text CONTAINS '#{button_text}'}")
end

Then /^I press button number (\d+)$/ do |button_number|
  button_number = button_number.to_i - 1
  buttons = query("android.widget.Button")

  if button_number >= buttons.count
    raise "Could not press Button number #{button_number}. Only #{buttons.count} was found"
  end
  touch(buttons[button_number])
end

Then /^I press image button number (\d+)$/ do |button_number|
  button_number = button_number.to_i - 1
  image_buttons = query("android.widget.ImageButton")

  if button_number >= image_buttons.count
    raise "Could not press ImageButton number #{button_number}. Only #{image_buttons.count} was found"
  end
  touch(image_buttons[button_number])
end

Then /^I press view with id "([^\"]*)"$/ do |view_id|
  touch("* id:'#{view_id}'")
end

Then /^I press "([^\"]*)"$/ do |identifier|
  touch("* marked:'#{identifier}'")
end

Then /^I click on screen (\d+)% from the left and (\d+)% from the top$/ do |x, y|
  perform_action('click_on_screen',x, y)
end

Then /^I touch the "([^\"]*)" text$/ do |text|
  perform_action('click_on_text',text)
end

Then /^I press list item number (\d+)$/ do |line_index|
  perform_action('press_list_item', line_index, 0)
end

Then /^I long press list item number (\d+)$/ do |line_index|
  perform_action('long_press_list_item', line_index, 0)
end
