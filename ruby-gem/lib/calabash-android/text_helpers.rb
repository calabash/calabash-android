module Calabash
  module Android
    module TextHelpers
      def has_text?(text)
        !query("* {text CONTAINS[c] '#{text}'}").empty?
      end

      def assert_text(text, should_find = true)
        raise "Text \"#{text}\" was #{should_find ? 'not ' : ''}found." if has_text?(text) ^ should_find

        true
      end

      def keyboard_enter_text(text, options = {})
        wait_for_keyboard
        perform_action('keyboard_enter_text', text)
      end

      def keyboard_enter_char(character, options = {})
        keyboard_enter_text(character[0,1], options)
      end

      def enter_text(uiquery, text, options = {})
        tap_when_element_exists(uiquery, options)
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

      def keyboard_visible?
        input_method = `#{default_device.adb_command} shell dumpsys input_method`.force_encoding('UTF-8')
        shown = input_method.each_line.grep(/mInputShown\s*=\s*(.*)/){$1}.first.chomp

        if shown == "true"
          true
        elsif shown == "false"
          false
        else
          raise "Could not detect keyboard visibility. '#{shown}'"
        end
      end

      def wait_for_keyboard(opt={})
        params = opt.clone
        params[:timeout_message] ||= "Timed out waiting for the keyboard to appear"
        params[:timeout] ||= 5

        wait_for(params) do
          keyboard_visible?
        end
      end
    end
  end
end