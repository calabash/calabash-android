Then /^I toggle checkbox number (\d+)$/ do |checkboxNumber|
  performAction('toggle_numbered_checkbox', checkboxNumber) 
end
