Then /^I go back$/ do
  performAction('go_back')
end

Then /^I press the menu key$/ do
  performAction('press_menu')
end

Then /^I press the enter button$/ do
  performAction('send_key_enter')
end

Then /^I swipe left$/ do
  performAction('swipe', 'left')
end

Then /^I swipe right$/ do
  performAction('swipe', 'right')
end

Then /^I deep swipe left$/ do
    performAction('drag',1,99,50,50,5)
end

Then /^I deep swipe right$/ do
    performAction('drag',99,1,50,50,5)
end

Then /^I select "([^\"]*)" from the menu$/ do |item|  
  performAction('select_from_menu', item)
end

Then /^I select tab number (\d+)$/ do | tab |
  performAction('select_tab', tab)
end

# @param - the "tag" associated with the tab, or the text within the tab label
Then /^I select the "([^\"]*)" tab$/ do | tab |
  performAction('select_tab', tab)
end

Then /^I scroll down$/ do
  performAction('scroll_down')
end

Then /^I scroll up$/ do
  performAction('scroll_up')
end

Then /^I drag from (\d+):(\d+) to (\d+):(\d+) moving with (\d+) steps$/ do |fromX, fromY, toX, toY, steps|
  performAction('drag',fromX,toX,fromY,toY,steps)
end
   
