module Calabash
  module Android
    module Retryable


      def retriable(opts, &blk)
        tries = opts[:tries]
        interval = opts[:interval]

        tries.times do |try|
          begin
            blk.call
            return

          rescue => e
            if (try + 1) >= tries
              raise
            else
              sleep interval
            end
          end
        end
      end
    end
  end
end
