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

      # Appends `text` into the first view matching `uiquery`.
      def enter_text(uiquery, text, options = {})
        tap_when_element_exists(uiquery, options)
        sleep 0.5
        set_selection(-1, -1)
        keyboard_enter_text(text, options)
      end

      def clear_text_in(query_string, options={})
        touch(query_string, options)
        sleep 0.5
        clear_text(options)
      end

      # Clears the text of the currently focused view.
      def clear_text(options={})
        set_selection(-1, -1)
        sleep 0.1
        perform_action("delete_surrounding_text", -1, 0)
      end

      def escape_backslashes(str)
        backslash = "\\"
        str.gsub(backslash, backslash*4)
      end

      def escape_newlines(str)
        newline = "\n"
        str.gsub(newline, "\\n")
      end

      def escape_quotes(str)
        str.gsub("'", "\\\\'")
      end

      def escape_string(str)
        escape_newlines(escape_quotes(escape_backslashes(str)))
      end

      # Sets the selection of the currently focused view.
      #
      # @param [Integer] selection_start The start of the selection, can be
      #  negative to begin counting from the end of the string.
      # @param [Integer] selection_end The end of the selection, can be
      #  negative to begin counting from the end of the string.
      def set_selection(selection_start, selection_end)
        perform_action("set_selection", selection_start, selection_end)
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
