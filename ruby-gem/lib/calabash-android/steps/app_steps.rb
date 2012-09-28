# -*- coding: utf-8 -*-
Given /^My app is running$/ do
#  rotate_phone(0)
end

Given /^my app is running$/ do
#  rotate_phone(0)
end

Then /^I repeat "([^\"]*)" action (\d+) times$/ do |action, num|
	num.to_i.times {step action}
end


