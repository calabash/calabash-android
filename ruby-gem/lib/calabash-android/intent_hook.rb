module Calabash
  module Android
    class IntentHook
      attr_reader :reaction, :filter

      def initialize(reaction, filter)
        @reaction = reaction
        @filter = filter
      end

      def to_json(*a)
        {
            'type' => reaction.type.to_s,
            'data' => reaction.data,
            'intentFilterData' => filter
        }.to_json
      end

      class Filter
        attr_reader :intent, :component

        def initialize(intent, component)
          @intent = intent
          @component = component
        end

        def to_json(*a)
          {
              'action' => intent.action,
              'component' => component
          }.to_json
        end
      end

      class Reaction
        attr_reader :type, :data

        def initialize
          class << data
            define_method(:to_json) do |*a|
              @data_json_method.call
            end
          end
        end

        def self.instrumentation(data)
          param_data = data.dup

          if param_data[:test_server_port].nil? ||
              param_data[:target_package].nil?
            raise 'Must provide :test_server_port and :target_package'
          end

          param_data[:class] ||= 'sh.calaba.instrumentationbackend.InstrumentationBackend'

          param_data[:component] ||=
              AndroidComponent.new("#{param_data[:target_package]}.test",
                                   'sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner')

          intent_hook = Reaction.new

          intent_hook.instance_eval do
            @type = :instrumentation
            @data = param_data
            @data_json_method = lambda do
              {
                  'testServerPort' => [:test_server_port],
                  'targetPackage' => [:target_package],
                  'class' => [:class],
                  'mainActivity' => [:main_activity],
              }.to_json
            end
          end

          intent_hook
        end
      end
    end
  end
end
