Then /^I press text of translated l10key (\d+)$/ do |l10key|
  performAction('press_l10n_element', l10key) 
end

Then /^I press button of translated l10key (\d+)$/ do |l10key|
  performAction('press_l10n_element', l10key,'button') 
end

Then /^I press menu item of translated l10key (\d+)$/ do |l10key|
  performAction('press_l10n_element', l10key,'menu_item') 
end

Then /^I press toggle button of translated l10key (\d+)$/ do |l10key|
  performAction('press_l10n_element', l10key,'toggle_button') 
end

Then /^I wait for the translated "([^\"]*)" l10nkey to appear$/ do |l10nkey|
  performAction('wait_for_l10n_element', l10nkey)
end