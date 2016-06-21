Given(/^I am in a webview with an iframe$/) do
  ensure_app_installed
  start_test_server_in_background
  touch("* marked:'Web View'")
  wait_for_element_exists("webview")
  query("webview", loadUrl: "https://cdn.rawgit.com/calabash/calabash-android/master/ruby-gem/integration-tests/features/support/data/page_with_iframe.html")
end

When(/^I search for elements inside an iframe$/) do
  wait_for_element_exists("all webview css:'iframe' index:0 css:'button'")
end

Then(/^I should be able to interact with them$/) do
  touch("webview css:'iframe' {nodeName LIKE[c] 'IFRAME'} css:'button'")
  sleep 1
  value = query("webview css:'iframe' css:'#result'").first['textContent']

  if value != 'Hello World'
    raise "Expected 'Hello World' got '#{value}'"
  end
end

Given(/^I have a webview available$/) do
  ensure_app_installed
  start_test_server_in_background
  touch("* marked:'Web View'")
  wait_for_element_exists("webview")
end

When(/^I evaluate bad javascript$/) do
  @error = nil

  begin
    evaluate_javascript("webview", "invalid.invalid")
  rescue => e
    @error = e
  end
end

Then(/^I should get an error with an javascript exception$/) do
  if @error.nil?
    raise 'No error was raised'
  else
    puts "error message: #{@error.message}"

    unless @error.message.include?('invalid')
      raise "Expected an error with a javascript exception. Got #{@error.message}"
    end
  end
end

And(/^it has no body$/) do
  evaluate_javascript("webview", "document.getElementsByTagName('html')[0].removeChild(document.body)")
  sleep 2
end

When(/^I query for elements$/) do
  @result = query("webview css:'*'")
end

Then(/^none should be returned$/) do
  unless @result.empty?
    raise "Results were returned! #{@result}"
  end
end