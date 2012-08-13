When /^I centre the map at (-?\d+\.\d+), (-?\d+\.\d+)$/ do | lat, lon |
  performAction('set_map_center', lat, lon)
end

When /^(?:I )?set the zoom level to (\d+)$/ do | zoom |
  performAction('set_map_zoom', zoom)
end

When /^(?:I )?zoom (in|out) on the map$/ do | zoom |
  performAction('set_map_zoom', zoom)
end

When /^I press the map marker "([^\"]*)"$/ do | marker_title |
  performAction('press_map_marker_by_title', marker_title)
end

Then /^I should see the following markers:$/ do | marker_table |
  verify_markers( marker_table )
end

Then /^I should see the following (\d+) markers:$/ do | number_of_markers, marker_table |
  verify_n_markers( number_of_markers, marker_table )
end

