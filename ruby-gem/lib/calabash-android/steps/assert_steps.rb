Then /^I see the text "([^\"]*)"$/ do |text|
  perform_action('assert_text',text, true)
end

Then /^I see "([^\"]*)"$/ do |text|
  perform_action('assert_text', text, true)
end

Then /^I should see "([^\"]*)"$/ do |text|
  perform_action('assert_text', text, true)
end

Then /^I should see text containing "([^\"]*)"$/ do |text|
  perform_action('assert_text', text, true)
end



Then /^I should not see "([^\"]*)"$/ do |text|
  perform_action('assert_text', text, false) #second param indicated that the text should _not_ be found
end


Then /^I don't see the text "([^\"]*)"$/ do |text|  
  perform_action('assert_text', text, false) #second param indicated that the text should _not_ be found
end

Then /^I don't see "([^\"]*)"$/ do |text|  
  perform_action('assert_text', text, false) #second param indicated that the text should _not_ be found
end
