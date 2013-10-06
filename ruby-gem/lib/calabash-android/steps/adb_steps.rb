
Then /^I see "([^\"]*)" in the ADB logs$/ do |text|
  raise "Did not see string #{text} in ADB logs" unless emitted_via_logcat(text)
end

Given /^I clear the ADB logs$/ do
  clear_logcat
end

Then /^I see "([^\"]*)" in the ADB logs with the tag "([^\"]*)"$/ do |text, tag|
  raise "Did not see string #{text} in ADB logs with tag #{tag}" unless emitted_via_logcat(text, tag)
end

Then /^I see "([^\"]*)" in the ADB logs at the "([^\"]*)" level $/ do |text, level|
  raise "Did not see string #{text} in ADB logs with tag #{tag}" unless emitted_via_logcat(text, level=level.upcase.chars.first)
end

Then /^I see "([^\"]*)" in the ADB logs with the tag "([^\"]*)" at the "([^\"]*)" level $/ do |text, tag, level|
  raise "Did not see string #{text} in ADB logs with tag #{tag} at log level #{level.upcase}" unless emitted_via_logcat(text, tag, level.upcase.chars.first)
end

Then /^I (?:have pushed|push) "([^\"]*)" to "([^\"]*)" on the device$/ do |src, dst|
	push(src, dst)
end

Then /^I only see logs with the tag "([^\"]*)" below the "([^\"]*)" level$/ do |tag, level|
	log_levels = get_levels_emitted_via_logcat(tag)

	level_ord = 'EWIDV'.index(level.upcase.chars.first)

	# see if we have any higher log levels?
	raise "Found higher log levels than #{level}:#{level_ord} in all levels: #{log_levels.keys.join(' ')}" unless (log_levels.keys.select { |key| log_levels[key] > level_ord }).empty?
end