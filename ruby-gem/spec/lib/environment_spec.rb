describe Calabash::Android::Environment do

  describe ".user_home_directory" do
    it "always returns a directory that exists" do
      expect(File.exist?(Calabash::Android::Environment.user_home_directory)).to be_truthy
    end

    it "returns local ./tmp/home on the XTC" do
      expect(Calabash::Android::Environment).to receive(:xtc?).and_return true

      expected = File.join("./", "tmp", "home")
      expect(Calabash::Android::Environment.user_home_directory).to be == expected
      expect(File.exist?(expected)).to be_truthy
    end

    describe "Windows" do

      before do
        expect(Calabash::Android::Environment).to receive(:windows?).and_return true
      end

      it "returns HOME if it is defined" do
        stub_env({"HOME" => "C:\\my\\home\\dir"})
        expect(File).to receive(:exist?).with("C:\\my\\home\\dir").and_return true

        expect(Calabash::Android::Environment.user_home_directory).to be == "C:\\my\\home\\dir"
      end

      it "returns USERPROFILE if HOME is not defined" do
        stub_env({"USERPROFILE" => "C:\\my\\home\\dir", "HOME" => nil})
        expect(File).to receive(:exist?).with("C:\\my\\home\\dir").and_return true

        expect(Calabash::Android::Environment.user_home_directory).to be == "C:\\my\\home\\dir"
      end
    end
  end

  describe '.debug?' do
    it "returns true when DEBUG == '1'" do
      stub_env('DEBUG', '1')
      expect(Calabash::Android::Environment.debug?).to be == true
    end

    it "returns false when DEBUG != '1'" do
      stub_env('DEBUG', 1)
      expect(Calabash::Android::Environment.debug?).to be == false
    end
  end

  describe '.xtc?' do
    it "returns true when XAMARIN_TEST_CLOUD == '1'" do
      stub_env('XAMARIN_TEST_CLOUD', '1')
      expect(Calabash::Android::Environment.xtc?).to be == true
    end

    it "returns false when XAMARIN_TEST_CLOUD != '1'" do
      stub_env('XAMARIN_TEST_CLOUD', 1)
      expect(Calabash::Android::Environment.xtc?).to be == false
    end
  end

  describe ".jenkins?" do
    it "returns true if JENKINS_HOME defined" do
      stub_env({"JENKINS_HOME" => "/Users/Shared/Jenkins"})

      expect(Calabash::Android::Environment.jenkins?).to be == true
    end

    describe "returns false if JENKINS_HOME" do
      it "is nil" do
        stub_env({"JENKINS_HOME" => nil})

        expect(Calabash::Android::Environment.jenkins?).to be == false
      end

      it "is empty string" do
        stub_env({"JENKINS_HOME" => ""})

        expect(Calabash::Android::Environment.jenkins?).to be == false
      end
    end
  end

  describe ".travis?" do
    it "returns true if TRAVIS defined" do
      stub_env({"TRAVIS" => "some truthy value"})

      expect(Calabash::Android::Environment.travis?).to be == true
    end

    describe "returns false if TRAVIS" do
      it "is nil" do
        stub_env({"TRAVIS" => nil})

        expect(Calabash::Android::Environment.travis?).to be == false
      end

      it "is empty string" do
        stub_env({"TRAVIS" => ""})

        expect(Calabash::Android::Environment.travis?).to be == false
      end
    end
  end

  describe ".circle_ci?" do
    it "returns true if CIRCLECI defined" do
      stub_env({"CIRCLECI" => true})

      expect(Calabash::Android::Environment.circle_ci?).to be == true
    end

    describe "returns false if CIRCLECI" do
      it "is nil" do
        stub_env({"CIRCLECI" => nil})

        expect(Calabash::Android::Environment.circle_ci?).to be == false
      end

      it "is empty string" do
        stub_env({"CIRCLECI" => ""})

        expect(Calabash::Android::Environment.circle_ci?).to be == false
      end
    end
  end

  describe ".teamcity?" do
    it "returns true if TEAMCITY_PROJECT_NAME defined" do
      stub_env({"TEAMCITY_PROJECT_NAME" => "project name"})

      expect(Calabash::Android::Environment.teamcity?).to be == true
    end

    describe "returns false if TEAMCITY_PROJECT_NAME" do
      it "is nil" do
        stub_env({"TEAMCITY_PROJECT_NAME" => nil})

        expect(Calabash::Android::Environment.teamcity?).to be == false
      end

      it "is empty string" do
        stub_env({"TEAMCITY_PROJECT_NAME" => ""})

        expect(Calabash::Android::Environment.teamcity?).to be == false
      end
    end
  end

  describe ".gitlab?" do
    it "returns true if GITLAB_CI is defined" do
      stub_env({"GITLAB_CI" => true})

      expect(Calabash::Android::Environment.gitlab?).to be == true
    end

    describe "returns false if GITLAB_CI undefined or empty" do
      it "is nil" do
        stub_env({"GITLAB_CI" => nil})

        expect(Calabash::Android::Environment.gitlab?).to be == false
      end

      it "is empty string" do
        stub_env({"GITLAB_CI" => ""})

        expect(Calabash::Android::Environment.gitlab?).to be == false
      end
    end
  end

  describe ".ci?" do
    describe "truthy" do
      it "CI" do
        expect(Calabash::Android::Environment).to receive(:jenkins?).and_return false
        expect(Calabash::Android::Environment).to receive(:travis?).and_return false
        expect(Calabash::Android::Environment).to receive(:circle_ci?).and_return false
        expect(Calabash::Android::Environment).to receive(:teamcity?).and_return false
        expect(Calabash::Android::Environment).to receive(:gitlab?).and_return false
        expect(Calabash::Android::Environment).to receive(:ci_var_defined?).and_return true

        expect(Calabash::Android::Environment.ci?).to be == true
      end

      it "Jenkins" do
        expect(Calabash::Android::Environment).to receive(:jenkins?).and_return true
        expect(Calabash::Android::Environment).to receive(:travis?).and_return false
        expect(Calabash::Android::Environment).to receive(:circle_ci?).and_return false
        expect(Calabash::Android::Environment).to receive(:teamcity?).and_return false
        expect(Calabash::Android::Environment).to receive(:gitlab?).and_return false
        expect(Calabash::Android::Environment).to receive(:ci_var_defined?).and_return false

        expect(Calabash::Android::Environment.ci?).to be == true
      end

      it "Travis" do
        expect(Calabash::Android::Environment).to receive(:jenkins?).and_return false
        expect(Calabash::Android::Environment).to receive(:travis?).and_return true
        expect(Calabash::Android::Environment).to receive(:circle_ci?).and_return false
        expect(Calabash::Android::Environment).to receive(:teamcity?).and_return false
        expect(Calabash::Android::Environment).to receive(:gitlab?).and_return false
        expect(Calabash::Android::Environment).to receive(:ci_var_defined?).and_return false

        expect(Calabash::Android::Environment.ci?).to be == true
      end

      it "Circle CI" do
        expect(Calabash::Android::Environment).to receive(:jenkins?).and_return false
        expect(Calabash::Android::Environment).to receive(:travis?).and_return false
        expect(Calabash::Android::Environment).to receive(:circle_ci?).and_return true
        expect(Calabash::Android::Environment).to receive(:teamcity?).and_return false
        expect(Calabash::Android::Environment).to receive(:gitlab?).and_return false
        expect(Calabash::Android::Environment).to receive(:ci_var_defined?).and_return false

        expect(Calabash::Android::Environment.ci?).to be == true
      end

      it "TeamCity" do
        expect(Calabash::Android::Environment).to receive(:jenkins?).and_return false
        expect(Calabash::Android::Environment).to receive(:travis?).and_return false
        expect(Calabash::Android::Environment).to receive(:circle_ci?).and_return false
        expect(Calabash::Android::Environment).to receive(:teamcity?).and_return true
        expect(Calabash::Android::Environment).to receive(:gitlab?).and_return false
        expect(Calabash::Android::Environment).to receive(:ci_var_defined?).and_return false

        expect(Calabash::Android::Environment.ci?).to be == true
      end

      it "GitLab" do
        expect(Calabash::Android::Environment).to receive(:jenkins?).and_return false
        expect(Calabash::Android::Environment).to receive(:travis?).and_return false
        expect(Calabash::Android::Environment).to receive(:circle_ci?).and_return false
        expect(Calabash::Android::Environment).to receive(:teamcity?).and_return false
        expect(Calabash::Android::Environment).to receive(:gitlab?).and_return true
        expect(Calabash::Android::Environment).to receive(:ci_var_defined?).and_return false

        expect(Calabash::Android::Environment.ci?).to be == true
      end
    end

    it "falsey" do
      expect(Calabash::Android::Environment).to receive(:jenkins?).and_return false
      expect(Calabash::Android::Environment).to receive(:travis?).and_return false
      expect(Calabash::Android::Environment).to receive(:circle_ci?).and_return false
      expect(Calabash::Android::Environment).to receive(:teamcity?).and_return false
      expect(Calabash::Android::Environment).to receive(:gitlab?).and_return false
      expect(Calabash::Android::Environment).to receive(:ci_var_defined?).and_return false

      expect(Calabash::Android::Environment.ci?).to be == false
    end
  end

  # private

  describe ".ci_var_defined?" do
    it "returns true if CI defined" do
      stub_env({"CI" => true})

      expect(Calabash::Android::Environment.send(:ci_var_defined?)).to be == true
    end

    describe "returns false if CI" do
      it "is nil" do
        stub_env({"CI" => nil})

        expect(Calabash::Android::Environment.send(:ci_var_defined?)).to be == false
      end

      it "is empty string" do
        stub_env({"CI" => ""})

        expect(Calabash::Android::Environment.send(:ci_var_defined?)).to be == false
      end
    end
  end
end

