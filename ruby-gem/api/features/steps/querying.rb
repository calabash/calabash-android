Given(/^a screen with a view with some id$/) do

end

When(/^Calabash queries for a any view with that id$/) do
  pan_up_to_see("* id:'buttonGotoDatePicker'", 5)
  @query_results = query("* id:'buttonGotoDatePicker'")
  @expect = {'id' => 'buttonGotoDatePicker'}
end

Then(/^the list of results (should|should not) contain that element$/) do |boolean|
  should_match = boolean == 'should'

  @query_results.select{|e| e[@expect.keys.first] == @expect.values.first}

  if should_match
    if @query_results.empty?
      raise "Element was not in results, expected it to be"
    end
  else
    unless @query_results.empty?
      raise "Element was in results, expected it not to be"
    end
  end
end

Given(/^a screen with a view that is invisible$/) do
  pan_up_to_see("* id:'buttonScrollplicated'", 5)
  touch("* id:'buttonScrollplicated'")
  wait_for_element_exists("all * id:'twoButton'")
end

When(/^Calabash queries for the invisible view$/) do
  @query_results = query("* id:'twoButton'")
  @expect = {'id' => 'twoButton'}
end

When(/^Calabash queries for the invisible view with the query specifying any visibility$/) do
  @query_results = query("all * id:'twoButton'")
  @expect = {'id' => 'twoButton'}
end

Given(/^a screen with a view of some class$/) do
  pan_up_to_see("* id:'imageButton'", 5)
end

When(/^Calabash queries for any view of that particular class$/) do
  @query_results = query("ImageButton")
  @expect = {'class' => 'android.widget.ImageButton'}
end

Given(/^a screen with a view of some class that inherits from another class$/) do
  pan_up_to_see("* id:'imageButton'", 5)
end

When(/^Calabash queries for any view of the fully qualified parent class$/) do
  @query_results = query("android.widget.ImageView")
  @expect = {'class' => 'android.widget.ImageButton'}
end

Given(/^a screen with a view with some text$/) do
  pan_up_to_see("* text:'Directional Swipe Measurer'", 5)
end

When(/^Calabash queries for a any view with that text$/) do
  @query_results = query("* text:'Directional Swipe Measurer'")
  @expect = {'text' => 'Directional Swipe Measurer'}
end

Given(/^a webview with an element in it with some html id$/) do
  goto_webview_editable_element
end

When(/^Calabash queries for a any element in the webview with that html id$/) do
  @query_results = query("webview css:'#inputText'")
  @expect = {'id' => 'inputText'}
end


Then(/^the list of results should contain both elements from the dialogue as well as elements outside the dialogue$/) do
  inside = @query_results.find {|e| e['id'] == 'button2'}
  outside = @query_results.find {|e| e['id'] == @top_most_button_id}

  unless inside && outside
    raise "Both elements from the dialogue as well as elements outside the dialogue were not returned. Inside: #{inside}, outside (#{@top_most_button_id}): #{outside}"
  end
end

When(/^Calabash queries for elements$/) do
  @query_results = query("*")
end