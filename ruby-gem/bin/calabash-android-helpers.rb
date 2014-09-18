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
      sets up a non-default keystore to use with this test project.
    resign <apk>
      resigns the app with the currently configured keystore.
    build <apk>
      builds the test server that will be used when testing the app.
    run <apk>
      runs Cucumber in the current folder with the environment needed.
    version
      prints the gem version

  <options> can be
    -v, --verbose
      Turns on verbose logging
EOF
end

def print_help
  print_usage
end

def is_json?(str)
  str[0..0] == '{'
end
