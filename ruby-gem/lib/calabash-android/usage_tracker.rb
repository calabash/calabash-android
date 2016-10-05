module Calabash
  module Android
    class UsageTracker

      require "httpclient"

      # @!visibility private
      @@track_usage = true

      # @!visibility private
      def self.enable_usage_tracking
        @@track_usage = true
      end

      # @!visibility private
      def self.disable_usage_tracking
        @@track_usage = false
      end

      # @!visibility private
      def post_usage
        if Calabash::Android::UsageTracker.track_usage? &&
            info_we_are_allowed_to_track != "none"
          begin
            HTTPClient.post(ROUTE, info)
          rescue => e
            message = %Q{ERROR: Could not post usage tracking information:#{$-0}#{e}}
            Calabash::Android::Logging.log_to_file(message)
          end
        end
      end

      # @!visibility private
      def post_usage_async
        t = Thread.new do
          post_usage
        end

        m = Thread.current

        Thread.new do
          loop do
            unless t.alive?
              break
            end

            unless m.alive?
              t.kill
              break
            end
          end
        end
        nil
      end

      private

      # @!visibility private
      def preferences
        Calabash::Android::Preferences.new
      end

      # @!visibility private
      def user_id
        preferences.user_id
      end

      # @!visibility private
      def info_we_are_allowed_to_track
        preferences.usage_tracking
      end

      # @!visibility private
      def self.track_usage?
        @@track_usage && !self.xtc?
      end

      # @!visibility private
      def self.xtc?
        ENV["XAMARIN_TEST_CLOUD"] == "1"
      end

      # @!visibility private
      DATA_VERSION = "1.1"

      # @!visibility private
      WINDOWS = "Windows"

      # @!visibility private
      OSX = "Darwin"

      # @!visibility private
      CALABASH_IOS = "iOS"

      # @!visibility private
      CALABASH_ANDROID = "Android"

      # @!visibility private
      ROUTE = "http://calabash-ci.macminicolo.net:56789/logEvent"

      # @!visibility private
      def host_os
        @host_os ||= lambda do
          if RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/
            WINDOWS
          else
            `uname -s`.chomp
          end
        end.call
      end

      # @!visibility private
      def host_os_version
        @host_os_version ||= lambda do
          if host_os == WINDOWS
            `ver`.chomp
          elsif host_os == OSX
            `sw_vers -productVersion`.chomp
          else
            `uname -r`.chomp
          end
        end.call
      end

      # @!visibility private
      def irb?
        $0 == "irb"
      end

      # @!visibility private
      def ruby_version
        @ruby_version ||= `#{RbConfig.ruby} -v`.chomp
      end

      # @!visibility private
      def used_bundle_exec?
        Object.const_defined?(:Bundler)
      end

      # @!visibility private
      def used_cucumber?
        Object.const_defined?(:Cucumber)
      end

      # @!visibility private
      #
      # Collect a hash of usage info.
      def info

        allowed = info_we_are_allowed_to_track

        if allowed == "none"
          raise RuntimeError,
            "This method should not be called if the user does not want to be tracked."
        end

        # Events only
        hash = {
          :event_name => "session",
          :data_version => DATA_VERSION,
          :distinct_id => user_id
        }

        if allowed == "system_info"
          hash.merge!(
            {
              :platform => CALABASH_ANDROID,
              :host_os => host_os,
              :host_os_version => host_os_version,
              :irb => irb?,
              :ruby_version => ruby_version,
              :used_bundle_exec => used_bundle_exec?,
              :used_cucumber => used_cucumber?,

              :version => Calabash::Android::VERSION,

              :ci => Calabash::Android::Environment.ci?,
              :jenkins => Calabash::Android::Environment.jenkins?,
              :travis => Calabash::Android::Environment.travis?,
              :circle_ci => Calabash::Android::Environment.circle_ci?,
              :gitlab => Calabash::Android::Environment.gitlab?,
              :teamcity => Calabash::Android::Environment.teamcity?
            }
          )
        end

        hash
      end
    end
  end
end

