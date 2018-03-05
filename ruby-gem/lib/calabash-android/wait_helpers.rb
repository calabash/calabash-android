module Calabash
  module Android

    module WaitHelpers


      class WaitError < RuntimeError
      end

      # 'post_timeout' is the time to wait after a wait function returns true
      DEFAULT_OPTS = {
          :timeout => 30,
          :retry_frequency => 0.3,
          :post_timeout => 0,
          :timeout_message => 'Timed out waiting...',
          :screenshot_on_error => true
      }

      def wait_for(options_or_timeout=DEFAULT_OPTS, &block)
        #note Hash is preferred, number acceptable for backwards compat
        default_timeout = DEFAULT_OPTS[:timeout]
        timeout = options_or_timeout || default_timeout
        post_timeout = DEFAULT_OPTS[:post_timeout]
        retry_frequency = DEFAULT_OPTS[:retry_frequency]
        timeout_message = DEFAULT_OPTS[:timeout_message]
        screenshot_on_error = DEFAULT_OPTS[:screenshot_on_error]

        if options_or_timeout.is_a?(Hash)
          timeout = options_or_timeout[:timeout] || default_timeout
          retry_frequency = options_or_timeout[:retry_frequency] || retry_frequency
          post_timeout = options_or_timeout[:post_timeout] || post_timeout
          timeout_message = options_or_timeout[:timeout_message]
          if options_or_timeout.key?(:screenshot_on_error)
            screenshot_on_error = options_or_timeout[:screenshot_on_error]
          end
        end

        begin
          Timeout::timeout(timeout, WaitError) do
            sleep(retry_frequency) until yield
          end
          sleep(post_timeout) if post_timeout > 0
        rescue WaitError => e
          msg = timeout_message || e
          if screenshot_on_error
            sleep(retry_frequency)
            return screenshot_and_retry(msg, &block)
          else
            raise wait_error(msg)
          end
        rescue => e
          handle_error_with_options(e, nil, screenshot_on_error)
        end
      end

      def screenshot_and_retry(msg, &block)
        path  = screenshot
        res = yield
        # Validate after taking screenshot
        if res
          FileUtils.rm_f(path)
          return res
        else
          embed(path, 'image/png', msg)
          raise wait_error(msg)
        end
      end

      def wait_poll(opts, &block)
        test = opts[:until]
        if test.nil?
          cond = opts[:until_exists]
          raise "Must provide :until or :until_exists" unless cond
          test = lambda { element_exists(cond) }
        end
        wait_for(opts) do
          if test.call()
            true
          else
            yield
            false
          end
        end
      end

      #options for wait_for apply
      def wait_for_element_exists(uiquery, options={})
        wait_for_elements_exist([uiquery], options)
      end

      #options for wait_for apply
      def wait_for_elements_exist(elements_arr, options={})
        if elements_arr.is_a?(String) || elements_arr.is_a?(Symbol)
          elements_arr = [elements_arr.to_s]
        end
        options[:timeout_message] = options[:timeout_message] || "Timeout waiting for elements: #{elements_arr.join(",")}"
        wait_for(options) do
          elements_arr.all? { |q| element_exists(q) }
        end
      end

      #options for wait_for apply
      def wait_for_element_does_not_exist(uiquery, options={})
        wait_for_elements_do_not_exist([uiquery], options)
      end

      #options for wait_for apply
      def wait_for_elements_do_not_exist(elements_arr, options={})
        if elements_arr.is_a?(String)
          elements_arr = [elements_arr]
        end
        options[:timeout_message] = options[:timeout_message] || "Timeout waiting for no elements matching: #{elements_arr.join(",")}"
        wait_for(options) do
          elements_arr.none? { |q| element_exists(q) }
        end
      end

      def handle_error_with_options(ex, timeout_message, screenshot_on_error)
        error_class = (ex && ex.class) || RuntimeError
        error = error_class.new(timeout_message || ex.message)

        if screenshot_on_error
          screenshot_and_raise error
        else
          raise error
        end
      end

      def wait_error(msg)
        (msg.is_a?(String) ? WaitError.new(msg) : msg)
      end

      # Performs a lambda action until the element (a query string) appears.
      # The default action is to do nothing.
      #
      # Raises an error if no uiquery is specified. Same options as wait_for
      # which are timeout, retry frequency, post_timeout, timeout_message, and
      # screenshot on error.
      #
      # Example usage:
      # until_element_exists("Button", :action => lambda { swipe("up") })
      def until_element_exists(uiquery, opts = {})
        extra_opts = { :until_exists => uiquery, :action => lambda { ; } }
        opts = DEFAULT_OPTS.merge(extra_opts).merge(opts)
        wait_poll(opts) do
          opts[:action].call
        end
      end

      # Performs a lambda action until the element (a query string) disappears.
      # The default action is to do nothing.
      #
      # Raises an error if no uiquery is specified. Same options as wait_for
      # which are timeout, retry frequency, post_timeout, timeout_message, and
      # screenshot on error.
      #
      # Example usage:
      # until_element_does_not_exist("Button", :action => lambda { swipe("up") })
      def until_element_does_not_exist(uiquery, opts = {})
        condition = lambda { element_exists(uiquery) ? false : true }
        extra_opts = { :until => condition, :action => lambda { ; } }
        opts = DEFAULT_OPTS.merge(extra_opts).merge(opts)
        wait_poll(opts) do
          opts[:action].call
        end
      end

      # Performs a lambda action once the element exists.
      # The default behavior is to touch the specified element.
      #
      # Raises an error if no uiquery is specified. Same options as wait_for
      # which are timeout, retry frequency, post_timeout, timeout_message, and
      # screenshot on error.
      #
      # Example usage: when_element_exists("Button", :timeout => 10)
      def when_element_exists(uiquery, opts = {})
        action = { :action => lambda { touch uiquery } }
        opts = action.merge(opts)
        wait_for_elements_exist([uiquery], opts)

        if opts[:action].parameters.length == 0
          opts[:action].call
        else
          opts[:action].call(uiquery)
        end
      end

      def wait_for_text(text, options={})
        wait_for_element_exists("* {text CONTAINS[c] '#{text}'}", options)
      end

      def wait_for_text_to_disappear(text, options={})
        wait_for_element_does_not_exist("* {text CONTAINS[c] '#{text}'}", options)
      end

      def wait_for_activity(activity_name, options={})
        wait_for(options) do
          perform_action('get_activity_name')['message'] == activity_name
        end
      end
    end
  end
end

