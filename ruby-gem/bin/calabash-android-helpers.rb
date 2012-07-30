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
  Usage: calabash-android <command-name> [parameters] [options]
  <command-name> can be one of
    help
      prints more detailed help information.
    gen
      generate a features folder structure.
    setup 
      sets up the current folder to run calabash against your 
      application.
      Will ask you some questions about you application, development
      environment and key store to user for signing.
    build <apk>
      builds the test server that will be used when testing the app.
      You need to run this command every time you make changes to the app.
    run <apk>
      runs Cucumber in the current folder with the enviroment needed.
    submit
      submits an apk along with your features to www.lesspainful.com

  <options> can be
    -v, --verbose
      Turns on verbose logging
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

def run_setup_if_settings_does_not_exist
  unless File.exists?(".calabash_settings")
    puts "Could not find .calabash_settings."
    puts "Should I run calabash-android setup for you?"
    puts "Please answer yes (y) or no (n)"
    if ['yes', 'y'].include? STDIN.gets.chomp
      calabash_setup
    else
      puts "Please run: calabash-android setup"
      exit 1
    end
  end
end

