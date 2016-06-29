require 'stringio'

describe Calabash::Android::Retryable do
  class StubObject
    include Calabash::Android::Retryable
  end

  describe ".retriable" do
    subject { StubObject.new }

    it 'retries' do
      expect {
        subject.retriable({ tries: 2, interval: 0.01 }) do
          raise "foo"
        end
      }.to raise_error(RuntimeError)
    end

    it 'succeeds' do
      expect {
        nr = 0
        subject.retriable({ tries: 2, interval: 0.01 }) do
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
