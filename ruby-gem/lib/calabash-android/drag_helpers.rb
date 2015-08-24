require 'calabash-android/env'
require 'calabash-android/monkey_helpers'

module Calabash
  module Android
    module DragHelpers
      include Calabash::Android::EnvironmentHelpers
      include Calabash::Android::MonkeyHelpers

      def drag_coordinates(from_x, from_y, to_x, to_y, steps=10, hold_time=0.5, hang_time=0.5)
        log "Dragging from #{from_x},#{from_y} to #{to_x},#{to_y}"
        monkey_move_from(from_x, from_y,
                         to_x, to_y,
                         hold_time: hold_time,
                         hang_time: hang_time,
                         steps: steps)
      end

      def drag_and_drop(from_query, to_query, steps=10, hold_time=0.5, hang_time=0.5)
        wait_for_element_exists(from_query)
        wait_for_element_exists(to_query)

        from_results = query(from_query)
        to_results = query(to_query)

        if from_results.any?
          if to_results.any?
            from_rect = from_results.first['rect']
            to_rect = to_results.first['rect']
            from_x = from_rect['center_x']
            from_y = from_rect['center_y']
            to_x = to_rect['center_x']
            to_y = to_rect['center_y']

            monkey_move_from(from_x, from_y, to_x, to_y, hold_time: hold_time, hang_time: hang_time, steps: steps)
          else
            raise "No matching elements for: #{to_query}"
          end
        else
          raise "No matching elements for: #{from_query}"
        end
      end
    end
  end
end