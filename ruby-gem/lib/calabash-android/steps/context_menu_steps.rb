Then /^I long press "([^\"]*)" and select item number (\d+)$/ do |text_to_press, index|
  long_press("* {text CONTAINS '#{text_to_press}'}")
  wait_for_element_exist("com.android.internal.view.menu.ListMenuItemView")

  touch_index = index.to_i - 1

  touch("com.android.internal.view.menu.ListMenuItemView android.widget.TextView index:#{touch_index}")
end

Then /^I long press "([^\"]*)" and select "([^\"]*)"$/ do |text_to_press, context_text|
  long_press("* {text CONTAINS '#{text_to_press}'}")
  wait_for_element_exist("com.android.internal.view.menu.ListMenuItemView")

  touch("com.android.internal.view.menu.ListMenuItemView android.widget.TextView marked:'#{context_text}'")
end

Then /^I long press "([^\"]*)"$/ do |text_to_press|
  long_press("* {text CONTAINS '#{text_to_press}'}")
end
