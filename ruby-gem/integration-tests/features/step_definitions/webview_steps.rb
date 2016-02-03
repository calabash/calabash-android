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