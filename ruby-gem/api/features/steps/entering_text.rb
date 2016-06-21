Given(/^an editable view$/) do
  goto_native_editable_view

  @type = :native
end

When(/^the user asks to enter text$/) do
  text_to_enter = 'Some sample text'

  if type == :native
    enter_text(native_editable_query, text_to_enter)
  elsif type == :webview
    enter_text(webview_editable_query, text_to_enter)
  else
    raise "Unknown type '#{type}'"
  end
end

Then(/^text is entered using the keyboard$/) do
  expected_text = 'Some sample text'

  if type == :native
    result = native_editable_text
  elsif type == :webview
    result = webview_editable_text
  else
    raise "Unknown type '#{type}'"
  end

  if result != expected_text
    raise "Text was not entered. Expected '#{expected_text}' got '#{result}'"
  end
end

Given(/^a webview with an editable field$/) do
  goto_webview_editable_element

  @type = :webview
end