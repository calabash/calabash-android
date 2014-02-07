Then /^I see the text "([^\"]*)"$/ do |text|
  assert_text(text)
end

Then /^I see "([^\"]*)"$/ do |text|
  assert_text(text)
end

Then /^I should see "([^\"]*)"$/ do |text|
  assert_text(text)
end

Then /^I should see text containing "([^\"]*)"$/ do |text|
  assert_text(text)
end



Then /^I should not see "([^\"]*)"$/ do |text|
  assert_text(text, false)
end

Then /^I don't see the text "([^\"]*)"$/ do |text|
  assert_text(text, false)
end

Then /^I don't see "([^\"]*)"$/ do |text|
  assert_text(text, false)
end
