module Calabash
  module Android
    module Defaults
      def self.query_timeout
        (ENV['CALABASH_DEFAULT_TIMEOUT'] && ENV['CALABASH_DEFAULT_TIMEOUT'].to_i) || 5
      end
    end
  end
end