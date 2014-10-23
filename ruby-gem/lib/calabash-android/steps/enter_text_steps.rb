Then /^I enter "([^\"]*)" as "([^\"]*)"$/ do |text, content_description|
  enter_text("android.widget.EditText {contentDescription LIKE[c] '#{content_description}'}", text)
end

Then /^I enter "([^\"]*)" into "([^\"]*)"$/ do |text, content_description|
  enter_text("android.widget.EditText {contentDescription LIKE[c] '#{content_description}'}", text)
end

Then /^I enter "([^\"]*)" into input field number (\d+)$/ do |text, index|
  enter_text("android.widget.EditText index:#{index.to_i-1}", text)
end

Then /^I enter text "([^\"]*)" into field with id "([^\"]*)"$/ do |text, id|
  enter_text("android.widget.EditText id:'#{id}'", text)
end

Then /^I clear "([^\"]*)"$/ do |identifier|
  clear_text_in("android.widget.EditText marked:'#{identifier}'}")
end

Then /^I clear input field number (\d+)$/ do |index|
  clear_text_in("android.widget.EditText index:#{index.to_i-1}")
end

Then /^I clear input field with id "([^\"]*)"$/ do |id|
  clear_text_in("android.widget.EditText id:'#{id}'")
end
