require 'stringio'

describe Calabash::Android::Retry do
  describe "#retry" do
    it 'retries' do
      expect {
        Calabash::Android::Retry.retry({ tries: 2, interval: 0.01 }) do
          raise "foo"
        end
      }.to raise_error(RuntimeError)
    end

    it 'succeeds' do
      expect {
        nr = 0
        Calabash::Android::Retry.retry({ tries: 2, interval: 0.01 }) do
          nr = nr + 1
          if nr == 2
            :ok
          else
            raise :fail
          end
        end
      }.not_to raise_error
    end
  end
end
