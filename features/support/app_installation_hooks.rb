AfterConfiguration do |config|
	FeatureNameMemory.feature_name = nil

    TestArtifactsMemory.feature_artifacts_dir = nil
    raise ArgumentError, 'Environment variable TEST_ARTIFACTS_DIR must be set before executing Cucumber!' if !defined? ENV["TEST_ARTIFACTS_DIR"] || ENV["TEST_ARTIFACTS_DIR"] == ""
    if Dir.exists? ENV["TEST_ARTIFACTS_DIR"] 
      log "Deleting old test artifacts directory: #{ENV["TEST_ARTIFACTS_DIR"]}"
      FileUtils.rm_rf(ENV["TEST_ARTIFACTS_DIR"])
    end
end

Before do |scenario|
  feature_name = scenario.feature.name
  if FeatureNameMemory.feature_name != feature_name
    log "Is first scenario - reinstalling apps"
    uninstall_apps
    install_app(ENV["TEST_APP_PATH"])
    install_app(ENV["APP_PATH"])
    FeatureNameMemory.feature_name = feature_name

    log "Feature file: #{scenario.feature.file}"
    TestArtifactsMemory.feature_artifacts_dir = File.join(ENV["TEST_ARTIFACTS_DIR"], 
                                                scenario.feature.file)
    log "Creating feature test artifacts directory: #{TestArtifactsMemory.feature_artifacts_dir}"
    FileUtils.mkdir_p ENV["TEST_ARTIFACTS_DIR"] unless Dir.exists? ENV["TEST_ARTIFACTS_DIR"]
    FileUtils.mkdir_p TestArtifactsMemory.feature_artifacts_dir
  end
end

at_exit do
#	uninstall_apps
end

FeatureNameMemory = Class.new
class << FeatureNameMemory
  @feature_name = nil
  attr_accessor :feature_name
end

TestArtifactsMemory = Class.new
class << TestArtifactsMemory
  @feature_artifacts_dir = nil
  attr_accessor :feature_artifacts_dir
end

