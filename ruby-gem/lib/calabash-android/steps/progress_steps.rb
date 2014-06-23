Then /^I wait for progress$/ do
  wait_for_element_does_not_exist("android.widget.ProgressBar")
end

Then /^I wait$/ do
  sleep 2
end


Then /^I wait for (\d+) seconds$/ do |seconds|
  sleep(seconds.to_i)
end

Then /^I wait for 1 second$/ do
  sleep 1
end

Then /^I wait for a second$/ do
  sleep 1
end

Then /^I wait for "([^\"]*)" to appear$/ do |text|
  wait_for_text(text)
end

Then /^I wait up to (\d+) seconds for "([^\"]*)" to appear$/ do |timeout, text|
  wait_for_text(text, timeout: timeout.to_i)
end

Then /^I wait to see "([^\"]*)"$/ do |text|
  wait_for_text(text)
end

Then /^I wait up to (\d+) seconds to see "([^\"]*)"$/ do |timeout, text|
  wait_for_text(text, timeout: timeout.to_i)
end

Then /^I wait for the "([^\"]*)" button to appear$/ do |identifier|
  wait_for_element_exists("android.widget.Button marked:'#{identifier}'");
end

Then /^I wait for the view with id "([^\"]*)" to appear$/ do |id|
  wait_for_element_exists("* id:'#{id}'")
end

Then /^I wait for the "([^\"]*)" view to appear$/ do |text|
  wait_for_element_exists("* marked:'#{text}'")
end


Then /^I wait for the "([^\"]*)" screen to appear$/ do |activity_name|
  wait_for_activity(activity_name)
end

Then /^I wait upto (\d+) seconds for the "([^\"]*)" screen to appear$/ do |timeout, activity_name|
  wait_for_activity(activity_name, timeout: timeout.to_i)
end

Then /^I wait up to (\d+) seconds for the "([^\"]*)" screen to appear$/ do |timeout, activity_name|
  wait_for_activity(activity_name, timeout: timeout.to_i)
end

# @param - the "tag" associated with the tab, or the text within the tab label
Then /^I wait for the "([^\"]*)" tab to appear$/ do |tab|
  wait_for do
    query("android.widget.TabWidget descendant TextView {text LIKE[c] '#{tab}'}", :isSelected).first
  end
end