
Given /^the application is running/ do
  wait_for_elements_exist(["* marked:'XTC Android Sample'"])

  screenshot_embed
end

Then /^I can mock the location/ do
  if ['Amazon Kindle Fire HD 8.9 (2nd Gen) (4.0.4)', 'Amazon Kindle Fire (2nd Gen) (4.0.3)'].include?(ENV['XTC_DEVICE'])
    puts 'Skipping mock location on Amazon device, as it does not work anyway'
  else
    set_gps_coordinates(56.0, 10.0)
  end
end

When /^I set the date to "(\d\d-\d\d-\d\d\d\d)" and time to "(\d\d:\d\d)"$/ do |date, time|
  # Touch the date picker button in the main menu
  wait_for_elements_exist(["* marked:'Date Picker'"])

  # Verify that we are on the date picker activity
  touch(query("button marked:'Date Picker'").first)

  @date = date
  @time = time

  raise "Could not parse date: #{@date}" unless @date =~ /(\d\d)-(\d\d)-(\d\d\d\d)/
  day, month, year = $1.to_i, $2.to_i, $3.to_i

  raise "Could not parse time: #{@time}" unless @time =~ /(\d\d):(\d\d)/
  hour, minute = $1.to_i, $2.to_i

  scroll_to('datePicker')
  set_date('android.widget.DatePicker index:0', year, month, day)

  scroll_to('timePicker')
  set_time('android.widget.TimePicker index:0', hour, minute)

  screenshot_embed

  save
end

And /^I go to the views screen$/ do
  wait_for_elements_exist(["* marked:'XTC Android Sample'"])

  # Touch the views sample button in the main menu
  touch(query("button marked:'Views Sample'").first)

  # Verify that we are on the views sample activity
  wait_for_elements_exist(["* marked:'Views Activity'"])
end

And /^I select the "(.*?)" radio button$/ do |value|
  @radio_value = value

  scroll_to 'radioGroup'
  touch("radioButton text:'#{@radio_value}'")
end

And /^I (.*?) the checkbox$/ do |state|
  raise "State can only be check or uncheck" unless state == 'check' || state == 'uncheck'

  check = state == 'check'
  checked = query('checkBox').first['checked']

  @checked = check

  # Click the checkbox if the desired state is not the same as the current state
  touch("checkBox") if check ^ checked
end

And /^I press the first list item$/ do
  scroll_to 'listview'
  tap_when_element_exists('android.widget.ListView index:0 android.widget.TextView index:0')
end

And /^I enter some text into the first edit text$/ do
  enter_text("Føø! :)", "editText index:0")
end

And /^I set the seek bar to ([0-9]+) %$/ do |percentage|
  # This only works because the value of the seek bar is 100
  @seek_bar_value = percentage.to_i

  element = query('seekBar').first
  rect = element['rect']

  press_x = rect['x'].to_i + rect['width'].to_i * @seek_bar_value / 100
  press_y = rect['center_y'].to_i

  # No "touch_coordinate" method exists, so we need to call it through "performAction"
  perform_action('touch_coordinate', press_x, press_y)

  sleep 1

  screenshot_embed

  save
end

Then /^the displayed data should be correct$/ do
  wait_for_elements_exist(["* marked:'XTC Android Sample'"])

  # Touch the view data button in the main menu
  scroll_to("button marked:'View Data'")
  touch(query("button marked:'View Data'").first)

  # Verify that we are on the view data activity
  wait_for_elements_exist(["* marked:'View Data'"])

  element = query("textView id:'textViewRadio'").first
  raise 'Radio value not correct' unless element['text'] == @radio_value

  element = query("textView id:'textViewCheckBox'").first
  checked = element['text'] == 'T'
  raise 'Checkbox value not correct' unless checked == @checked

  element = query("textView id:'textViewSeekBar'").first
  value = element['text'].to_i
  raise "Seek bar value not correct. Expected #{@seek_bar_value} got #{value}" if (value - @seek_bar_value).abs > 2
end

Given /^I am on the web view screen$/ do
  # Wait for the application to load
  wait_for_elements_exist(["* marked:'XTC Android Sample'"])

  # Touch the web view button in the main menu
  touch(query("button marked:'Web View'").first)

  # Verify that we are on the web view
  wait_for_elements_exist(["* marked:'Webview Sample'"])

  screenshot_embed
end

When /^I submit the text "(.*?)"$/ do |input|
  enter_text("webView css:'#inputText'", input)
  touch(query("webView css:'#inputButton'"))

  sleep 2
end

Then /^the result should be "(.*?)"$/ do |input|
  result_element = (query "webView css:'#result'").first
  text_content = result_element['textContent']

  raise "The result is not #{input}" unless text_content == input
end

Given /^I am on the scroll screen$/ do
  # Wait for the application to load
  wait_for_elements_exist(["* marked:'XTC Android Sample'"])

  # Touch the scroll screen button in the main menu
  touch(query("button marked:'Scroll Screen'").first)

  # Verify that we are on the scroll screen
  wait_for_elements_exist(["* marked:'Scroll Activity'"])

  screenshot_embed
end

Then /^I should be able to scroll until I reach the bottom$/ do
  # Keep scrolling until we reach the text marked bottom text
  while query("textView marked:'Bottom Text'").empty? do
    scroll_down
    sleep 1
  end
end

Given /^I am on the swipe screen$/ do
  # Wait for the application to load
  wait_for_elements_exist(["* marked:'XTC Android Sample'"])

  # Touch the swipe screen button in the main menu
  touch(query("button marked:'Directional Swipe Measurer'").first)

  # Verify that we are on the swipe screen
  wait_for_elements_exist(["* marked:'Directional Swipe'"])

  screenshot_embed
end

Then /^I should be able to flick "(.*?)"$/ do |direction|
  case direction
    when 'left'
      flick_right
      expected_text = 'Right'
    when 'right'
      flick_left
      expected_text = 'Left'
    else
      raise 'Direction can only be left and right'
  end

  sleep 1
  output = query("textView id:'textViewHeader'", 'text').first

  raise 'Wrong swipe output' unless output == "#{expected_text} Swipe"
end

Given /^I am on the textfield screen$/ do
  selector = "button id:'buttonGotoTextfieldsView'"
  while query(selector).empty?
    scroll_down
  end
  touch selector
  wait_for_elements_exist "editText"
end

When /^I enter text into the (\d+).. field$/ do |id|
  selector = "editText index:#{id.to_i}"
  enter_text selector, 'Mjallo?'
end

Then /^I should still see text in field number (\d+')$/ do |id|
  selector = "editText index:#{id.to_i} text:'Mjallo?'"
  raise if query(selector).empty?
end

