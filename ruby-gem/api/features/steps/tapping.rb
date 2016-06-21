Given(/^any visible view$/) do
  pan_up_to_see("* id:'buttonGotoOrientations'", 5)
  @type = :native
end

When(/^Calabash is asked to tap it$/) do
  if type == :native
    touch("* id:'buttonGotoOrientations'")
  elsif type == :webview
    evaluate_javascript("webview", "document.getElementById('inputButton').addEventListener('click',
                                  function() {document.body.innerHTML = '<p>CLICKED</p>'})")
    touch("webview css:'#inputButton'")
  else
    raise "Unknown type '#{type}'"
  end
end

Then(/^it will perform a tap gesture on the coordinates of the view$/) do
  wait_for_element_exists("* id:'buttonGotoPortraitActivity'")
end

Given(/^any visible webview element$/) do
  goto_webview_editable_element
  @type = :webview
end

Then(/^it will perform a tap gesture on the coordinates of the element$/) do
  wait_for_element_exists("webview css:'*' {textContent LIKE 'CLICKED'}")
end

When(/^Calabash is asked to tap an element in the dialogue$/) do
  touch("* id:'button1'")
end

Then(/^it will perform a tap gesture on the coordinates of the view in the dialogue$/) do
  wait_for_element_does_not_exist("* id:'button1'")
end