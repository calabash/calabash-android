require 'calabash-android/management/app_installation'

AfterConfiguration do |config|
	FeatureNameMemory.feature_name = nil
end

Before do |scenario|
  feature_name = scenario.feature.name
  if FeatureNameMemory.feature_name != feature_name
    log "First scenario in feature - reinstalling apps"
    
    uninstall_apps
    install_app(ENV["TEST_APP_PATH"])
    install_app(ENV["APP_PATH"])
    FeatureNameMemory.feature_name = feature_name
	end
end

at_exit do
  require 'net/http'
  Net::HTTP.get(URI.parse("http://127.0.0.1:34777/kill"))
end

FeatureNameMemory = Class.new
class << FeatureNameMemory
  @feature_name = nil
  attr_accessor :feature_name
end
