module Calabash
  module Android
    module TouchHelpers
      def tap(query_string, options={})
        Gesture.generate_gesture(Gesture.tap(options), query_string).execute
      end

      def double_tap(query_string, options={})
        Gesture.generate_gesture(Gesture.double_tap(options), query_string).execute
      end

      def long_press(query_string, options={})
        length = options[:length]

        if length
          puts "Using the length parameter is deprecated. Use 'time' (in seconds) instead."
          options[:time] = length/1000.0
        end

        options[:time] ||= 1

        tap(query_string, options)
      end

      def swipe(*args)
        if args.length == 1 || (args.length == 2 && args[1].is_a?(Hash))
          query_string = "* id:'content'"
          direction = args[0]
          options = args[1] || {}
        elsif args.length == 2 || (args.length == 3 && args[2].is_a?(Hash))
          query_string = args[0]
          direction = args[1]
          options = args[2] || {}
        else
          raise ArgumentError.new "wrong number of arguments (#{args.length} for 1..3)"
        end

        Gesture.generate_gesture(Gesture.swipe(direction, options), query_string).execute
      end

      def fling(*args)
        if args.length == 1
          raise ArgumentError.new "first parameter must be either direction or query_string" if args.last.is_a?(Hash)

          swipe(args[0], {fling: true})
        elsif args.length == 2
          if args.last.is_a?(Hash)
            swipe(args[0], {fling: true}.merge(args[1]))
          else
            swipe(args[0], args[1], {fling: true})
          end
        elsif args.length == 3
          swipe(args[0], args[1], {fling: true}.merge(args[2]))
        else
          raise ArgumentError.new "wrong number of arguments (#{args.length} for 1..3)"
        end
      end

      def pinch(*args)
        if args.length == 1 || (args.length == 2 && args[1].is_a?(Hash))
          query_string = "* id:'content'"
          direction = args[0]
          options = args[1] || {}
        elsif args.length == 2 || (args.length == 3 && args[2].is_a?(Hash))
          query_string = args[0]
          direction = args[1]
          options = args[2] || {}
        else
          raise ArgumentError.new "wrong number of arguments (#{args.length} for 1..3)"
        end

        Gesture.generate_gesture(Gesture.pinch(direction, options), query_string).execute
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