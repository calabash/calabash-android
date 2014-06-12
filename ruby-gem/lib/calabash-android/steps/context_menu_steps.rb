Then /^I long press "([^\"]*)" and select item number (\d+)$/ do |text_to_press, index|
  puts 'Warning: This predefined step is deprecated. Implement a new, less vague step.'

  long_press_when_element_exists("* {text CONTAINS '#{text_to_press}'}")

  touch_index = index.to_i - 1

  touch_when_element_exists("com.android.internal.view.menu.ListMenuItemView android.widget.TextView index:#{touch_index}")
end

Then /^I long press "([^\"]*)" and select "([^\"]*)"$/ do |text_to_press, context_text|
  puts 'Warning: This predefined step is deprecated. Implement a new, less vague step.'

  long_press_when_element_exists("* {text CONTAINS '#{text_to_press}'}")
  touch_when_element_exists("com.android.internal.view.menu.ListMenuItemView android.widget.TextView marked:'#{context_text}'")
end

Then /^I long press "([^\"]*)"$/ do |text_to_press|
  long_press_when_element_exists("* {text CONTAINS '#{text_to_press}'}")
end
