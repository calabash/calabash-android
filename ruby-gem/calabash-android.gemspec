# -*- encoding: utf-8 -*-
$:.push File.expand_path("../lib", __FILE__)
require "calabash-android/version"

Gem::Specification.new do |s|
  s.name        = "calabash-android"
  s.version     = Calabash::Android::VERSION
  s.platform    = Gem::Platform::RUBY
  s.authors     = ["Jonas Maturana Larsen"]
  s.email       = ["jonas@lesspainful.com"]
  s.homepage    = "http://github.com/calabash"
  s.summary     = %q{Client for calabash-android for automated functional testing on Android}
  s.description = %q{calabash-android drives tests for native  and hybrid Android apps. }
  s.files         = `git ls-files`.split("\n") + Dir["test-server/calabash-js/src/*.js"] + ["lib/calabash-android/lib/TestServer.apk"]
  s.executables   = "calabash-android"
  s.require_paths = ["lib"]

  s.add_dependency( "cucumber" )
  s.add_dependency( "json" )
  s.add_dependency( "retriable", "~> 1.3.3.1" )
  s.add_dependency( "slowhandcuke" )
  s.add_dependency( "rubyzip", "~> 0.9.9" )
  s.add_dependency( "awesome_print" )
  s.add_dependency( 'httpclient', '~> 2.3.2')
  s.add_dependency( 'xamarin-test-cloud', '>= 0.9.23')
  s.add_dependency( 'escape', '~> 0.0.4')
end
