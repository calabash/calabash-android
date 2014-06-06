Then /^I enter "([^\"]*)" as "([^\"]*)"$/ do |text, content_description|
  enter_text("android.widget.EditText contentDescription:'#{content_description}'", text)
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

Then /^I clear "([^\"]*)"$/ do |name|
  clear_text("android.widget.EditText marked:'#{name}'}")
end

Then /^I clear input field number (\d+)$/ do |number|
  clear_text("android.widget.EditText index:#{number.to_i-1}")
end

Then /^I clear input field with id "([^\"]*)"$/ do |view_id|
  clear_text("android.widget.EditText id:'#{view_id}'")
end
