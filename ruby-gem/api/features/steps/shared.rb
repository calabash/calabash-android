def pan_up_to_see(query, tries)
  tries.times do |i|
    if i == tries-1
      raise "Could not find '#{query}' by panning up"
    end

    if element_exists(query)
      break
    end

    pan_up
  end
end

Given(/^a dialogue is rendered on screen$/) do
  pan_up_to_see("* id:'imageButton'", 5)
  @top_most_button_id = query("button").first[:id]
  touch("* id:'imageButton'")
  wait_for_element_exists("* id:'button2'")
end
