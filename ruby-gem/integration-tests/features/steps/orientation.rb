
Given /^I see a view with orientation "(.*)"$/ do |orientation|
  scroll_to_push_button 'Orientations'
  sleep 1
  touch "button marked:'#{orientation}'"
end

Given /^the app should be shown in orientation "(.*)"$/ do |orientation|
  wait_for_elements_exist ["* marked:'This is #{orientation.downcase}'"]
  sleep 0.5

  # Calabash-Android bug: Cannot create screenshots with spaces in name
  # https://github.com/calabash/calabash-android/issues/398
  screenshot_embed name: "#{orientation}.png".gsub(' ', '_')
end
