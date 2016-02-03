#! /usr/bin/env ruby
require 'fileutils'

devices = "0746d8d6"
series = "master"
locale = "en_US"
user = ENV['XTC_USER'] || raise("Please set 'XTC_USER'")
api_key = ENV['XTC_API_KEY'] || raise("Please set 'XTC_API_KEY'")

FileUtils.rm_rf('test_servers')

File.delete('./Gemfile') if File.exist?('./Gemfile')
File.delete('./Gemfile.lock') if File.exist?('./Gemfile.lock')

require File.join(File.dirname(__FILE__), '..', 'lib', 'calabash-android', 'version.rb')

gemfile = File.read('./Gemfile.template')
gemfile.sub!('##CALABASH_ANDROID_VERSION##', Calabash::Android::VERSION)

File.open('./Gemfile', 'w+') do |file|
  file.write(gemfile)
end

system('bundle install')

system('bundle exec calabash-android build ./features/support/apps/general.apk')

exec('bundle', 'exec', 'test-cloud', 'submit', './features/support/apps/general.apk', api_key, '--devices', devices,
      '--series', series, '--locale', locale, '--user', user, '--profile', ARGV[0], '--config', './config/cucumber.yml')