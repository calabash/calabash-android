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
            'data' => reaction.data_as_hash,
            'intentFilterData' => filter
        }.to_json
      end

      class Filter
        attr_reader :intent

        def initialize(intent)
          @intent = intent
        end

        def to_json(*a)
          {
              'action' => intent.action,
              'component' => intent.component
          }.to_json
        end
      end

      class Reaction
        attr_reader :type, :data

        def data_as_hash
          if @data_hash_method
            @data_hash_method.call
          else
            {}
          end
        end

        def self.do_nothing
          reaction = Reaction.new

          reaction.instance_eval do
            @type = :'do-nothing'
          end

          reaction
        end

        def self.take_picture(path_on_device)
          reaction = Reaction.new
          data = {image_file: path_on_device}

          reaction.instance_eval do
            @type = :'take-picture'
            @data = data
            @data_hash_method = lambda do
              {
                  'imageFile' => @data[:image_file],
              }
            end
          end

          reaction
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

          reaction = Reaction.new

          reaction.instance_eval do
            @type = :instrumentation
            @data = param_data
            @data_hash_method = lambda do
              {
                  'testServerPort' => @data[:test_server_port],
                  'targetPackage' => @data[:target_package],
                  'class' => @data[:class],
                  'mainActivity' => @data[:main_activity],
              }
            end
          end

          reaction
        end
      end
    end
  end
end
