package sh.calaba.instrumentationbackend.actions.webview;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.org.codehaus.jackson.map.ObjectMapper;
import sh.calaba.org.codehaus.jackson.type.TypeReference;


public class Touch implements Action {

    @Override
    public Result execute(String... args) {

        try {
            String queryResult = QueryHelper.executeJavascriptInWebviews(null,"calabash.js", args[1], args[0]);
            List<HashMap<String,Object>> p = new ObjectMapper().readValue(queryResult, new TypeReference<List<HashMap<String,Object>>>(){});

            if (p.isEmpty()) {
                throw new RuntimeException("No element found");
            }

            Map<String, Object> firstRect = QueryHelper.findFirstVisibleRectangle(p);

            float[] screenCoordinates = QueryHelper.getScreenCoordinatesForCenter(firstRect);

            InstrumentationBackend.solo.clickOnScreen(screenCoordinates[0], screenCoordinates[1]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new Result(true, "");
    }

    @Override
    public String key() {
        return "touch";
    }
}
