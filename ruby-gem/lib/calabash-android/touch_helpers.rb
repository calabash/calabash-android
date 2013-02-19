module Calabash
  module Android
    module TouchHelpers
      def tap(mark, *args)
        touch("* marked:'#{mark}'", *args)
      end
    end
  end
end