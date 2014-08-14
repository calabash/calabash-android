module Calabash
  module Android
    module TouchHelpers
      include ::Calabash::Android::Gestures

      def execute_gesture(multi_touch_gesture)
        result = JSON.parse(http("/gesture", JSON.parse(multi_touch_gesture.to_json)))

        if result['outcome'] != 'SUCCESS'
          raise "Failed to perform gesture. #{result['reason']}"
        end
      end
      
      def tap(query_string, options={})
        execute_gesture(Gesture.with_parameters(Gesture.tap(options), {query_string: query_string}.merge(options)))
      end

      def double_tap(query_string, options={})
        execute_gesture(Gesture.with_parameters(Gesture.double_tap(options), {query_string: query_string}.merge(options)))
      end

      def long_press(query_string, options={})
        length = options[:length]

        if length
          puts "Using the length key is deprecated. Use 'time' (in seconds) instead."
          options[:time] = length/1000.0
        end

        options[:time] ||= 1

        tap(query_string, options)
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
    end
  end
end