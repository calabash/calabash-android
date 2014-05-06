require 'calabash-android/operations'
require 'json'

module Calabash
  module Android
    #
    # Orientation Helpers
    #
    module OrientationHelpers

      # Get device orientation
      # @note Optionally force orientation to "down"
      # @param [Boolean] force_down force orientation to "down"
      # @return [Symbol] device orientation (:up, :down, :left, :right)
      def device_orienation(force_down = false)
        # consider using this code for Java implementation (http://stackoverflow.com/a/6786814/1095277)
        # when returning device orientation must detect "reverse" positions
        # create "get_device_orientaion" action
        ni
      end

      # Get current activity orientation
      # @note Activity orientation is not an easy feature to implement, this method offers initial support.
      # Activity orientation is not the same as physical device implementation.
      # @see http://developer.android.com/guide/topics/manifest/activity-element.html
      # @return [String] current activity orientation ("up", "down", "left", "right")
      # <li>"down"</li> - default portrait orientation (sensor panel down)
      # <li>"up"</li> - reverse portrait orientation (upside down, sensor panel up)
      # <li>"right"</li> - default landscape orientation (sensor panel on the right)
      # <li>"left"</li> - landscape left orientation (sensor panel on the left)
      def activity_orientation
        res = JSON.parse(performAction("get_activity_orientation"))
        screenshot_and_raise("get_activity_orientation action failed: #{res['message']}") unless res["success"]
        orientation = res["bonusInformation"].first
        # convert return value to cross-platform (ios-like) values
        # add check for "reverse" for possible future changes in Java implementation
        if orientation.include?("portrait")
            orientation.include?("reverse") ? "down" : "up"
        else
            orientation.include?("reverse") ? "left" : "right"
        end
      end

      # Return current notification bar orientation
      # @note Matches current activity orientation
      # return [String] current notification bar orientation
      def notification_bar_orientation
        activity_orientation
      end

      # Return current notification bar orientation (analogue of iOS status bar in Android)
      # @note Helper function for writing cross-platform tests
      # return [String] current status bar orientation
      def status_bar_orientation
        activity_orientation
      end

      # Returns +true+ if activity orientation is portrait
      # @return [Boolean]
      def portrait?
        orientation = activity_orientation
        orientation.eql?("up") || orientation.eql?("down")
      end

      # Returns +true+ if activity orientation is landscape
      # @return [Boolean]
      def landscape?
        orientation = activity_orientation
        orientation.eql?("left") || orientation.eql?("right")
      end
    end
  end
end
