package sh.calaba.instrumentationbackend.actions.webview;

import sh.calaba.instrumentationbackend.query.Operation;

public class JavaScriptOperation implements Operation {

	public final String script;
	public final String arg;	

	public JavaScriptOperation(String script, String args) {		
		this.script = script;
		this.arg = args;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object apply(Object o) {	
		throw new UnsupportedOperationException("asd");
		/*
		Map<String, Object> domEl = (Map<String, Object>) o;
		
		WebView webView = (WebView) domEl.get("webView");
		
		domEl.remove("class");
		domEl.remove("html");
		domEl.remove("webView");
		
		String elJson = QueryHelper.toJsonString(domEl);

		
		WebFuture asyncRes = QueryHelper.executeAsyncJavascriptInWebviews(webView, this.script,
						elJson, this.arg);		
		return asyncRes.getAsString();		
		*/
	}

	@Override
	public String getName() {
		return "JSOp[script="+this.script+"]";
	}
	
	

}
