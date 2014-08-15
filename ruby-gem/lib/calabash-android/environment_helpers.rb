module Calabash
  module Android
    module EnvironmentHelpers
      # Are we running in the Xamarin Test Cloud?
      #
      # @return [Boolean] Returns true if cucumber is running in the test cloud.
      def xamarin_test_cloud?
        ENV['XAMARIN_TEST_CLOUD'] == '1'
      end
    end
  end
end