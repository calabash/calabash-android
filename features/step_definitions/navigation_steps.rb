Then /^I go back$/ do
  performAction('go_back')
end

Then /^I press the menu key$/ do
  performAction('press_menu')
end

Then /^I swipe left$/ do
  performAction('swipe', 'left')
end

Then /^I swipe right$/ do
  performAction('swipe', 'right')
end

Then /^I select "([^\"]*)" from the menu$/ do |item|  
  performAction('select_from_menu', item)
end

Then /^I scroll down$/ do
  performAction('scroll_down')
end

Then /^I scroll up$/ do
  performAction('scroll_up')
end
