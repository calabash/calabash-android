require 'rexml/document'
require 'timeout'

if RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/
    require 'win32/registry'
end

module Calabash
    module Android
        module Dependencies
            private

            class ScanningTimedOutError < RuntimeError; end

            def self.set_android_dependencies(android_dependencies)
                @@android_dependencies = android_dependencies
            end

            def self.set_java_dependencies(java_dependencies)
                @@java_dependencies = java_dependencies
            end

            def self.android_dependencies(key)
                setup unless defined?(@@android_dependencies)

                if @@android_dependencies.has_key?(key)
                    file = @@android_dependencies[key]

                    unless File.exists?(file)
                        raise "No such file '#{file}'"
                    end

                    file
                else
                    raise "No such dependency '#{key}'"
                end
            end

            def self.java_dependencies(key)
                setup unless defined?(@@java_dependencies)

                if key == :ant_path
                    ant_executable
                elsif @@java_dependencies.has_key?(key)
                    file = @@java_dependencies[key]

                    unless File.exists?(file)
                        raise "No such file '#{file}'"
                    end

                    file
                else
                    raise "No such dependency '#{key}'"
                end
            end

            public

            def self.adb_path
                android_dependencies(:adb_path)
            end

            def self.aapt_path
                android_dependencies(:aapt_path)
            end

            def self.zipalign_path
                android_dependencies(:zipalign_path)
            end

            def self.android_jar_path
                android_dependencies(:android_jar_path)
            end

            def self.java_path
                java_dependencies(:java_path)
            end

            def self.keytool_path
                java_dependencies(:keytool_path)
            end

            def self.jarsigner_path
                java_dependencies(:jarsigner_path)
            end

            def self.ant_path
                java_dependencies(:ant_path)
            end

            def self.setup
                if ENV['CI_NO_ANDROID_RUNTIME'] == '1'
                    @@android_dependencies = {}
                    @@java_dependencies = {}
                    return
                end

                @@halt_scanning = false
                @@halt_scanning_thread = nil

                if ENV['ANDROID_HOME']
                    android_sdk_location = ENV['ANDROID_HOME']
                    Logging.log_debug("Setting Android SDK location to $ANDROID_HOME")
                else
                    android_sdk_location = detect_android_sdk_location
                end

                if android_sdk_location.nil?
                    Logging.log_error 'Could not find an Android SDK please make sure it is installed.'
                    Logging.log_error 'You can read about how Calabash is searching for an Android SDK and how you can help here:'
                    Logging.log_error 'https://github.com/calabash/calabash-android/blob/master/documentation/installation.md#prerequisites'

                    raise 'Could not find an Android SDK'
                end

                Logging.log_debug("Android SDK location set to '#{android_sdk_location}'")

                @@halt_scanning_thread = Thread.new do
                    sleep 60
                    @@halt_scanning = true
                end

                begin
                    set_android_dependencies(locate_android_dependencies(android_sdk_location))
                rescue ScanningTimedOutError => e
                    Logging.log_error 'Timed out locating Android dependency'
                    Logging.log_error 'You can read about how Calabash is searching for an Android SDK and how you can help here:'
                    Logging.log_error 'https://github.com/calabash/calabash-android/blob/master/documentation/installation.md#prerequisites'

                    raise e.message
                rescue Environment::InvalidEnvironmentError => e
                    Logging.log_error 'Could not locate Android dependency'
                    Logging.log_error 'You can read about how Calabash is searching for an Android SDK and how you can help here:'
                    Logging.log_error 'https://github.com/calabash/calabash-android/blob/master/documentation/installation.md#prerequisites'

                    raise e
                end

                if ENV['JAVA_HOME']
                    java_sdk_home = ENV['JAVA_HOME']
                    Logging.log_debug("Setting Java SDK location to $JAVA_HOME")
                else
                    java_sdk_home = detect_java_sdk_location
                end

                Logging.log_debug("Java SDK location set to '#{java_sdk_home}'")

                Thread.kill(@@halt_scanning_thread) if @@halt_scanning_thread
                @@halt_scanning = false

                @@halt_scanning_thread = Thread.new do
                    sleep 60
                    @@halt_scanning = true
                end

                begin
                    set_java_dependencies(locate_java_dependencies(java_sdk_home))
                rescue ScanningTimedOutError => e
                    Logging.log_error 'Timed out locating Java dependency'
                    Logging.log_error "You can read about how Calabash is searching for a JDK and how you can help here:"
                    Logging.log_error "https://github.com/calabash/calabash-android/blob/master/documentation/installation.md#prerequisites"

                    raise e.message
                rescue Environment::InvalidJavaSDKHome => e
                    Logging.log_error "Could not find Java Development Kit please make sure it is installed."
                    Logging.log_error "You can read about how Calabash is searching for a JDK and how you can help here:"
                    Logging.log_error "https://github.com/calabash/calabash-android/blob/master/documentation/installation.md#prerequisites"

                    raise e
                rescue Environment::InvalidEnvironmentError => e
                    Logging.log_error "Could not find Java dependency"
                    Logging.log_error "You can read about how Calabash is searching for a JDK and how you can help here:"
                    Logging.log_error "https://github.com/calabash/calabash-android/blob/master/documentation/installation.md#prerequisites"

                    raise e
                end

                Thread.kill(@@halt_scanning_thread) if @@halt_scanning_thread
            end

            private

            def self.tools_directory
                tools_directories = tools_directories(ENV['ANDROID_HOME'])

                File.join(ENV['ANDROID_HOME'], tools_directories.first)
            end

            def self.tools_directories(android_sdk_location)
                build_tools_files = list_files(File.join(android_sdk_location, 'build-tools')).select {|file| File.directory?(file)}

                build_tools_directories =
                    build_tools_files.select do |dir|
                        begin
                            Calabash::Android::Version.new(File.basename(dir))
                            true
                        rescue ArgumentError
                            false
                        end
                    end.sort do |a, b|
                        Calabash::Android::Version.compare(Calabash::Android::Version.new(File.basename(a)), Calabash::Android::Version.new(File.basename(b)))
                    end.reverse.map{|dir| File.join('build-tools', File.basename(dir))}

                if build_tools_directories.empty?
                    unless build_tools_files.reverse.first.nil?
                        build_tools_directories = [File.join('build-tools', File.basename(build_tools_files.reverse.first))]
                    end
                end

                build_tools_directories + ['platform-tools', 'tools']
            end

            def self.platform_directory(android_sdk_location)
                files = list_files(File.join(android_sdk_location, 'platforms'))
                            .select {|file| File.directory?(file)}

                sorted_files = files.sort_by {|item| '%08s' % item.split('-').last}.reverse

                if sorted_files.first.nil?
                    raise Environment::InvalidEnvironmentError,
                          "Could not find any platform directory in '#{File.join(android_sdk_location, 'platforms')}'"
                end

                File.join('platforms', File.basename(sorted_files.first))
            end

            def self.locate_android_dependencies(android_sdk_location)
                adb_path = scan_for_path(android_sdk_location, adb_executable, ['platform-tools'])
                aapt_path = scan_for_path(android_sdk_location, aapt_executable, tools_directories(android_sdk_location))
                zipalign_path = scan_for_path(android_sdk_location, zipalign_executable, tools_directories(android_sdk_location))

                if adb_path.nil?
                    raise Environment::InvalidEnvironmentError,
                          "Could not find '#{adb_executable}' in '#{android_sdk_location}'"
                end

                if aapt_path.nil?
                    raise Environment::InvalidEnvironmentError,
                          "Could not find '#{aapt_executable}' in '#{android_sdk_location}'"
                end

                if zipalign_path.nil?
                    raise Environment::InvalidEnvironmentError,
                          "Could not find '#{zipalign_executable}' in '#{android_sdk_location}'"
                end

                Logging.log_debug("Set aapt path to '#{aapt_path}'")
                Logging.log_debug("Set zipalign path to '#{zipalign_path}'")
                Logging.log_debug("Set adb path to '#{adb_path}'")

                android_jar_path = scan_for_path(File.join(android_sdk_location, 'platforms'), 'android.jar', [File.basename(platform_directory(android_sdk_location))])

                if android_jar_path.nil?
                    raise Environment::InvalidEnvironmentError,
                          "Could not find 'android.jar' in '#{File.join(android_sdk_location, 'platforms')}'"
                end

                Logging.log_debug("Set android jar path to '#{android_jar_path}'")

                {
                    aapt_path: aapt_path,
                    zipalign_path: zipalign_path,
                    adb_path: adb_path,
                    android_jar_path: android_jar_path
                }
            end

            def self.locate_java_dependencies(java_sdk_location)
                # For the Java dependencies, we will use the PATH elements of they exist
                on_path = find_executable_on_path(java_executable)

                if on_path
                    Logging.log_debug('Found java on PATH')
                    java_path = on_path
                else
                    if java_sdk_location.nil? || java_sdk_location.empty?
                        raise Environment::InvalidJavaSDKHome,
                              "Could not locate '#{java_executable}' on path, and Java SDK Home is invalid."
                    end

                    java_path = scan_for_path(java_sdk_location, java_executable, ['bin'])
                end

                Logging.log_debug("Set java path to '#{java_path}'")

                on_path = find_executable_on_path(keytool_executable)

                if on_path
                    Logging.log_debug('Found keytool on PATH')
                    keytool_path = on_path
                else
                    if java_sdk_location.nil? || java_sdk_location.empty?
                        raise Environment::InvalidJavaSDKHome,
                              "Could not locate '#{keytool_executable}' on path, and Java SDK Home is invalid."
                    end

                    keytool_path = scan_for_path(java_sdk_location, keytool_executable, ['bin'])
                end

                Logging.log_debug("Set keytool path to '#{keytool_path}'")

                on_path = find_executable_on_path(jarsigner_executable)

                if on_path
                    Logging.log_debug('Found jarsigner on PATH')
                    jarsigner_path = on_path
                else
                    if java_sdk_location.nil? || java_sdk_location.empty?
                        raise Environment::InvalidJavaSDKHome,
                              "Could not locate '#{jarsigner_executable}' on path, and Java SDK Home is invalid."
                    end

                    jarsigner_path = scan_for_path(java_sdk_location, jarsigner_executable, ['bin'])
                end

                Logging.log_debug("Set jarsigner path to '#{jarsigner_path}'")

                if java_path.nil?
                    raise Environment::InvalidEnvironmentError,
                          "Could not find '#{java_executable}' on PATH or in '#{java_sdk_location}'"
                end

                if keytool_path.nil?
                    raise Environment::InvalidEnvironmentError,
                          "Could not find '#{keytool_executable}' on PATH or in '#{java_sdk_location}'"
                end

                if jarsigner_path.nil?
                    raise Environment::InvalidEnvironmentError,
                          "Could not find '#{jarsigner_executable}' on PATH or in '#{java_sdk_location}'"
                end

                {
                    java_path: java_path,
                    keytool_path: keytool_path,
                    jarsigner_path: jarsigner_path
                }
            end

            def self.halt_scanning?
                @@halt_scanning
            end

            def self.scan_for_path(path, file_name, expected_sub_folders = nil)
                if self.halt_scanning?
                    Logging.log_error("Timed out looking for '#{file_name}', currently looking in '#{path}'")
                    raise ScanningTimedOutError, "Timed out looking for '#{file_name}'"
                end

                # Optimization for expected folders
                if expected_sub_folders && !expected_sub_folders.empty?
                    expected_sub_folders.each do |expected_sub_folder|
                        result = scan_for_path(File.join(path, expected_sub_folder), file_name)

                        return result if result
                    end

                    Logging.log_warn("Did not find '#{file_name}' in any standard directory of '#{path}'. Calabash will therefore take longer to load")
                    Logging.log_debug(" - Expected to find '#{file_name}' in any of:")

                    expected_sub_folders.each do |expected_sub_folder|
                        Logging.log_debug(" - #{File.join(path, expected_sub_folder)}")
                    end
                end

                files = list_files(path).sort.reverse

                if files.reject{|file| File.directory?(file)}.
                    map{|file| File.basename(file)}.include?(file_name)
                    return File.join(path, file_name)
                else
                    files.select{|file| File.directory?(file)}.each do |dir|
                        result = scan_for_path(dir, file_name)

                        return result if result
                    end
                end

                nil
            end

            def self.detect_android_sdk_location
                if File.exist?(monodroid_config_file)
                    sdk_location = read_attribute_from_monodroid_config('android-sdk', 'path')

                    if sdk_location
                        Logging.log_debug("Setting Android SDK location from '#{monodroid_config_file}'")

                        return sdk_location
                    end
                end

                if File.exist?(File.expand_path('~/Library/Developer/Xamarin/android-sdk-mac_x86/'))
                    return File.expand_path('~/Library/Developer/Xamarin/android-sdk-mac_x86/')
                end
                
                if File.exist?(File.expand_path('~/Library/Developer/Xamarin/android-sdk-macosx/'))
                    return File.expand_path('~/Library/Developer/Xamarin/android-sdk-macosx/')
                end
                
                # Default location when installing with Android Studio
                if File.exist?(File.expand_path('~/Library/Android/sdk/'))
                    return File.expand_path('~/Library/Android/sdk/')
                end
                
                if File.exist?('C:\\Android\\android-sdk')
                    return 'C:\\Android\\android-sdk'
                end

                if is_windows?
                    from_registry = read_registry(::Win32::Registry::HKEY_CURRENT_USER, "Software\\Novell\\Mono for Android", 'AndroidSdkDirectory')

                    if from_registry && File.exist?(from_registry)
                        Logging.log_debug("Setting Android SDK location from HKEY_CURRENT_USER Software\\Novell\\Mono for Android")
                        return from_registry
                    end

                    from_registry = read_registry(::Win32::Registry::HKEY_LOCAL_MACHINE, 'Software\\Android SDK Tools', 'Path')

                    if from_registry && File.exist?(from_registry)
                        Logging.log_debug("Setting Android SDK location from HKEY_LOCAL_MACHINE Software\\Android SDK Tools")
                        return from_registry
                    end
                end

                nil
            end

            def self.detect_java_sdk_location
                if File.exist?(monodroid_config_file)
                    sdk_location = read_attribute_from_monodroid_config('java-sdk', 'path')

                    if sdk_location
                        Logging.log_debug("Setting Java SDK location from '#{monodroid_config_file}'")

                        return sdk_location
                    end
                end

                java_versions = ['1.9', '1.8', '1.7', '1.6']

                if is_windows?
                    java_versions.each do |java_version|
                        key = "SOFTWARE\\JavaSoft\\Java Development Kit\\#{java_version}"
                        from_registry = read_registry(::Win32::Registry::HKEY_LOCAL_MACHINE, key, 'JavaHome')

                        if from_registry && File.exist?(from_registry)
                            Logging.log_debug("Setting Java SDK location from HKEY_LOCAL_MACHINE #{key}")
                            return from_registry
                        end
                    end
                end

                nil
            end

            def self.monodroid_config_file
                File.expand_path('~/.config/xbuild/monodroid-config.xml')
            end

            def self.read_attribute_from_monodroid_config(element, attribute)
                element = REXML::Document.new(IO.read(monodroid_config_file)).elements["//#{element}"]

                if element
                    element.attributes[attribute]
                else
                    nil
                end
            end

            def self.find_executable_on_path(executable)
                path_elements.each do |x|
                    f = File.join(x, executable)
                    return f if File.exists?(f)
                end

                nil
            end

            def self.path_elements
                return [] unless ENV['PATH']
                ENV['PATH'].split (/[:;]/)
            end

            def self.zipalign_executable
                is_windows? ? 'zipalign.exe' : 'zipalign'
            end

            def self.jarsigner_executable
                is_windows? ? 'jarsigner.exe' : 'jarsigner'
            end

            def self.java_executable
                is_windows? ? 'java.exe' : 'java'
            end

            def self.keytool_executable
                is_windows? ? 'keytool.exe' : 'keytool'
            end

            def self.adb_executable
                is_windows? ? 'adb.exe' : 'adb'
            end

            def self.aapt_executable
                is_windows? ? 'aapt.exe' : 'aapt'
            end

            def self.ant_executable
                is_windows? ? 'ant.exe' : 'ant'
            end

            def self.is_windows?
                (RbConfig::CONFIG['host_os'] =~ /mswin|mingw|cygwin/)
            end

            def self.read_registry(root_key, key, value)
                begin
                    root_key.open(key)[value]
                rescue
                    nil
                end
            end

            def self.list_files(path)
                # Dir.glob does not accept backslashes, even on windows. We have to
                # substitute all backwards slashes to forward.
                # C:\foo becomes C:/foo

                if is_windows?
                    Dir.glob(File.join(path, '*').gsub('\\', '/'))
                else
                    Dir.glob(File.join(path, '*'))
                end
            end
        end
    end
end
