When /^I centre the map at (-?\d+\.\d+), (-?\d+\.\d+)$/ do | lat, lon |
  perform_action('set_map_center', lat, lon)
end

When /^I pan the map to (-?\d+\.\d+), (-?\d+\.\d+)$/ do | lat, lon |
  perform_action('pan_map_to', lat, lon)
  sleep(1)
end

When /^(?:I )?set the map zoom level to (\d+)$/ do | zoom |
  perform_action('set_map_zoom', zoom)
  sleep(0.2)
end

When /^(?:I )?zoom (in|out) on the map$/ do | zoom |
  perform_action('set_map_zoom', zoom)
  sleep(0.2)
end

Then /^the map zoom level should be (\d+)$/ do | zoom |
  result = perform_action('get_map_zoom')
  raise StandardError.new( "The map's zoom level should be #{zoom} but is #{result['message']}"  ) unless zoom.eql?( result['message'] )
end

When /^I tap the map marker "([^\"]*)"$/ do | marker_title |
  perform_action('tap_map_marker_by_title', marker_title, 60000)
end

When /^I double tap the map marker "([^\"]*)"$/ do | marker_title |
  perform_action('tap_map_marker_by_title', marker_title, 60000)
  sleep(0.4)
  perform_action('tap_map_marker_by_title', marker_title, 100)
end

When /^I tap away from the markers$/ do
  perform_action('tap_map_away_from_markers')
end

Then /^I should see the following markers:$/ do | marker_table |
  verify_map_markers( marker_table )
end

Then /^the map should be centred at (-?\d+\.\d+), (-?\d+\.\d+)$/ do | lat, lon |
  result = perform_action('get_map_center')
  bonus_info = result['bonusInformation']
  actual_lat = bonus_info[0].to_f
  actual_lon = bonus_info[1].to_f
  lat = lat.to_f
  lon = lon.to_f
  tol = 0.00001
  if( (lat - actual_lat).abs > tol || (lon - actual_lon).abs > tol ) 
    raise StandardError.new( "The map should have been centred on: #{lat},#{lon} but was actually centred on #{bonus_info.inspect}" )
  end
end

Then /^the map marker "([^\"]*)" should be highlighted$/ do | marker_title |
  result = perform_action('get_map_marker', marker_title)
  result = result['message']
  result = JSON.parse( result )
  raise StandardError.new( "The marker '#{marker_title}' was found, but is not highlighted" ) unless result['focused']
end
