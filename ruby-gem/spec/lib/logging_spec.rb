require 'stringio'

describe Calabash::Android::Logging do
  describe ".colorize" do
    it "does nothing in win32 environments" do
      expect(Calabash::Android::Environment).to receive(:windows?).and_return true

      actual = Calabash::Android::Logging.send(:colorize, "string", 32)
      expect(actual).to be == "string"
    end

    it "does nothing on the XTC" do
      expect(Calabash::Android::Environment).to receive(:windows?).and_return false
      expect(Calabash::Android::Environment).to receive(:xtc?).and_return true

      actual = Calabash::Android::Logging.send(:colorize, "string", 32)
      expect(actual).to be == "string"
    end

    it "applies the color" do
      expect(Calabash::Android::Environment).to receive(:windows?).and_return false
      expect(Calabash::Android::Environment).to receive(:xtc?).and_return false

      actual = Calabash::Android::Logging.send(:colorize, "string", 32)
      expect(actual[/32/, 0]).not_to be == nil
    end
  end

  describe "logging" do
    before do
      allow(Calabash::Android::Environment).to receive(:debug?).and_return true
    end

    it ".log_warn" do
      Calabash::Android::Logging.log_warn("warn")
    end

    it ".log_debug" do
      Calabash::Android::Logging.log_debug("debug")
    end

    it ".log_error" do
      Calabash::Android::Logging.log_error("error")
    end

    # .log_info is already taken by the XTC logger. (>_O)
    it ".log_info" do
      Calabash::Android::Logging.log_info("info")
    end
  end

  describe "file logging" do
    let(:now) { Time.now }

    it ".timestamp" do
      expected = now.strftime("%Y-%m-%d_%H-%M-%S")
      expect(Time).to receive(:now).and_return(now)

      actual = Calabash::Android::Logging.send(:timestamp)
      expect(actual).to be == expected
    end

    describe "logs directory and calabash.log" do
      let(:tmp_dir) { File.expand_path("tmp") }
      let(:log_file) { File.join(tmp_dir, "logs", "calabash.log") }

      before do
        FileUtils.rm_rf(tmp_dir)
        allow(Calabash::Android::DotDir).to receive(:directory).and_return(tmp_dir)
      end

      it ".logs_directory" do
        actual = Calabash::Android::Logging.send(:logs_directory)
        expect(actual).to be == File.join(tmp_dir, "logs")
        expect(File.exist?(actual)).to be_truthy
      end

      describe ".calabash_log_file" do
        it "creates the log file if it does not exist" do
          expect(FileUtils).to receive(:touch).and_call_original

          actual = Calabash::Android::Logging.send(:calabash_log_file)
          expect(actual).to be == log_file
          expect(File.exist?(actual)).to be_truthy
        end

        it "returns the log file if it does exist" do
          FileUtils.mkdir_p(File.join(tmp_dir, "logs"))
          FileUtils.touch(log_file)
          expect(FileUtils).not_to receive(:touch).with(log_file)

          actual = Calabash::Android::Logging.send(:calabash_log_file)
          expect(actual).to be == log_file
          expect(File.exist?(actual)).to be_truthy
        end
      end

      describe ".log_to_file" do

        before do
          allow(Calabash::Android::Logging).to receive(:timestamp).and_return("stamp")
        end

        it "appends message to log file" do
          Calabash::Android::Logging.log_to_file("Pushing mid")
          lines = File.read(log_file).force_encoding("utf-8").split($-0)

          expect(lines.count).to be == 1
          expect(lines[0]).to be == "stamp Pushing mid"

          Calabash::Android::Logging.log_to_file("Get over here!")
          lines = File.read(log_file).force_encoding("utf-8").split($-0)

          expect(lines.count).to be == 2
          expect(lines[0]).to be == "stamp Pushing mid"
          expect(lines[1]).to be == "stamp Get over here!"
        end

        it "splits multiline messages into lines" do
          lines = [
            "Pushing mid",
            "Get over here!"
          ].join($-0)

          Calabash::Android::Logging.log_to_file(lines)
          lines = File.read(log_file).force_encoding("utf-8").split($-0)

          expect(lines.count).to be == 2
          expect(lines[0]).to be == "stamp Pushing mid"
          expect(lines[1]).to be == "stamp Get over here!"
        end

        it "handles errors by logging when debugging" do
          allow(Calabash::Android::Environment).to receive(:debug?).and_return true
          expect(File).to receive(:open).and_raise StandardError, "Did not get the last hit"

           actual = capture_stdout do
             Calabash::Android::Logging.log_to_file("message")
           end.string.gsub(/\e\[(\d+)m/, "")

           expected = "DEBUG: Could not write:\n\nmessage\n\nto calabash.log because:\n\nDid not get the last hit\n\n"

           actual.gsub!($-0, "")
           expected.gsub!($-0, "")

           expect(actual).to be == expected
        end

        it "handles errors by ignoring them when not debugging" do
          allow(Calabash::Android::Environment).to receive(:debug?).and_return false
          expect(File).to receive(:open).and_raise StandardError, "Did not get the last hit"

           actual = capture_stdout do
             Calabash::Android::Logging.log_to_file("message")
           end.string.gsub(/\e\[(\d+)m/, "")

           expect(actual).to be == ""
        end
      end
    end

    describe ".log_to_file" do
      it "appends log file" do

      end

      it "does not fail when errors are raised" do

      end
    end
  end
end

