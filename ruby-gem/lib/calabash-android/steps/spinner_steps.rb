Then /^I select "([^\"]*)" from "([^\"]*)"$/ do |item_text, spinner_identifier|
  spinner = query("android.widget.Spinner marked:'#{spinner_identifier}'").first

  raise 'Spinner not found' unless spinner

  if spinner
    touch(spinner)

    q = "android.widget.PopupWindow$PopupViewContainer android.widget.CheckedTextView marked:'#{item_text}'"

    wait_for_element_exists(q)
    touch(q)
  end
end