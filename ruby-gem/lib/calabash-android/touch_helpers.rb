module Calabash
  module Android
    module TouchHelpers
      include ::Calabash::Android::Gestures

      def execute_gesture(multi_touch_gesture)
        result = JSON.parse(http("/gesture", JSON.parse(multi_touch_gesture.to_json), read_timeout: multi_touch_gesture.timeout+10))

        if result['outcome'] != 'SUCCESS'
          raise "Failed to perform gesture. #{result['reason']}"
        end

        nil
      end

      def tap(mark, *args)
        puts "Warning: The method tap is deprecated. Use tap_mark instead. In later Calabash versions we will change the semantics of `tap` to take a general query."

        tap_mark(mark, *args)
      end

      def tap_mark(mark, *args)
        touch("* marked:'#{mark}'", *args)
      end

      def touch(query_string, options={})
        if query_result?(query_string)
          center_x, center_y = find_coordinate(query_string, options)

          perform_action("touch_coordinate", center_x, center_y)
        else
          if query_string.nil? && (options.nil? || options.empty?)
            raise "Can't touch nil"
          end
          execute_gesture(Gesture.with_parameters(Gesture.tap(options), {query_string: query_string}.merge(options)))
        end
      end

      def double_tap(query_string, options={})
        if query_result?(query_string)
          center_x, center_y = find_coordinate(query_string, options)

          perform_action("double_tap_coordinate", center_x, center_y)
        else
          execute_gesture(Gesture.with_parameters(Gesture.double_tap(options), {query_string: query_string}.merge(options)))
        end
      end

      def long_press(query_string, options={})
        if query_result?(query_string)
          center_x, center_y = find_coordinate(query_string, options)
          length = options[:length]

          perform_action("long_press_coordinate", center_x, center_y, *(length unless length.nil?))
        else
          length = options[:length]

          if length
            puts "Using the length key is deprecated. Use 'time' (in seconds) instead."
            options[:time] = length/1000.0
          end

          options[:time] ||= 1

          touch(query_string, options)
        end
      end

      def drag(*args)
        pan(*args)
      end

      def pan_left(options={})
        pan("DecorView", :left, options)
      end

      def pan_right(options={})
        pan("DecorView", :right, options)
      end

      def pan_up(options={})
        pan("* id:'content'", :up, options)
      end

      def pan_down(options={})
        pan("* id:'content'", :down, options)
      end

      def pan(query_string, direction, options={})
        execute_gesture(Gesture.with_parameters(Gesture.swipe(direction, options), {query_string: query_string}.merge(options)))
      end

      def flick_left(options={})
        flick("DecorView", :left, options)
      end

      def flick_right(options={})
        flick("DecorView", :right, options)
      end

      def flick_up(options={})
        flick("* id:'content'", :up, options)
      end

      def flick_down(options={})
        flick("* id:'content'", :down, options)
      end

      def flick(query_string, direction, options={})
        execute_gesture(Gesture.with_parameters(Gesture.swipe(direction, {flick: true}.merge(options)), {query_string: query_string}.merge(options)))
      end

      def pinch_out(options={})
        pinch("* id:'content'", :out, options)
      end

      def pinch_in(options={})
        pinch("* id:'content'", :in, options)
      end

      def pinch(query_string, direction, options={})
        execute_gesture(Gesture.with_parameters(Gesture.pinch(direction, options), {query_string: query_string}.merge(options)))
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

      def query_result?(uiquery)
        element = if uiquery.is_a?(Array)
          uiquery.first
        else
          uiquery
        end

        element.is_a?(Hash) && element.has_key?('rect') && element['rect'].has_key?('center_x') && element['rect'].has_key?('center_y')
      end
    end
  end
end