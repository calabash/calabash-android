package sh.calaba.instrumentationbackend.actions.view;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Press implements Action {

    @Override
    public Result execute(String... args) {
        View viewToPress = null;
        String helpText = "";
        String description = args[0];
        // Try to press button

        InstrumentationBackend.solo.searchButton(description);
        helpText += "Buttons:\n";
        for (Button b : InstrumentationBackend.solo.getCurrentViews(Button.class)) {
            helpText += b.getText() + "\n";
            if (b.getText().toString().equalsIgnoreCase(description)) {
                System.out.println("Found button to press:" + b);
                viewToPress = b;
                break;
            }
        }

        // Try to press view with content description
        if (viewToPress == null) {
            helpText += "Content descriptions:\n";
            for (View view : InstrumentationBackend.solo.getCurrentViews()) {
                String viewDescription = view.getContentDescription() != null ? view.getContentDescription().toString() : "";

                if (viewDescription.length() > 0) {
                    helpText += viewDescription + "\n";
                }
                if (viewDescription != null && viewDescription.equalsIgnoreCase(description)) {
                    System.out.println("Found view to press:" + view);
                    viewToPress = view;
                    break;
                }
            }
        }

        // Try to find view with matching class name
        if (viewToPress == null) {
        	InstrumentationBackend.solo.searchText(description);
        	System.out.println("searchText(" + description + ")");
        	helpText += "Classes:\n";
            for (View v : InstrumentationBackend.solo.getCurrentViews()) {
                if (v.getClass() == TextView.class) {
                    TextView tv = (TextView) v;
                    helpText += v.getClass().getName() + "(" + tv.getText().toString() + ")(" + tv.getParent().getClass() + ")\n";
                    if (tv.getText().toString().equalsIgnoreCase(description)) {
                        viewToPress = v;
                        break;
                    }

                } else {
                    helpText += v.getClass().getName() + "\n";
                    if (v.getClass().getName().endsWith(description)) {
                        viewToPress = v;
                        break;
                    }
                }
            }
        }
        if (viewToPress != null) {
            System.out.println("Pressing:" + viewToPress);
            if (viewToPress instanceof TextView) {
                System.out.println("Pressing(.getText()):" + ((TextView) viewToPress).getText());
            }
            InstrumentationBackend.solo.clickOnView(viewToPress);
            return Result.successResult();
        } else {
            return new Result(false, ("Could not find anything to press matching: '" + description + "' found: \n" + helpText));
        }
    }

    @Override
    public String key() {
        return "press";
    }

}
