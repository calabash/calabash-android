Then /^I toggle checkbox number (\d+)$/ do |index|
  touch_when_element_exists("android.widget.CheckBox index:#{index.to_i-1}")
end
