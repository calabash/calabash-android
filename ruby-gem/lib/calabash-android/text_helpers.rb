module Calabash
  module Android
    module TextHelpers
      def selection_start
        'SELECTION_START'
      end

      def selection_end
        'SELECTION_END'
      end

      def selection_all
        'SELECTION_ALL'
      end

      def has_text?(text)
        !query("* {text CONTAINS[c] '#{text}'}").empty?
      end

      def assert_text(text, should_find = true)
        raise "Text \"#{text}\" was #{should_find ? 'not ' : ''}found." if has_text?(text) ^ should_find

        true
      end

      def keyboard_enter_text(text, options = {})
        perform_action('keyboard_enter_text', text)
      end

      def keyboard_enter_char(character, options = {})
        keyboard_enter_text(character[0,1], options)
      end

      def enter_text(uiquery, text, options = {})
        tap_when_element_exists(uiquery, options)

        wait_for({timeout: 5, timeout_message: "Timeout waiting for element to gain focus"}) do
          query(uiquery, :isFocused).first
        end

        perform_action("set_selection", selection_end)

        keyboard_enter_text(text, options)

      end

      def clear_text_in(query_string, options={})
        unless query_string.nil?
          touch(query_string, options)
          sleep 0.5
        end

        clear_text(options)
      end

      def clear_text(options={})
        if options.is_a?(String)
          puts "Warning: The method clear_text now clears the text in the currently focused view. Use clear_text_in instead"
          puts "Notice that clear_text_in only clears the text of the first element matching the given query, not all."
          puts "Use query(query, setText: '') to replicate the old behaviour"

          clear_text_in(options)
        else
          perform_action('clear_text')
        end
      end

      def escape_quotes(str)
        str.gsub("'", "\\\\'")
      end
    end
  end
end
