require 'json'

def verify_markers( expected_marker_table )
  result = performAction('get_map_markers')
  actual_table = result['bonusInformation']
    
  actual_table.each_with_index do | marker_info, index |
    # eg: {"latitude":-12.345678, "longitude":123.456789, "title":"My Marker"}
    marker_info = JSON.parse( marker_info )
    actual_table[index] = marker_info
  end
    
  expected_marker_table.diff!(actual_table)
end


def verify_n_markers( number_of_markers, expected_marker_table )
  result = performAction('get_map_markers', number_of_markers)
  actual_table = result['bonusInformation']
    
  actual_table.each_with_index do | marker_info, index |
    marker_info = JSON.parse( marker_info )
    actual_table[index] = marker_info
  end

  expected_marker_table.diff!(actual_table)
end
