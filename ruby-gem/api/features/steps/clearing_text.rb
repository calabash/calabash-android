Given(/^an editable view with text in it$/) do
  goto_native_editable_view

  results = query(native_editable_query, setText: "Some sample text")
  
  if results.length == 0
    raise "View for query '#{native_editable_query}' not found"
  end

  @type = :native
end

When(/^the user asks to clear the text of the view$/) do
  clear_text_in(native_editable_query)
end

Then(/^the text is cleared using selection and the keyboard$/) do
  if type == :native
    result = native_editable_text
  elsif type == :webview
    result = webview_editable_text
  else
    raise "Unknown type '#{type}'"
  end

  if result != ""
    raise "Text was not cleared. Expected '' got '#{result}'"
  end
end

Given(/^a webview with an editable element with text in it$/) do
  goto_webview_editable_element
  evaluate_javascript("webview", "document.getElementById('inputText').value = 'Some sample text'")

  @type = :webview
end

When(/^the user asks to clear the text of the element$/) do
  clear_text_in(webview_editable_query)
end