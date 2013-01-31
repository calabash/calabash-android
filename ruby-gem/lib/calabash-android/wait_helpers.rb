module Calabash
  module Android

    module WaitHelpers


      class WaitError < RuntimeError
      end


      def wait_for(options_or_timeout=
          {:timeout => 10,
           :retry_frequency => 0.2,
           :post_timeout => 0.1,
           :timeout_message => "Timed out waiting...",
           :screenshot_on_error => true}, &block)
        #note Hash is preferred, number acceptable for backwards compat
        timeout=options_or_timeout
        post_timeout=0.1
        retry_frequency=0.2
        timeout_message = nil
        screenshot_on_error = true

        if options_or_timeout.is_a?(Hash)
          timeout = options_or_timeout[:timeout] || 10
          retry_frequency = options_or_timeout[:retry_frequency] || 0.2
          post_timeout = options_or_timeout[:post_timeout] || 0.1
          timeout_message = options_or_timeout[:timeout_message]
          screenshot_on_error = options_or_timeout[:screenshot_on_error] || true
        end

        begin
          Timeout::timeout(timeout, WaitError) do
            sleep(retry_frequency) until yield
          end
          sleep(post_timeout) if post_timeout > 0
        rescue WaitError => e
          handle_error_with_options(e, timeout_message, screenshot_on_error)
        rescue Exception => e
          handle_error_with_options(e, nil, screenshot_on_error)
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
      def wait_for_elements_exist(elements_arr, options={})
        options[:timeout_message] = options[:timeout_message] || "Timeout waiting for elements: #{elements_arr.join(",")}"
        wait_for(options) do
          elements_arr.all? { |q| element_exists(q) }
        end
      end

#options for wait_for apply
      def wait_for_elements_do_not_exist(elements_arr, options={})
        options[:timeout_message] = options[:timeout_message] || "Timeout waiting for no elements matching: #{elements_arr.join(",")}"
        wait_for(options) do
          elements_arr.none? { |q| element_exists(q) }
        end
      end

      def handle_error_with_options(ex, timeout_message, screenshot_on_error)
        msg = (timeout_message || ex)
        if ex
          msg = "#{msg} (#{ex.class})"
        end
        if screenshot_on_error
          screenshot_and_raise msg
        else
          raise msg
        end
      end


    end
  end
end

