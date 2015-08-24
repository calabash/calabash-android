require 'calabash-android/env'

module Calabash
  module Android
    module MonkeyHelpers
      include Calabash::Android::EnvironmentHelpers

      MAX_RETRIES = 10
      @@monkey_port = nil
      @@monkey_pid = nil

      def monkey_move_from(from_x, from_y, to_x, to_y, args={})
        @@monkey_port = start_monkey
        monkey_touch(:down, from_x, from_y)
        sleep(args.fetch(:hold_time))

        x_delta = to_x - from_x
        y_delta = to_y - from_y
        steps = args.fetch(:steps)

        steps.times do |i|
          move_x = (from_x + ((i+1) * (x_delta.to_f / steps))).to_i
          move_y = (from_y + ((i+1) * (y_delta.to_f / steps))).to_i
          monkey_touch(:move, move_x, move_y)
        end

        sleep(args.fetch(:hang_time))
        monkey_touch(:up, to_x, to_y)
      end

      def start_monkey
        unless xamarin_test_cloud? #monkey processes are automatically killed in XTC
          kill_existing_monkey_processes
        end

        MAX_RETRIES.times do
          port = rand((1024..65535))

          monkey_starter_thread = Thread.new do
            Thread.current[:output]= `#{_adb_command} shell monkey --port #{port}`
          end
          sleep(4)

          output = monkey_starter_thread[:output]
          unless output && output.include?('Error binding to network socket.')
            return port
          end
        end
        raise 'Unable to start com.android.commands.monkey'
      end

      def existing_monkey_pids
        procs = `#{_adb_command} shell ps`
        procs.scan(/.+?\s(?<pid>[0-9]+).+?com.android.commands.monkey\r?\n?/).flatten
      end

      def kill_existing_monkey_processes
        existing_monkey_pids.each do |pid|
          `#{_adb_command} shell kill -9 #{pid}`
        end
      end

      def monkey_tap(x, y, should_start_monkey=true)
        start_monkey if should_start_monkey
        monkey_touch(:down, x, y)
        monkey_touch(:up, x, y)
      end

      def monkey_touch(touch_type, x, y)
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

      def adb_command
        if xamarin_test_cloud?
          Env.adb_path
        else
          default_device.adb_command
        end
      end
    end
  end
end
