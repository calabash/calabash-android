Then /^I long press "([^\"]*)" and select item number (\d+)$/ do |text, index|
  step_deprecated

  long_press_when_element_exists("* {text CONTAINS[c] '#{text}'}")
  tap_when_element_exists("com.android.internal.view.menu.ListMenuItemView android.widget.TextView index:#{index.to_i - 1}")
end

Then /^I long press "([^\"]*)" and select "([^\"]*)"$/ do |text, identifier|
  step_deprecated

  long_press_when_element_exists("* {text CONTAINS[c] '#{text}'}")
  tap_when_element_exists("com.android.internal.view.menu.ListMenuItemView android.widget.TextView marked:'#{identifier}'")
end

Then /^I long press "([^\"]*)"$/ do |text|
  long_press_when_element_exists("* {text CONTAINS[c] '#{text}'}")
end
