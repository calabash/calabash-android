require 'calabash-android/env'

module Calabash
  module Android
    module MonkeyHelpers
      include Calabash::Android::EnvironmentHelpers

      MAX_RETRIES = 10
      @@monkey_port = nil
      @@monkey_pid = nil

      def monkey_move_from(from_x, from_y, to_x, to_y, args={})
        start_monkey
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

        kill_existing_monkey_processes
      end

      def get_monkey_port
        MAX_RETRIES.times do
          port = rand((1024..65535))
          monkey_starter_thread = Thread.new do
            Thread.current[:output]= `#{adb_command} shell monkey --port #{port}`
          end
          sleep(4)

          output = monkey_starter_thread[:output]
          unless output && output.include?('Error binding to network socket.')
            return port
          end
        end
        raise 'Unable to start monkey on device'
      end

      def start_monkey
        kill_existing_monkey_processes
        @@monkey_port   = get_monkey_port
        monkey_timeout  = 10

        options = {
            :timeout => monkey_timeout,
            :timeout_message => "Monkey did not start on #{@@monkey_port} in #{monkey_timeout} seconds"
        }

        wait_for(options) {
          perform_action('send_tcp', @@monkey_port, 'sleep 0', true)
        }
      end

      def existing_monkey_pids
        procs = `#{adb_command} shell ps`
        procs.scan(/.+?\s(?<pid>[0-9]+).+?com.android.commands.monkey\r?\n?/).flatten
      end

      def kill_monkey_processes_on_device
        perform_action('send_tcp', @@monkey_port, "quit", true) unless @@monkey_port.nil?
        existing_monkey_pids.each do |pid|
          `#{adb_command} shell kill -9 #{pid}`
        end
      end

      def kill_monkey_processes_on_host
        unless xamarin_test_cloud?
          monkey_args = "#{adb_command} shell monkey --port"
          if Env.is_windows?
            processes = `WMIC PATH win32_process GET Commandline, processid /FORMAT:CSV`.split(/\r?\n/).flatten
            processes.each do |process|
              components = process.split(',')
              if components.length > 2 && components[1].index(monkey_args) == 0
                `kill -9 #{components[2]}`
              end
            end
          else
            processes = `ps -xww -o pid,user,args`.split("\n").flatten
            processes.each do |process|
              if process.index(monkey_args) == 0
                pid = process.strip().split(' ')[0].to_i
                `kill -9 #{pid}`
              end
            end
          end
        end
      end

      def kill_existing_monkey_processes
        kill_monkey_processes_on_host
        kill_monkey_processes_on_device
        @@monkey_port = nil
        @@monkey_pid = nil
      end

      def monkey_tap(x, y, should_start_monkey=true)
        start_monkey if should_start_monkey
        monkey_touch(:down, x, y)
        monkey_touch(:up, x, y)
        kill_existing_monkey_processes
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
          default_device.adb_command
      end
    end
  end
end
