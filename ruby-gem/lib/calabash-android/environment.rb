module Calabash
  module Android
    # @!visibility private
    class Environment
      # @!visibility private
      class InvalidEnvironmentError < RuntimeError; end
      # @!visibility private
      class InvalidJavaSDKHome < RuntimeError; end

    # @!visibility private
      # Returns true if running on Windows
      def self.windows?
        RbConfig::CONFIG['host_os'][/mswin|mingw|cygwin/, 0] != nil
      end

    # @!visibility private
      # Returns the user home directory
      def self.user_home_directory
        if self.xtc?
          home = File.join("./", "tmp", "home")
          FileUtils.mkdir_p(home)
          home
        else
          if self.windows?
            # http://stackoverflow.com/questions/4190930/cross-platform-means-of-getting-users-home-directory-in-ruby
            home = ENV["HOME"] || ENV["USERPROFILE"]
          else
            require "etc"
            home = Etc.getpwuid.dir
          end

          unless File.exist?(home)
            home = File.join("./", "tmp", "home")
            FileUtils.mkdir_p(home)
          end

          home
        end
      end

      # @!visibility private
      # Returns true if the server / client version check can be skipped
      def self.skip_version_check?
        ENV["SKIP_VERSION_CHECK"] == "1"
      end

      # @!visibility private
      # Returns true if debugging is enabled.
      def self.debug?
        ENV['DEBUG'] == '1' ||
          ARGV.include?("-v") ||
          ARGV.include?("--verbose")
      end

      # @!visibility private
      # Returns true if we are running on the XTC
      def self.xtc?
        ENV['XAMARIN_TEST_CLOUD'] == '1'
      end

      # @!visibility private
      # Returns true if running in Jenkins CI
      #
      # Checks the value of JENKINS_HOME
      def self.jenkins?
        value = ENV["JENKINS_HOME"]
        !!value && value != ''
      end

      # @!visibility private
      # Returns true if running in Travis CI
      #
      # Checks the value of TRAVIS
      def self.travis?
        value = ENV["TRAVIS"]
        !!value && value != ''
      end

      # @!visibility private
      # Returns true if running in Circle CI
      #
      # Checks the value of CIRCLECI
      def self.circle_ci?
        value = ENV["CIRCLECI"]
        !!value && value != ''
      end

      # @!visibility private
      # Returns true if running in Teamcity
      #
      # Checks the value of TEAMCITY_PROJECT_NAME
      def self.teamcity?
        value = ENV["TEAMCITY_PROJECT_NAME"]
        !!value && value != ''
      end

      # @!visibility private
      # Returns true if running in Teamcity
      #
      # Checks the value of GITLAB_CI
      def self.gitlab?
        value = ENV["GITLAB_CI"]
        !!value && value != ''
      end

      # @!visibility private
      # Returns true if running in a CI environment
      def self.ci?
        [
          self.ci_var_defined?,
          self.travis?,
          self.jenkins?,
          self.circle_ci?,
          self.teamcity?,
          self.gitlab?
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

