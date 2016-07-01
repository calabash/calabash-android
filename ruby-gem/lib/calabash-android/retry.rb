module Calabash
  module Android
    module Retry
      def self.retry(opts, &blk)
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
