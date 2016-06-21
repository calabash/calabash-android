def native_editable_query
  "* id:'editTextFirst'"
end

def webview_editable_query
  "webview css:'#inputText'"
end

def native_editable_text
  results = query(native_editable_query, :getText)

  if results.length == 0
    raise "View for query '#{native_editable_query}' not found"
  end

  results.first
end

def webview_editable_text
  results = evaluate_javascript("webview", "return document.getElementById('inputText').value")

  if results.length == 0
    raise "View for query '#{webview_editable_query}' not found"
  end

  results.first
end

def type
  @type
end

def goto_native_editable_view
  touch("* id:'buttonGotoViewsSample'")

  pan_up_to_see(native_editable_query, 5)
end

def goto_webview_editable_element
  touch("* id:'buttonGotoWebView'")
  wait_for_element_exists(webview_editable_query)
end

Then(/^the view is focused by tapping it$/) do
  # Intentionally left blank
end

Then(/^the webview element is focused by tapping it$/) do
  # Intentionally left blank
end