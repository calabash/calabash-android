Then /^I go back$/ do
  press_back_button
end

Then /^I press the menu key$/ do
  press_menu_button
end

Then /^I press the enter button$/ do
  press_user_action_button
  # Or, possibly, press_enter_button
end


Then /^I swipe left$/ do
  perform_action('swipe', 'left')
end

Then /^I swipe right$/ do
  perform_action('swipe', 'right')
end

Then /^I select "([^\"]*)" from the menu$/ do |identifier|
  select_options_menu_item(identifier)
end

Then /^I select tab number (\d+)$/ do | tab |
  touch("android.widget.TabWidget descendant TextView index:#{tab.to_i-1}")
end

# @param - the "tag" associated with the tab, or the text within the tab label
Then /^I select the "([^\"]*)" tab$/ do | tab |
  touch("android.widget.TabWidget descendant TextView {text LIKE[c] '#{tab}'}")
end

Then /^I scroll down$/ do
  scroll_down
end

Then /^I scroll up$/ do
  scroll_up
end

Then /^I drag from (\d+):(\d+) to (\d+):(\d+) moving with (\d+) steps$/ do |from_x, from_y, to_x, to_y, steps|
  perform_action('drag', from_x, to_x, from_y, to_y, steps)
end
   
