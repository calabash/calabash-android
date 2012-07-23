  def package_name(app)
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
    "8"
  end

  def manifest(app)
    require 'tmpdir'
    dir = Dir.mktmpdir
    FileUtils.cp(app, File.join(dir, "app.apk"))

    system "unzip -d #{dir} #{app} AndroidManifest.xml"

    `java -jar #{File.dirname(__FILE__)}/../lib/calabash-android/lib/AXMLPrinter2.jar #{dir}/AndroidManifest.xml`
  end

