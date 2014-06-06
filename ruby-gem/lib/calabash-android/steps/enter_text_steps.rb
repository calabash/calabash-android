Then /^I enter "([^\"]*)" as "([^\"]*)"$/ do |text, content_description|
  enter_text("EditText contentDescription:'#{content_description}'", text)
end

Then /^I enter "([^\"]*)" into "([^\"]*)"$/ do |text, content_description|
  enter_text("EditText {contentDescription LIKE[c] '#{content_description}'}", text)
end

Then /^I enter "([^\"]*)" into input field number (\d+)$/ do |text, index|
  enter_text("EditText index:#{index.to_i-1}", text)
end

Then /^I enter text "([^\"]*)" into field with id "([^\"]*)"$/ do |text, id|
  enter_text("EditText id:'#{id}'", text)
end

Then /^I clear "([^\"]*)"$/ do |name|
  query("EditText {contentDescription LIKE[c] '#{name}'}", setText: '')
end

Then /^I clear input field number (\d+)$/ do |number|
  query("EditText index:#{number.to_i-1}", setText: '')
end

Then /^I clear input field with id "([^\"]*)"$/ do |view_id|
  query("EditText id:'#{view_id}'", setText: '')
end
