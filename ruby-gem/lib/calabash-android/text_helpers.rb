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
        perform_action('keyboard_enter_text', text)
      end

      def keyboard_enter_char(character, options = {})
        keyboard_enter_text(character[0,1], options)
      end

      def enter_text(uiquery, text, options = {})
        tap_when_element_exists(uiquery, options)
        sleep 0.5
        keyboard_enter_text(text, options)
      end

      def clear_text(query_string, options={})
        result = query(query_string, setText: '')

        raise "No elements found. Query: #{query_string}" if result.empty?

        true
      end

      def escape_quotes(str)
        str.gsub("'", "\\\\'")
      end
    end
  end
end