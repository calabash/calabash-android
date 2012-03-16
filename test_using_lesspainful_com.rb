#!/usr/bin/env ruby -wKU
require 'tempfile'
require 'json'

def is_json?(str)
  str[0..0] == '{'
end

if ARGV.size != 2
  puts "Usage: ./test_using_lesspainful_com apk_file secret"
  exit
end

apk_file = ARGV[0]
puts "No such file '#{apk_file}'" unless File.exist?(apk_file)
secret = ARGV[1]

archive_path = "#{Tempfile.new("archive").path}.zip"
puts "Creating zip file"
system ("zip -r -o #{archive_path} features && zip -j #{archive_path} bin/Test.apk")

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

