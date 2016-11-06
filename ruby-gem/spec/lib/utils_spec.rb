describe Calabash::Utils do
  describe 'with_silent_zip' do
    it 'is a method that takes a block and evaluates it' do
      expect(Calabash::Utils.with_silent_zip do
        2+2
      end).to eq(4)
    end

    it 'disables zip warnings about invalid date if zip responds to it' do
      stub_const("Zip", Module.new do
        @warn_invalid_date = true

        def self.warn_invalid_date
          @warn_invalid_date
        end

        def self.warn_invalid_date=(value)
          @warn_invalid_date = value
        end
      end)

      expect(Calabash::Utils.with_silent_zip do
        Zip.warn_invalid_date
      end).to eq(false)
    end

    it 'does not try to disable zip warnings about invalid date if zip does not respond to it' do
      stub_const("Zip", Module.new do
      end)

      expect(Calabash::Utils.with_silent_zip do
        2+2
      end).to eq(4)
    end

    it 'resets zip warnings about invalid date if zip responds to it' do
      stub_const("Zip", Module.new do
        @warn_invalid_date = :default_value

        def self.warn_invalid_date
          @warn_invalid_date
        end

        def self.warn_invalid_date=(value)
          @warn_invalid_date = value
        end
      end)

      expect(Calabash::Utils.with_silent_zip do
        Zip.warn_invalid_date
      end).to eq(false)

      expect(Zip.warn_invalid_date).to eq(:default_value)
    end
  end
end