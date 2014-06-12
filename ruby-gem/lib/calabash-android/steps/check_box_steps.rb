Then /^I toggle checkbox number (\d+)$/ do |index|
  tap_when_element_exists("android.widget.CheckBox index:#{index.to_i-1}")
end
