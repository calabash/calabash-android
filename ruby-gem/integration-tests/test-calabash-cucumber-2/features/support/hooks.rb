def save
  scroll_to_push_button 'Save'
end

def scroll_to_push_button(name)
  q = "button marked:'#{name}'"

  while query(q).empty? do
    scroll_down
    sleep 1
  end

  touch q
end