module Calabash
  module Android
    class AndroidComponent
      attr_reader :package_name, :class_name

      def initialize(package_name, class_name)
        @package_name = package_name
        @class_name = class_name
      end

      def self.from_json(json)
        self.new(json['packageName'], json['className'])
      end

      def to_json(*a)
        {
            'className' => class_name,
            'packageName' => package_name
        }.to_json
      end
    end
  end
end
