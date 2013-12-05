Then /^I select "([^\"]*)" from "([^\"]*)"$/ do |item_text, spinner_content_description|
  performAction('select_item_from_named_spinner', spinner_content_description, item_text)
end

Then /^I select "([^\"]*)" from spinner with id "([^\"]*)"$/ do |text, spinner_view_id|
  performAction('select_item_from_id_spinner', spinner_view_id, text)
end