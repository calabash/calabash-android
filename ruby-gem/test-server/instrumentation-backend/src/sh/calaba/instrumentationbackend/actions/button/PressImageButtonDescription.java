package sh.calaba.instrumentationbackend.actions.button;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.widget.ImageButton;


public class PressImageButtonDescription implements Action {

    @Override
    public Result execute(String... args) {
        String err = "No image button found with description "+args[0]+" found. Found only:";
        int index = 0;
        if (args.length == 2) {
            index = Integer.parseInt(args[1]);
        }

        int count = 0;
        for (ImageButton b : InstrumentationBackend.solo.getCurrentViews(ImageButton.class)){
            err += " "+ b.getContentDescription();
            if (args[0].equals(b.getContentDescription())){
                if (count == index) {
                    InstrumentationBackend.solo.clickOnView(b);
                    return Result.successResult();
                }
                count++;
            }
        }
        return new Result(false, err);
    }

    @Override
    public String key() {
        return "press_image_button_description";
    }

}
