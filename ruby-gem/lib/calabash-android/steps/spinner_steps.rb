Then /^I select "([^\"]*)" from "([^\"]*)"$/ do |item_text, spinner_identifier|
  spinner = query("android.widget.Spinner marked:'#{spinner_identifier}'")

  if spinner.empty?
    touch_when_element_exists("android.widget.Spinner * marked:'#{spinner_identifier}'")
  else
    touch(spinner)
  end

  touch_when_element_exists("android.widget.PopupWindow$PopupViewContainer * marked:'#{item_text}'")
end