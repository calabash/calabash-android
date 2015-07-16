require 'calabash-android/env'

module Calabash
  module Android
    module DragHelpers

      # Public API
      def drag_coordinates(from_x, from_y, to_x, to_y, options={})
        puts "Dragging from #{from_x},#{from_y} to #{to_x},#{to_y}"

        monkey_touch(:down, from_x, from_y)
        sleep(options.fetch(:hold_time, 1))

        x_delta = to_x - from_x
        y_delta = to_y - from_y
        steps = options.fetch(:steps, 5)

        (1..steps).each do |i|
          move_x = (from_x + (i * (x_delta.to_f / steps))).to_i
          move_y = (from_y + (i * (y_delta.to_f / steps))).to_i
          monkey_touch(:move, move_x, move_y)
        end

        sleep(options.fetch(:hang_time, 1))
        monkey_touch(:up, to_x, to_y)
      end

      def drag_and_drop(from_query, to_query, options={})
        wait_for_element_exists(from_query)
        wait_for_element_exists(to_query)

        from_results = query(from_query)
        to_results = query(to_query)

        if from_results.any?
          if to_results.any?
            from_rect = from_results.first['rect']
            to_rect = to_results.first['rect']
            drag_coordinates(from_rect['center_x'], from_rect['center_y'], to_rect['center_x'], to_rect['center_y'], options)
          else
            puts "No matching elements for: #{to_query}"
          end
        else
          puts "No matching elements for: #{from_query}"
        end
      end

      private
      MAX_RETRIES = 7
      @@monkey_port = nil

      def start_monkey
        kill_existing_monkey_processes
        MAX_RETRIES.times do
          port = rand((1025..65535))
          output = ''
          timeout(0.5) do
            output = `#{adb_command} shell monkey --port #{port}`
          end rescue Timeout::Error #ignore timeout error

          unless output.include? 'Error binding to network socket.'
            #The shim will always exit, but on success it will not output the line above
            if existing_monkey_pids.count == 1 then
              return port
            end
          end
        end
        raise 'Unable to connect to start com.android.commands.monkey'
      end

      def existing_monkey_pids
        procs = `#{adb_command} shell ps`
        procs.scan(/.+?\s(?<pid>[0-9]+).+?com.android.commands.monkey\r?\n?/).flatten
      end

      def kill_existing_monkey_processes
        existing_monkey_pids.each do |pid|
          `#{adb_command} shell kill -9 #{pid}`
        end
      end

      def ensure_connected
        #lazy instantiation of monkey, since it takes a while
        @@monkey_port ||= start_monkey
      end

      def monkey_touch(touch_type, x, y)
        ensure_connected
        case touch_type
          when :move
            perform_action('send_tcp', @@monkey_port, "touch move #{x} #{y}", true)
          when :down
            perform_action('send_tcp', @@monkey_port, "touch down #{x} #{y}", true)
          when :up
            perform_action('send_tcp', @@monkey_port, "touch up #{x} #{y}", true)
          else
            raise "touch_type #{touch_type} is invalid"
        end
      end

      #private version of Operations Device.adb_command
      def adb_command
        @operations_instance ||= lambda do
          Class.new do
            include Calabash::Android::Operations
          end.new
        end.call

        device = @operations_instance.default_device
        device.send(:adb_command)
      end
    end
  end
end
