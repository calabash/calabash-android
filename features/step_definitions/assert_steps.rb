Then /^I see the text "([^\"]*)"$/ do |text|
    performAction('assert_text',text, true) 
end

Then /^I see "([^\"]*)"$/ do |text|
    performAction('assert_text', text, true) 
end

Then /^I dont see the text "([^\"]*)"$/ do |text|  
  performAction('assert_text', text, false) #second param indicated that the text should _not_ be found
end

Then /^I dont see "([^\"]*)"$/ do |text|  
  performAction('assert_text', text, false) #second param indicated that the text should _not_ be found
end


