Then /^I press text of translated l10n?key "?([^\"]*)"?$/ do |l10nkey|
  perform_action('press_l10n_element', l10nkey)
end

Then /^I press button of translated l10n?key "?([^\"]*)"?$/ do |l10nkey|
  perform_action('press_l10n_element', l10nkey,'button')
end

Then /^I press menu item of translated l10n?key "?([^\"]*)"?$/ do |l10nkey|
  perform_action('press_l10n_element', l10nkey,'menu_item')
end

Then /^I press toggle button of translated l10n?key "?([^\"]*)"?$/ do |l10nkey|
  perform_action('press_l10n_element', l10nkey,'toggle_button')
end

Then /^I wait for the translated "?([^\"]*)"? l10n?key to appear$/ do |l10nkey|
  perform_action('wait_for_l10n_element', l10nkey)
end