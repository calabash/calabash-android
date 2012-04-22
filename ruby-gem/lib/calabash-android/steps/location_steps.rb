Then /^I am in "([^\"]*)"$/ do |location|
  set_gps_coordinates_from_location(location)
end

Then /^I am at "([^\"]*)"$/ do |location|
  set_gps_coordinates_from_location(location)
end

Then /^I go to "([^\"]*)"$/ do |location|
  set_gps_coordinates_from_location(location)
end

Then /^I am at ([-+]?[0-9]*\.?[0-9]+), ([-+]?[0-9]*\.?[0-9]+)$/ do |latitude, longitude|
  set_gps_coordinates(latitude, longitude)
end

Then /^I go to ([-+]?[0-9]*\.?[0-9]+), ([-+]?[0-9]*\.?[0-9]+)$/ do |latitude, longitude|
  set_gps_coordinates(latitude, longitude)
end
