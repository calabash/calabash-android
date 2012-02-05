package sh.calaba.instrumentationbackend.actions.gestures;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.Display;


public class ClickOnScreen implements Action {

    @Override
    public Result execute(String... args) {
        Display display = InstrumentationBackend.solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        
        float x = Float.parseFloat(args[0]);
        float y = Float.parseFloat(args[1]);
        
        int width = display.getWidth();
        int height = display.getHeight();
        
        InstrumentationBackend.solo.clickOnScreen((x/100)*width, (y/100)*height);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "click_on_screen";
    }

}
