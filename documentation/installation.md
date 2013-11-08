Installation
============
### Prerequisites

You need to have Ruby installed. Verify your installation by running ruby -v in a terminal - it should print "ruby 1.8.7" (or higher).

If you are on Windows you can get Ruby from [RubyInstaller.org](http://rubyinstaller.org/)

You should have the Android SDK installed and the environment variable `ANDROID_HOME` should be pointing to it.

You also need to have Ant installed and added to your path

### Installation

Install `calabash-android` by running

- `gem install calabash-android`
- You might have to run `sudo gem install calabash-android` if you do not have the right permissions.



Troubleshooting Installation
----------------------------

### Mac

If you are on Mac you may see an error like this:

    ~$ sudo gem install calabash-android
    Password:
    Building native extensions.  This could take a while...
    ERROR:  Error installing calabash-android:
  ERROR: Failed to build gem native extension.

    /System/Library/Frameworks/Ruby.framework/Versions/1.8/usr/bin/ruby extconf.rb
    mkmf.rb can't find header files for ruby at /System/Library/Frameworks/Ruby.framework/Versions/1.8/usr/lib/ruby/ruby.h

One possible cause can be not having the correct Command Line Tools (compiler
tool chain) for your OS X release. For example, for OS X 10.8
"Mountain Lion" you need the "Mountain Lion" version of these. If you
have [Xcode](http://developer.apple.com/xcode/) installed you can
install them from it's Preferences pane (in the Download tab).
Otherwise you can download the Command Line Tools for you OS X version
from the [Apple Developer web site](http://developer.apple.com/downloads/index.action).

### Ubuntu

These instructions assume you'll be using Ruby 1.9.1.

Installing Ruby:

    sudo apt-get install ruby1.9.1

You might need to install the dev package when installing calabash:

    sudo apt-get isntall ruby1.9.1-dev

If you see something like this while installing calabash:

    cannot load such file -- rspec/expectations (LoadError)

It's because the rspec package isn't installed:

    gem install rspec
