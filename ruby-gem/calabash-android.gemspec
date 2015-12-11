# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "calabash-android/version"

Gem::Specification.new do |s|
  s.name        = "calabash-android"
  s.version     = Calabash::Android::VERSION
  s.platform    = Gem::Platform::RUBY
  s.license     = "EPL-1.0"
  s.authors     = ["Jonas Maturana Larsen"]
  s.email       = ["jonas@lesspainful.com"]
  s.homepage    = "http://github.com/calabash"
  s.summary     = %q{Client for calabash-android for automated functional testing on Android}
  s.description = %q{calabash-android drives tests for native  and hybrid Android apps. }
  s.files         = `git ls-files | grep -v "test-server/instrumentation-backend"`.split("\n") + Dir["test-server/calabash-js/src/*.js"] + ["lib/calabash-android/lib/TestServer.apk"]
  s.executables   = "calabash-android"
  s.require_paths = ["lib"]

  s.add_dependency( "cucumber" )
  s.add_dependency( "json", '~> 1.8' )
  s.add_dependency( 'retriable', '>= 1.3.3.1', '< 1.5')
  s.add_dependency( "slowhandcuke", '~> 0.0.3')
  s.add_dependency( "rubyzip", "~> 1.1" )
  s.add_dependency( "awesome_print", '~> 1.2')
  s.add_dependency( 'httpclient', '>= 2.3.2', '< 3.0')
  s.add_dependency( 'escape', '~> 0.0.4')

  s.add_development_dependency( 'rake', '~> 10.3' )
  s.add_development_dependency( 'yard', '~> 0.8' )
  s.add_development_dependency( 'redcarpet', '~> 3.1' )
  s.add_development_dependency( "rspec_junit_formatter" )
  s.add_development_dependency( "rspec", "~> 3.0" )
  s.add_development_dependency( "pry" )
  s.add_development_dependency( "pry-nav" )
  s.add_development_dependency( "guard-rspec" )
  s.add_development_dependency( "guard-bundler" )
  s.add_development_dependency( "growl" )
  s.add_development_dependency( "stub_env" )
end
