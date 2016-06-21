module LocationMocking
  EXPECTED_LATITUDE = 22.47
  EXPECTED_LONGITUDE = -53.2
  EXPECTED_LATITUDE_TWICE = -9.3
  EXPECTED_LONGITUDE_TWICE = 10.92
end

def expect_coordinates(latitude, longitude)
  wait_for_element_exists("* id:'latitude' text:'#{latitude}'",
                          timeout_message: "Latitude was '#{query("* id:'latitude'", :getText).first}'. Expected #{latitude}")
  wait_for_element_exists("* id:'longitude' text:'#{longitude}'",
                          timeout_message: "Latitude was '#{query("* id:'longitude'", :getText).first}'. Expected #{longitude}")
end

Given(/^the application is listening for coordinates based on (\w+)$/) do |type|
  pan_up_to_see("* id:'buttonGotoLocation'", 5)
  touch("* id:'buttonGotoLocation'")

  if type == "GPS"
    touch("* id:'gps'")
  elsif type == "wifi"
    touch("* id:'wifi'")
  else
    raise "Unknown type '#{type}'"
  end
end

When(/^Calabash is asked to mock the location to certain coordinates$/) do
  set_gps_coordinates(LocationMocking::EXPECTED_LATITUDE, LocationMocking::EXPECTED_LONGITUDE)
end

Then(/^the application will believe the device is current at those coordinates$/) do
  expect_coordinates(LocationMocking::EXPECTED_LATITUDE, LocationMocking::EXPECTED_LONGITUDE)
end

When(/^Calabash is asked to mock the location again$/) do
  set_gps_coordinates(LocationMocking::EXPECTED_LATITUDE_TWICE, LocationMocking::EXPECTED_LONGITUDE_TWICE)
end

Then(/^the application will believe the device is current at the new coordinates$/) do
  expect_coordinates(LocationMocking::EXPECTED_LATITUDE_TWICE, LocationMocking::EXPECTED_LONGITUDE_TWICE)
end