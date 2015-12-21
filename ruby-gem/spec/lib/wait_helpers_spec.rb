
describe Calabash::Android::WaitHelpers do

  it "can override DEFAULT_OPTS" do
    original = Calabash::Android::WaitHelpers::DEFAULT_OPTS[:timeout]
    begin
      Calabash::Android::WaitHelpers::DEFAULT_OPTS[:timeout] = 10
      expect(Calabash::Android::WaitHelpers::DEFAULT_OPTS[:timeout]).to be == 10
    ensure
      Calabash::Android::WaitHelpers::DEFAULT_OPTS[:timeout] = original
    end
  end
end

