def calabash_submit(args)
  require "rubygems"
  require 'tempfile'
  require 'json'

  if is_windows?
    puts "Submitting to LessPainful.com from Windows is currently not supported"
    exit
  end


  if args.size != 2
    puts "Usage: calabash-android submit apk_file secret"
    exit
  end


  apk_file = args[0]
  puts "No such file '#{apk_file}'" unless File.exist?(apk_file)
  secret = args[1]

  unless File.exist?("features/support/Test.apk")
    puts "No test server in features/support/"
    puts "Please run 'calabash-android build' and then retry"
    exit
  end

  archive_path = "#{Tempfile.new("archive").path}.zip"
  puts "Creating zip file"
  system("zip -r -o #{archive_path} features")

  puts "Uploading to www.lesspainful.com"
  puts 'curl -F "secret=#{secret}" -F "app=@#{apk_file}" -F "env=@#{archive_path}" https://www.lesspainful.com/calabash_android_upload'
  result = `curl -F "secret=#{secret}" -F "app=@#{apk_file}" -F "env=@#{archive_path}" https://www.lesspainful.com/calabash_android_upload`

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