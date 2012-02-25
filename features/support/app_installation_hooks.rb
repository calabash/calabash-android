AfterConfiguration do |config|
	@@feature_name = nil
end

Before do |scenario|
	feature_name = scenario.feature.name
	if @@feature_name != feature_name
		$stdout.puts "Is first scenario"
		uninstall_app
		install_app
		@@feature_name = feature_name
	end
end


at_exit do
	uninstall_app
end

