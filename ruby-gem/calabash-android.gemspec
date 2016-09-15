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
  s.executables   = "calabash-android"
  s.require_paths = ["lib"]
  s.files         = lambda do
      # This should be in lib/calabash-android somewhere
      ["irbrc"] +
      Dir.glob("bin/**/*.rb") + ["bin/calabash-android"] +
      Dir.glob('lib/**/*.rb') +
      ["lib/calabash-android/deprecated_actions.map",
      "lib/calabash-android/removed_actions.txt"] +
      Dir.glob("test-server/calabash-js/src/*.js") +
      Dir.glob("lib/**/*.jar") +
      Dir.glob("features-skeleton/**/*.*") +
      ["epl-v10.html", "LICENSE"] +
      ["lib/calabash-android/lib/TestServer.apk",
       "lib/calabash-android/lib/AndroidManifest.xml"]
  end.call

  s.add_dependency( "cucumber" )
  s.add_dependency( "json", '~> 1.8' )
  s.add_dependency( "slowhandcuke", '~> 0.0.3')
  s.add_dependency( "rubyzip", "~> 1.1" )
  s.add_dependency( "awesome_print", '~> 1.2')
  s.add_dependency( 'httpclient', '>= 2.7.1', '< 3.0')
  s.add_dependency( 'escape', '~> 0.0.4')
  s.add_dependency( 'luffa' )

  s.add_development_dependency( 'rake', '~> 10.3' )
  s.add_development_dependency( 'yard', '~> 0.8' )
  puts RUBY_PLATFORM
  if RUBY_PLATFORM[/darwin/] || RUBY_PLATFORM["linux"]
    s.add_development_dependency( 'redcarpet', '~> 3.1' )
  end
  s.add_development_dependency( "rspec_junit_formatter" )
  s.add_development_dependency( "rspec", "~> 3.0" )
  s.add_development_dependency( "pry" )
  s.add_development_dependency( "pry-nav" )
  s.add_development_dependency( "guard-rspec" )
  s.add_development_dependency( "guard-bundler" )
  # Pin to 3.0.6; >= 3.1.0 requires ruby 2.2. This is guard dependency.
  s.add_development_dependency("listen", "3.0.6")
  s.add_development_dependency( "growl" )
  s.add_development_dependency( "stub_env" )
end
