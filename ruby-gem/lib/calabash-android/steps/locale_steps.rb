When(/^I change the language code to "(.*?)" and region code to "(.*?)"$/) do |language, region|
  perform_action('set_locale', language, region)
end

When(/^I change the language code to "(.*?)"$/) do |language|
  perform_action('set_locale', language)
end