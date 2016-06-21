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
  @skip_location = false

  # Cannot mock locations on this particular device
  if (ENV["XTC_DEVICE_NAME"] || "").include?("Kindle Fire HD 8.9")
    $stdout.puts "Skipping as device is kindle fire HD 8.9. '#{ENV["XTC_DEVICE_NAME"]}'"
    @skip_location = true
  end

  pan_up_to_see("* id:'buttonGotoLocation'", 5)
  touch("* id:'buttonGotoLocation'")

  if type == "GPS"
    unless query("* index:10", :getContext, :getPackageManager, hasSystemFeature: ["android.hardware.location.gps"]).first
      @skip_location = true
    end

    touch("* id:'gps'")
  elsif type == "wifi"
    unless query("* index:10", :getContext, :getPackageManager, hasSystemFeature: ["android.hardware.location.network"]).first
      @skip_location = true
    end

    touch("* id:'wifi'")
  else
    raise "Unknown type '#{type}'"
  end

  if @skip_location
    $stdout.puts "Test is skipped"
    query("android.widget.TextView", setText: ["Skipped"])
  end
end

When(/^Calabash is asked to mock the location to certain coordinates$/) do
  unless @skip_location
    set_gps_coordinates(LocationMocking::EXPECTED_LATITUDE, LocationMocking::EXPECTED_LONGITUDE)
  end
end

Then(/^the application will believe the device is current at those coordinates$/) do
  unless @skip_location
    expect_coordinates(LocationMocking::EXPECTED_LATITUDE, LocationMocking::EXPECTED_LONGITUDE)
  end
end

When(/^Calabash is asked to mock the location again$/) do
  unless @skip_location
    set_gps_coordinates(LocationMocking::EXPECTED_LATITUDE_TWICE, LocationMocking::EXPECTED_LONGITUDE_TWICE)
  end
end

Then(/^the application will believe the device is current at the new coordinates$/) do
  unless @skip_location
    expect_coordinates(LocationMocking::EXPECTED_LATITUDE_TWICE, LocationMocking::EXPECTED_LONGITUDE_TWICE)
  end
end