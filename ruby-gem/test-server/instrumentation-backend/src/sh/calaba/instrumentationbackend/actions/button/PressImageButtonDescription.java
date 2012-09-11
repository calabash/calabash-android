package sh.calaba.instrumentationbackend.actions.button;


import android.widget.ImageButton;
import android.view.ViewGroup;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class PressImageButtonDescription implements Action {

    @Override
    public Result execute(String... args) {
        String err = "No image button found with description "+args[0]+" found. Found only:";
        for (ImageButton b : InstrumentationBackend.solo.getCurrentImageButtons()){
            err += " "+ b.getContentDescription();
            if (args[0].equals(b.getContentDescription())){
                InstrumentationBackend.solo.clickOnView(b);
                return Result.successResult();
            }
        }
        return new Result(false, err);
    }

    @Override
    public String key() {
        return "press_image_button_description";
    }

}
