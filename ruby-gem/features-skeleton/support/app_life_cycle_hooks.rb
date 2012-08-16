require 'calabash-android/management/adb'

Before do |scenario|
  # John Gallagher provided the "scenario_is_outline" fix: https://groups.google.com/forum/?fromgroups#!topic/calabash-ios/ICA4f24eSsY
  # ...there may be a better way of doing this...
  @scenario_is_outline = (scenario.class == Cucumber::Ast::OutlineTable::ExampleRow) 
  if @scenario_is_outline 
    scenario = scenario.scenario_outline
    # Still need to call connect_to_test_server...
  elsif scenario.failed?
    return #No need to start the server is anything before this has failed.
  end

  return if scenario.failed? #No need to start the server is anything before this has failed.
  start_test_server_in_background
end

After do
    shutdown_test_server
end
