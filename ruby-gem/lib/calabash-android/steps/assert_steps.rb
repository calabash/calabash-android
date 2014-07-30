Then /^I see the text "([^\"]*)"$/ do |text|
  wait_for_text(text, timeout: 10)
end

Then /^I see "([^\"]*)"$/ do |text|
  wait_for_text(text, timeout: 10)
end

Then /^I should see "([^\"]*)"$/ do |text|
  wait_for_text(text, timeout: 10)
end

Then /^I should see text containing "([^\"]*)"$/ do |text|
  wait_for_text(text, timeout: 10)
end



Then /^I should not see "([^\"]*)"$/ do |text|
  wait_for_text_to_disappear(text, timeout: 10)
end

Then /^I don't see the text "([^\"]*)"$/ do |text|
  wait_for_text_to_disappear(text, timeout: 10)
end

Then /^I don't see "([^\"]*)"$/ do |text|
  wait_for_text_to_disappear(text, timeout: 10)
end
