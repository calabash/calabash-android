module Calabash
  module Android
    module TouchHelpers
      def tap(mark, *args)
        touch("* marked:'#{mark}'", *args)
      end

      def double_tap(uiquery, options = {})
        center_x, center_y = find_coordinate(uiquery, options)

        perform_action("double_tap_coordinate", center_x, center_y)
      end

      # Performs a "long press" operation on a selected view
      # Params:
      # +uiquery+: a uiquery identifying one view
      # +options[:length]+: the length of the long press in milliseconds (optional)
      #
      # Examples:
      #   - long_press("* id:'my_id'")
      #   - long_press("* id:'my_id'", {:length=>5000})
      def long_press(uiquery, options = {})
        center_x, center_y = find_coordinate(uiquery, options)
        length = options[:length]
        perform_action("long_press_coordinate", center_x, center_y, *(length unless length.nil?))
      end

      def touch(uiquery, options = {})
        center_x, center_y = find_coordinate(uiquery, options)

        perform_action("touch_coordinate", center_x, center_y)
      end

      def find_coordinate(uiquery, options={})
        raise "Cannot find nil" unless uiquery

        element = execute_uiquery(uiquery)

        raise "No elements found. Query: #{uiquery}" if element.nil?

        x = element["rect"]["center_x"]
        y = element["rect"]["center_y"]

        if options[:offset]
          x += options[:offset][:x] || 0
          y += options[:offset][:y] || 0
        end

        [x, y]
      end

      def tap_when_element_exists(query_string, options={})
        options.merge!({action: lambda {|q| touch(q, options)}})

        if options[:scroll] == true
          scroll_to(query_string, options)
        else
          when_element_exists(query_string, options)
        end
      end

      def long_press_when_element_exists(query_string, options={})
        options.merge!({action: lambda {|q| long_press(q, options)}})

        if options[:scroll] == true
          scroll_to(query_string, options)
        else
          when_element_exists(query_string, options)
        end
      end
    end
  end
end