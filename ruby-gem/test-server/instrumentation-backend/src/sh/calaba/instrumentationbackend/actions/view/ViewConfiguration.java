package sh.calaba.instrumentationbackend.actions.view;

import android.view.View;
import android.webkit.WebView;

import java.io.IOException;
import java.util.Map;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;

public class ViewConfiguration implements Action, IOnViewAction {
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public Result execute(String... args) {
        ExecuteOnView executeOnView = new ExecuteOnView();
        return executeOnView.execute(this, args);
    }

    @Override
    public String doOnView(View view) {
        android.view.ViewConfiguration result = android.view.ViewConfiguration.get(view.getContext());
        try {
            return mapper.writeValueAsString(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String doOnView(Map viewMap) {
        final WebView webView = (WebView) viewMap.get("webView");
        return doOnView(webView);
    }

    @Override
    public String key() {
        return "view_configuration";
    }
}
