module Calabash
  module Android
    class IntentHook
      USAGE_COUNT_INFINITE = -1

      attr_reader :reaction, :filter, :usage_count

      def initialize(reaction, filter, usage_count=USAGE_COUNT_INFINITE)
        @reaction = reaction
        @filter = filter
        @usage_count = usage_count.to_i
      end

      def to_json(*a)
        {
            'usageCount' => usage_count,
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
              if @data_json_method.nil?
                {}
              else
                @data_json_method.call
              end
            end
          end
        end

        def self.do_nothing
          intent_hook = Reaction.new

          intent_hook.instance_eval do
            @type = :'do-nothing'
          end

          intent_hook
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
