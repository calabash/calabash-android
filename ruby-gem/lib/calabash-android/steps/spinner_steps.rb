Then /^I select "([^\"]*)" from "([^\"]*)"$/ do |item_text, spinner_content_description|
  performAction('select_item_from_named_spinner', spinner_content_description, item_text)
end