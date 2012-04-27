require 'tempfile'
require 'json'
require "rubygems"


def msg(title, &block)
  puts "\n" + "-"*10 + title + "-"*10
  block.call
  puts "-"*10 + "-------" + "-"*10 + "\n"
end

def print_usage
  puts <<EOF
  Usage: calabash-android <command-name> [parameters]
  <command-name> can be one of
    help
      prints more detailed help information.
    gen
      generate a features folder structure.
    setup will ask you some questions about you application, development
      environment and key store to user for signing.

    build builds the test server that will be used when testing the app.
      You need to run this command everytime you make changes to app.

    run runs Cucumber in the current folder with the enviroment needed.
EOF
end

def print_help
  file = File.join(File.dirname(__FILE__), '..', 'doc', 'calabash-android-help.txt')
  #system("less #{file}")
  #TODO
  print_usage
end


def is_json?(str)
  str[0..0] == '{'
end

def calabash_submit(args)
  if args.size < 2
   msg("Error") do
     puts "You must supply at path to apk and secret."
   end
   exit 1
  end

  apk_file = ARGV[0]
  puts "No such file '#{apk_file}'" unless File.exist?(apk_file)
  secret = ARGV[1]

  archive_path = "#{Tempfile.new("archive").path}.zip"
  puts "Creating zip file"
  system("zip -r -o #{archive_path} features && zip -j #{archive_path} bin/Test.apk")

  puts "Uploading apk and features to www.lesspainful.com"
  result = `curl -F "secret=#{secret}" -F "app=@#{apk_file}" -F "env=@#{archive_path}" https://www.lesspainful.com/cmd_upload`

  if is_json? result
    json_result = JSON.parse(result)
    puts "Test status is '#{json_result['status']}"
    puts "Test id is '#{json_result['id']}"
    puts "You can see the test result here: #{json_result['url']}"
    puts "You can pull the status by using this command:"
    puts "curl -F \"secret=#{secret}\" -F \"id=#{json_result['id']}\" https://www.lesspainful.com/cmd_status"
  else
    puts result
  end

end

