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
  api_levels = Dir["#{ENV["ANDROID_HOME"]}/platforms/android-*"].collect{|platform| platform.split("-").last.to_i}.sort
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
  require 'tmpdir'
  dir = Dir.mktmpdir
  FileUtils.cp(app, File.join(dir, "app.apk"))

  system "unzip -d #{dir} #{app} AndroidManifest.xml"

  `java -jar #{File.dirname(__FILE__)}/lib/AXMLPrinter2.jar #{dir}/AndroidManifest.xml`
end

def checksum(file_path)
  require 'digest/md5'
  Digest::MD5.hexdigest(File.read(file_path))
end


