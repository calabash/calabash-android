Installation
============

For an in-depth guide see [the Xamarin docs for Calabash installation](https://developer.xamarin.com/guides/testcloud/calabash/configuring/)

### Calabash Sandbox

If you are new to Ruby or gem management, we suggest you use the [Calabash sandbox](https://github.com/calabash/install)

### Prerequisites
You need to have Ruby installed. Verify your installation by running ruby -v in a terminal - it should print "ruby 2.0.0" (or higher). We recommend using a managed version of Ruby like rbenv or rvm.

If you are on Windows you can get Ruby from [RubyInstaller.org](http://rubyinstaller.org/)

You'll also need to have the Java Development Kit (JDK) installed and available. Calabash will attempt to automatically find this from registry keys on windows, or monodroid config elsewhere, but you can also specify it explicitly by setting the `JAVA_HOME` environment variable to its location (e.g. C:\Program Files\Java\jdk1.8.0_20), or having the JDK binaries themselves (i.e. C:\Program Files\Java\jdk1.8.0_20\bin) in your path. 

You should have the Android SDK installed. You can download it from [here](http://developer.android.com/sdk/index.html).  Create an environment variable with the name : `ANDROID_HOME` and its value pointing to the location of the unzipped downloaded SDK.

To compile Calabash-Android from source, you will also need to have Ant installed and added to your path. It can be downloaded from [here](https://ant.apache.org/bindownload.cgi).

### Installation

We recommend *always* using Bundler to manage your version of Calabash.

Install `bundler` by running

 - `gem install bundler`

Create a file called "Gemfile" in the working directory. The Gemfile will contain all your Ruby dependencies.

As an example:

```ruby
# Contents of Gemfile
source "https://rubygems.org"

gem 'calabash-android'
gem 'cucumber'
```

Install using `bundle install`. Remeber to regularly update your Calabash/Cucumber dependencies by running `bundle update`.

Run *all* commands by prefixing `bundler exec`, e.g. `bundle exec calabash-android run ...` or `bundle exec calabash-android console`...

You should *never* install gems with sudo. If you are having issues installing bundler, we recommend using the Calabash Sandbox or using a managed version of Ruby, e.g. rbenv or rvm.
