package sh.calaba.instrumentationbackend.actions.button;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class PressImageButtonNumber implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clickOnImageButton(Integer.parseInt(args[0]) - 1);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "press_image_button_number";
    }

}