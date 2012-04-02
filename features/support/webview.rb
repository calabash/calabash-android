def query(q)
  raise "Currently only queries are only supported for webviews" unless q.start_with? "webView"
  
  q.slice!(0, "webView".length)
  if q =~ /(css|xpath):\s*(.*)/
    r = performAction("query", $1, $2)    
    JSON.parse(r["message"])
  else
   raise "Invalid query #{q}" 
  end
end