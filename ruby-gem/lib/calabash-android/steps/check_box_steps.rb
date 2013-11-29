Then /^I toggle checkbox number (\d+)$/ do |checkbox_number|
  checkbox_number = checkbox_number.to_i - 1
  checkboxes = query("android.widget.CheckBox")

  if checkbox_number >= checkboxes.count
    raise "Could not press CheckBox number #{checkbox_number}. Only #{checkboxes.count} was found"
  end
  touch(checkboxes[checkbox_number])
end
