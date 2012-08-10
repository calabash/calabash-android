require 'calabash-android/management/app_installation'

AfterConfiguration do |config|
	FeatureNameMemory.feature_name = nil
end

Before do |scenario|
  feature_name = scenario.feature.name
  if FeatureNameMemory.feature_name != feature_name \
      or ENV["RESET_BETWEEN_SCENARIOS"] == "1"
    if ENV["RESET_BETWEEN_SCENARIOS"] == "1"
      log "New scenario - reinstalling apps"
    else
      log "First scenario in feature - reinstalling apps"
    end
    
    uninstall_apps
    install_app(ENV["TEST_APP_PATH"])
    install_app(ENV["APP_PATH"])
    FeatureNameMemory.feature_name = feature_name
	end
end

FeatureNameMemory = Class.new
class << FeatureNameMemory
  @feature_name = nil
  attr_accessor :feature_name
end
