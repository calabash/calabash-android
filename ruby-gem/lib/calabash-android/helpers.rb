require 'rexml/document'
require 'rexml/xpath'

include REXML

def package_name(app)
  require 'rexml/document'
  require 'rexml/xpath'

  manifest = Document.new(manifest(app))
  manifest.root.attributes['package']
end
  
def main_activity(app)
  manifest = Document.new(manifest(app))
  main_activity = manifest.elements["//action[@name='android.intent.action.MAIN']/../.."].attributes['name']
  #Handle situation where main activity is on the form '.class_name'
  if main_activity.start_with? "."
    main_activity = package_name(app) + main_activity
  elsif not main_activity.include? "." #This is undocumentet behaviour but Android seems to accept shorthand naming that does not start with '.'
    main_activity = "#{package_name(app)}.#{main_activity}"
  end
  main_activity
end

def api_level
  formatted_android_home = ENV["ANDROID_HOME"].gsub("\\", "/")
  api_levels = Dir["#{formatted_android_home}/platforms/android-*"].collect{|platform| platform.split("-").last.to_i}.sort
  if api_levels.empty?
    raise "Android SDK not found. Please install one of more using #{ENV["ANDROID_HOME"]}/tools/android"
  end

  api_levels = api_levels.find_all {|l| l > 7}
  if api_levels.empty?
    raise "Android SDK above 7 not found. Please install one of more using #{ENV["ANDROID_HOME"]}/tools/android"
  end
  api_levels.first
end

def manifest(app)
  `java -jar "#{File.dirname(__FILE__)}/lib/manifest_extractor.jar" "#{app}"`
end

def checksum(file_path)
  require 'digest/md5'
  Digest::MD5.hexdigest(File.read(file_path))
end

def test_server_path(apk_file_path)
  "test_servers/#{checksum(apk_file_path)}_#{Calabash::Android::VERSION}.apk"
end


