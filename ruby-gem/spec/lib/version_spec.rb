describe Calabash::Android::Version do

  describe '.new' do
    describe 'non-pre-release versions' do
      subject(:version) { Calabash::Android::Version.new('0.9.169') }
      it { expect(version).not_to be nil }
      it { expect(version.major).to be == 0 }
      it { expect(version.minor).to be == 9 }
      it { expect(version.patch).to be == 169 }
      it { expect(version.pre).to be nil }
      it { expect(version.pre_version).to be nil }
    end

    describe 'unnumbered pre-release versions' do
      subject(:version) { Calabash::Android::Version.new('0.9.169.pre') }
      it { expect(version.pre).to be == 'pre' }
      it { expect(version.pre_version).to be nil }
    end

    describe 'numbered pre-release versions' do
      subject(:version) { Calabash::Android::Version.new('0.9.169.pre1') }
      it { expect(version.pre).to be == 'pre1' }
      it { expect(version.pre_version).to be == 1 }
    end

    describe 'invalid arguments' do
      it { expect { Calabash::Android::Version.new(' ') }.to raise_error ArgumentError }
      it { expect { Calabash::Android::Version.new('5.1.pre3') }.to raise_error ArgumentError }
      it { expect { Calabash::Android::Version.new('5.pre2') }.to raise_error ArgumentError }
    end
  end

  describe '#hash' do
    it '0.9.5.pre1' do
      str = '0.9.5.pre1'
      version = Calabash::Android::Version.new(str)
      expect(str.hash).to be == version.hash
    end

    it '0.9.5.pre' do
      str = '0.9.5.pre'
      version = Calabash::Android::Version.new(str)
      expect(str.hash).to be == version.hash
    end

    it '0.9.5' do
      str = '0.9.5'
      version = Calabash::Android::Version.new(str)
      expect(str.hash).to be == version.hash
    end

    it '0.9' do
      version = Calabash::Android::Version.new("0.9")
      expect("0.9.0".hash).to be == version.hash
    end

    it '0' do
      version = Calabash::Android::Version.new("0")
      expect("0.0.0".hash).to be == version.hash
    end

    it "9 == 9.0 == 9.0.0" do
      version = Calabash::Android::Version.new("9")
      expect("9.0.0".hash).to be == version.hash

      version = Calabash::Android::Version.new("9.0")
      expect("9.0.0".hash).to be == version.hash
    end
  end

  describe '#eql?' do
    it 'mocked' do
      a = Calabash::Android::Version.new('9.9.9')
      b = Calabash::Android::Version.new('0.0.0')

      expect(a).to receive(:hash).and_return(-1, -1)
      expect(b).to receive(:hash).and_return(-1, 0)

      expect(a.eql?(b)).to be == true
      expect(a.eql?(b)).to be == false
    end

    it 'are equal' do

      a = Calabash::Android::Version.new('0.0.0')
      b = Calabash::Android::Version.new('0.0.0')

      expect(a.eql?(b)).to be == true
    end

    it 'are not equal' do

      a = Calabash::Android::Version.new('9.9.9')
      b = Calabash::Android::Version.new('0.0.0')

      expect(a.eql?(b)).to be == false
    end
  end

  describe 'instance as a hash key' do
    it 'keys match' do
      a = Calabash::Android::Version.new('0.0.0')
      b = Calabash::Android::Version.new('0.0.0')

      hash = { a => 'before!' }

      hash[b] = 'after!'
      expect(hash[a]) == 'after!'
    end

    it 'keys do not match' do
      a = Calabash::Android::Version.new('0.0.0')
      b = Calabash::Android::Version.new('9.9.9')

      hash = { a => 'before!' }

      hash[b] = 'after!'
      expect(hash[a]) == 'before!'
      expect(hash[b]) == 'after!'
    end
  end

  describe '==' do
    it 'tests equality' do
      a = Calabash::Android::Version.new('0.9.5')
      b = Calabash::Android::Version.new('0.9.5')
      expect(a == b).to be true

      a = Calabash::Android::Version.new('0.9.5.pre')
      b = Calabash::Android::Version.new('0.9.5.pre')
      expect(a == b).to be true

      a = Calabash::Android::Version.new('0.9.5.pre1')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(a == b).to be true

      a = Calabash::Android::Version.new('0.9.5.pre')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(a == b).to be false

      a = Calabash::Android::Version.new('0.9.5')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(a == b).to be false

      a = Calabash::Android::Version.new("9.0.0")
      b = Calabash::Android::Version.new("9")
      expect(b == a).to be true

      a = Calabash::Android::Version.new("9")
      b = Calabash::Android::Version.new("9.0")
      expect(b == a).to be true

      a = Calabash::Android::Version.new("9.0.0")
      b = Calabash::Android::Version.new("9.0")
      expect(b == a).to be true
    end
  end

  describe '!=' do
    it 'tests not equal' do
      a = Calabash::Android::Version.new('0.9.4')
      b = Calabash::Android::Version.new('0.9.5')
      expect(a != b).to be true

      a = Calabash::Android::Version.new('0.9.5')
      b = Calabash::Android::Version.new('0.9.5.pre')
      expect(a != b).to be true

      a = Calabash::Android::Version.new('0.9.5')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(a != b).to be true

      a = Calabash::Android::Version.new('0.9.5.pre')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(a != b).to be true

      a = Calabash::Android::Version.new('0.9.5.pre1')
      b = Calabash::Android::Version.new('0.9.5.pre2')
      expect(a != b).to be true
    end
  end

  describe '<' do
    it 'tests less than' do
      a = Calabash::Android::Version.new('0.9.4')
      b = Calabash::Android::Version.new('0.9.5')
      expect(a < b).to be true

      a = Calabash::Android::Version.new('0.9.5')
      b = Calabash::Android::Version.new('0.9.5.pre')
      expect(a > b).to be true

      a = Calabash::Android::Version.new('0.9.5')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(a > b).to be true

      a = Calabash::Android::Version.new('0.9.5.pre')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(a < b).to be true

      a = Calabash::Android::Version.new('0.9.5.pre1')
      b = Calabash::Android::Version.new('0.9.5.pre2')
      expect(a < b).to be true
    end
  end

  describe '>' do
    it 'tests greater than' do
      a = Calabash::Android::Version.new('0.9.4')
      b = Calabash::Android::Version.new('0.9.5')
      expect(b > a).to be true

      a = Calabash::Android::Version.new('0.9.5')
      b = Calabash::Android::Version.new('0.9.5.pre')
      expect(b < a).to be true

      a = Calabash::Android::Version.new('0.9.5')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(b < a).to be true

      a = Calabash::Android::Version.new('0.9.5.pre')
      b = Calabash::Android::Version.new('0.9.5.pre1')
      expect(b > a).to be true

      a = Calabash::Android::Version.new('0.9.5.pre1')
      b = Calabash::Android::Version.new('0.9.5.pre2')
      expect(b > a).to be true

      a = Calabash::Android::Version.new("8.0")
      b = Calabash::Android::Version.new("9.0")
      expect(a > b).to be false

      a = Calabash::Android::Version.new("9.0")
      b = Calabash::Android::Version.new("8.0")
      expect(a > b).to be true
    end
  end

  describe '<=' do
    it 'tests less-than or equal' do
      a = Calabash::Android::Version.new('0.9.4')
      b = Calabash::Android::Version.new('0.9.5')
      expect(a <= b).to be true
      a = Calabash::Android::Version.new('0.9.5')
      expect(a <= b).to be true

      a = Calabash::Android::Version.new("9.0.0")
      b = Calabash::Android::Version.new("9")
      expect(b <= a).to be true

      a = Calabash::Android::Version.new("9.0")
      b = Calabash::Android::Version.new("9")
      expect(b <= a).to be true

      a = Calabash::Android::Version.new("9.0.0")
      b = Calabash::Android::Version.new("9.0")
      expect(b <= a).to be true
    end
  end

  describe '>=' do
    it 'tests greater-than or equal' do
      a = Calabash::Android::Version.new('0.9.4')
      b = Calabash::Android::Version.new('0.9.5')
      expect(b >= a).to be true
      a = Calabash::Android::Version.new('0.9.5')
      expect(b >= a).to be true

      a = Calabash::Android::Version.new("9.0.0")
      b = Calabash::Android::Version.new("9")
      expect(b >= a).to be true

      a = Calabash::Android::Version.new("9.0")
      b = Calabash::Android::Version.new("9")
      expect(b >= a).to be true

      a = Calabash::Android::Version.new("9.0.0")
      b = Calabash::Android::Version.new("9.0")
      expect(b >= a).to be true
    end
  end

  describe '.compare' do
    subject(:a) {  Calabash::Android::Version.new('6.0')  }
    it 'works if there is no patch level' do
      b = Calabash::Android::Version.new('5.1.1')
      expect(Calabash::Android::Version.compare(a, b)).to be  == 1
      expect(Calabash::Android::Version.compare(b, a)).to be  == -1
    end

    it 'works if there is no minor level' do
      b = Calabash::Android::Version.new('5')
      expect(Calabash::Android::Version.compare(a, b)).to be == 1
      expect(Calabash::Android::Version.compare(b, a)).to be == -1
    end
  end
end
