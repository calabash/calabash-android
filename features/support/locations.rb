def set_gps_coordinates_from_location(location)
  require 'geocoder'
  results = Geocoder.search(location)
  raise Exception, "Got no results for #{location}" if results.empty?
  
  best_result = results.first
  set_gps_coordinates(best_result.latitude, best_result.longitude)
end

def set_gps_coordinates(latitude, longitude)
  performAction('set_gps_coordinates', latitude, longitude) 
end
