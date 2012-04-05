Then /^I manually compare the current screen with the reference image "([^\"]*)"$/ do |name|
    # TODO: once test artifacts directory is configurable, the image location
    # will need to be either a fully qualified path somewhere (webserver?) or
    # be relative to the output HTML file
    raise Exception, "Reference image not found: #{name}" if !File.exists?(name)
    embed(name, 'image/png', name)
end

Then /^I manually (.*)$/ do |action|
    # Do nothing
end
