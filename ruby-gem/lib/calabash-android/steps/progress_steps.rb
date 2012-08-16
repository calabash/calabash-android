Then /^I wait for progress$/ do
  performAction('wait_for_no_progress_bars') 
end

Then /^I wait$/ do
  performAction('wait', 2)
end

Then /^I wait for dialog to close$/ do
  performAction('wait_for_dialog_to_close')
end


Then /^I wait for (\d+) seconds$/ do |seconds|
  performAction('wait', seconds)
end

Then /^I wait for 1 second$/ do
  performAction('wait', 1)
end

Then /^I wait for a second$/ do
  performAction('wait', 1)
end


Then /^I wait for "([^\"]*)" to appear$/ do |text|
  performAction('wait_for_text', text)
end

Then /^I wait up to (\d+) seconds for "([^\"]*)" to appear$/ do |timeout, text|
  performAction('wait_for_text', text, timeout)
end

Then /^I wait to see "([^\"]*)"$/ do |text|
  performAction('wait_for_text', text)
end

Then /^I wait up to (\d+) seconds to see "([^\"]*)"$/ do |timeout, text|
  performAction('wait_for_text', text, timeout)
end

Then /^I wait for the "([^\"]*)" button to appear$/ do |text|
  performAction('wait_for_button', text)
end

Then /^I wait for the view with id "([^\"]*)" to appear$/ do |text|
  performAction('wait_for_view_by_id', text)
end

Then /^I wait for the "([^\"]*)" view to appear$/ do |text|
  performAction('wait_for_view', text)
end


Then /^I wait for the "([^\"]*)" screen to appear$/ do |text|
    performAction('wait_for_screen', text)
end

Then /^I wait upto (\d+) seconds for the "([^\"]*)" screen to appear$/ do |timeout, text|
    performAction('wait_for_screen', text, timeout)
end

Then /^I wait up to (\d+) seconds for the "([^\"]*)" screen to appear$/ do |timeout, text|
    performAction('wait_for_screen', text, timeout)
end

# @param - the "tag" associated with the tab, or the text within the tab label
Then /^I wait for the "([^\"]*)" tab to appear$/ do | tab |
  performAction('wait_for_tab', tab)
end