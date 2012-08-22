Then /^I see the text "([^\"]*)"$/ do |text|
  performAction('assert_text',text, true) 
end

Then /^I see "([^\"]*)"$/ do |text|
  performAction('assert_text', text, true) 
end

Then /^I should see "([^\"]*)"$/ do |text|
  performAction('assert_text', text, true) 
end

Then /^I should see text containing "([^\"]*)"$/ do |text|
  performAction('assert_text', text, true) 
end



Then /^I should not see "([^\"]*)"$/ do |text|
  performAction('assert_text', text, false) #second param indicated that the text should _not_ be found
end


Then /^I don't see the text "([^\"]*)"$/ do |text|  
  performAction('assert_text', text, false) #second param indicated that the text should _not_ be found
end

Then /^I don't see "([^\"]*)"$/ do |text|  
  performAction('assert_text', text, false) #second param indicated that the text should _not_ be found
end

# This step is more of an example or macro to be used within your own custom steps
# Generally, assert_view_property takes 3 args, but for if 'property'='compoundDrawables', the next arg should be 'left'/'right'/'top'/'bottom', followed by the expected drawable ID.
# @param view_id - the name of the view, eg: R.my_view_id
# @param property - eg: 'visibility' (visible/invisible/gone), 'drawable' (expected drawable ID) 
Then /^the view with id "([^\"]*)" should have property "([^\"]*)" = "([^\"]*)"$/ do | view_id, property, value |
  # get_view_property is also available: performAction( 'get_view_property', 'my_view', 'visibility') 
  performAction( 'assert_view_property', view_id, property, value )
end

Then /^the "([^\"]*)" activity should be open$/ do | expected_activity |
  actual_activity = performAction('get_activity_name')['message']
  raise "The current activity is #{actual_activity}" unless( actual_activity == expected_activity || actual_activity == expected_activity + 'Activity' )
end