require 'zip'

module Calabash
  module Utils
    def self.with_silent_zip(&block)
      previous_value = false

      if Zip.respond_to?(:warn_invalid_date)
        previous_value = Zip.warn_invalid_date
      end

      if Zip.respond_to?(:warn_invalid_date=)
        Zip.warn_invalid_date = false
      end

      r = block.call

      if Zip.respond_to?(:warn_invalid_date=)
        Zip.warn_invalid_date = previous_value
      end

      r
    end
  end
end