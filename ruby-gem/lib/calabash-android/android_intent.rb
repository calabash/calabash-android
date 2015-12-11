module Calabash
  module Android
    class AndroidIntent
      attr_reader :action, :data, :flags, :package, :type, :component, :extras
      attr_reader :clip_data, :intent_index

      def self.with_action(action)
        android_intent = AndroidIntent.new

        android_intent.instance_eval do
          @action = action
        end

        android_intent
      end

      def self.from_json(json)
        android_intent = AndroidIntent.new

        intent_json = json['intent']
        index = json['index']

        android_intent.instance_eval do
          unless intent_json.nil?
            @action = intent_json['action']
            @data = intent_json['data'] && URI.parse(intent_json['data'])
            @flags = intent_json['flags']
            @package = intent_json['package']
            @type = intent_json['type']
            @component =
                intent_json['component'] && AndroidComponent.from_json(intent_json['component'])
            @clip_data = intent_json['clipData']

            @extras = intent_json['extras']
          end

          @intent_index = index
        end

        android_intent
      end

      def to_json(*a)
        {
            'index' => intent_index,
            'intent' => {
                'action' => action,
                'data' => data,
                'flags' => flags,
                'package' => package,
                'type' => type,
                'component' => component,
                'clipData' => clip_data,
                'extras' => extras
            }
        }.to_json
      end
    end
  end
end
