require 'bundler'
load 'lib/calabash-android/env.rb'

def build
  test_server_template_dir = File.join(File.dirname(__FILE__), 'test-server')

  Dir.mktmpdir do |workspace_dir|

    @test_server_dir = File.join(workspace_dir, 'test-server')
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
  end
end

task :build do
  unless File.exists? "test-server/calabash-js/src"
    puts "calabash-js not found!"
    puts "For instuctions see: https://github.com/calabash/calabash-android/wiki/Building-calabash-android"
    exit 1
  end

  build
end


Bundler::GemHelper.install_tasks
