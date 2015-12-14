module Calabash
  module Android

    class Environment

      # Returns the user home directory
      def self.user_home_directory
        if self.xtc?
          home = File.join("./", "tmp", "home")
          FileUtils.mkdir_p(home)
          home
        else
          require 'etc'
          Etc.getpwuid.dir
        end
      end

      # Returns true if debugging is enabled.
      def self.debug?
        ENV['DEBUG'] == '1'
      end

      # Returns true if we are running on the XTC
      def self.xtc?
        ENV['XAMARIN_TEST_CLOUD'] == '1'
      end

      # Returns true if running in Jenkins CI
      #
      # Checks the value of JENKINS_HOME
      def self.jenkins?
        value = ENV["JENKINS_HOME"]
        !!value && value != ''
      end

      # Returns true if running in Travis CI
      #
      # Checks the value of TRAVIS
      def self.travis?
        value = ENV["TRAVIS"]
        !!value && value != ''
      end

      # Returns true if running in Circle CI
      #
      # Checks the value of CIRCLECI
      def self.circle_ci?
        value = ENV["CIRCLECI"]
        !!value && value != ''
      end

      # Returns true if running in Teamcity
      #
      # Checks the value of TEAMCITY_PROJECT_NAME
      def self.teamcity?
        value = ENV["TEAMCITY_PROJECT_NAME"]
        !!value && value != ''
      end

      # Returns true if running in a CI environment
      def self.ci?
        [
          self.ci_var_defined?,
          self.travis?,
          self.jenkins?,
          self.circle_ci?,
          self.teamcity?
        ].any?
      end

      private

      # !@visibility private
      def self.ci_var_defined?
        value = ENV["CI"]
        !!value && value != ''
      end
    end
  end
end

