require 'bundler'
load 'lib/calabash-android/env.rb'

# Monkey patch 'log' for env
class Env
  def self.log(message, error = false)
    $stdout.puts "#{Time.now.strftime("%Y-%m-%d %H:%M:%S")} - #{message}"
  end
end

def build
  test_server_template_dir = find_server_repo_or_raise

  Dir.mktmpdir do |workspace_dir|
    @test_server_dir = File.join(workspace_dir, 'calabash-android-server', 'server')
    FileUtils.cp_r(test_server_template_dir, workspace_dir)

    args = [
      Env.ant_path,
      "clean",
      "package",
      "-debug",
      "-Dtools.dir=\"#{Env.tools_dir}\"",
      "-Dandroid.api.level=19",
      "-Dversion=#{Calabash::Android::VERSION}",
    ]
    Dir.chdir(@test_server_dir) do
      STDOUT.sync = true
      IO.popen(args.join(" ")) do |io|
        io.each { |s| print s }
      end
      if $?.exitstatus != 0
        puts "Could not build the test server. Please see the output above."
        exit $?.exitstatus
      end
    end

    FileUtils.mkdir_p "test_servers" unless File.exist? "test_servers"

    FileUtils.cp(File.join(@test_server_dir, "bin", "Test_unsigned.apk"), File.join(File.dirname(__FILE__), 'lib/calabash-android/lib/TestServer.apk'))
    FileUtils.cp(File.join(@test_server_dir, "AndroidManifest.xml"), File.join(File.dirname(__FILE__), 'lib/calabash-android/lib/AndroidManifest.xml'))
  end
end

task :build do
  build
end

def find_server_repo_or_raise
  calabash_server_dir = ENV['CALABASH_SERVER_PATH'] || File.join(File.dirname(__FILE__), '..', '..', 'calabash-android-server')
  unless File.exist?(calabash_server_dir) && File.exists?(File.join(calabash_server_dir, "server", "calabash-js", "src"))
    raise %Q{\033[31m
Expected to find the calabash-android-server repo at:

    #{File.expand_path(calabash_server_dir)}

Either clone the repo to that location with:

$ git clone --recursive git@github.com:calabash/calabash-android-server.git #{calabash_server_dir}

or set CALABASH_SERVER_PATH to point to your local copy of the server.

$ CALABASH_SERVER_PATH=/path/to/server bundle exec rake build

For full instuctions see: https://github.com/calabash/calabash-android/wiki/Building-calabash-android\033[0m

    }
  end
  calabash_server_dir
end


Bundler::GemHelper.install_tasks

begin
  require "rspec/core/rake_task"
  RSpec::Core::RakeTask.new(:spec) do |task|
    task.pattern = "spec/lib/**{,/*/**}/*_spec.rb"
  end

  RSpec::Core::RakeTask.new(:unit) do |task|
    task.pattern = "spec/lib/**{,/*/**}/*_spec.rb"
  end

  RSpec::Core::RakeTask.new(:integration) do |task|
    task.pattern = "spec/integration/**{,/*/**}/*_spec.rb"
  end
rescue LoadError => _
end

begin
  require "yard"
  YARD::Rake::YardocTask.new do |t|
    # see .yardopts for options
  end
rescue LoadError => _
end

