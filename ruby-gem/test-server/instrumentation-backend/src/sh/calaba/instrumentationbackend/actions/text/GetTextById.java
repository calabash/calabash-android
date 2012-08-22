package sh.calaba.instrumentationbackend.actions.text;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.TextView;

public class GetTextById implements Action {

    @Override
    public Result execute(String... args) {
        String idArgument = args[0];
        final View theView = TestHelpers.getViewById(idArgument);

        if (null == theView) {
            return notFoundResult(idArgument);
        } else if (!(theView instanceof TextView)) {
            return foundButNotATextViewResult(idArgument, theView);
        }
        
        return new Result(true, textOf(theView));
    }

    @Override
    public String key() {
        return "get_text_by_id";
    }

    private String textOf(final View theView) {
        return ((TextView)theView).getText().toString();
    }

    private Result foundButNotATextViewResult(String idArgument, final View theView) {
        return Result.failedResult(String.format("Found View with id %s but it is a %s not a %s", idArgument, theView.getClass().getName(), TextView.class.getName()));
    }

    private Result notFoundResult(String idArgument) {
        return Result.failedResult(String.format("View with id %s was not found.", idArgument));
    }

}
