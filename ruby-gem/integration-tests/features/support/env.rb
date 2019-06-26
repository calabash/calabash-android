require 'calabash-android/color_helper'
require 'calabash-android/operations'

World(Calabash::Android::ColorHelper)
World(Calabash::Android::Operations)

ENV['TEST_APP_PATH'] = test_server_path(ENV['APP_PATH'])

# Pry is not allowed on the Xamarin Test Cloud.  This will force a validation
# error if you mistakenly submit a binding.pry to the Test Cloud.
if !ENV['XAMARIN_TEST_CLOUD']
   require 'pry'
   Pry.config.history.file = '.pry-history'
   require 'pry-nav'
end
