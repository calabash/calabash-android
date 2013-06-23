package sh.calaba.instrumentationbackend.actions.button;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.widget.Button;


public class PressButtonText implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.searchButton(args[0]);
        for (Button b : InstrumentationBackend.solo.getCurrentViews(Button.class))
        	System.out.println("Button text: " + b.getText());
        InstrumentationBackend.solo.clickOnButton(args[0]);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "press_button_with_text";
    }

}

