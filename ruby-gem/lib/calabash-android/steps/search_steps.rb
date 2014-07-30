Then /^I enter "([^\"]*)" into search field$/ do |text|
  enter_text("android.widget.SearchView index:0", text)
end

Then /^I enter "([^\"]*)" into search field number (\d+)$/ do |text, number|
  enter_text("android.widget.SearchView index:#{number.to_i-1}", text)
end
