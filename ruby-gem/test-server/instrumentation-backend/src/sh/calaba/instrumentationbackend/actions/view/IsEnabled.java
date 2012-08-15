package sh.calaba.instrumentationbackend.actions.view;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;

public class IsEnabled implements Action {

    @Override
    public Result execute(String... args) {
        final String firstArgument = args[0];
        final View foundView = TestHelpers.getViewById(firstArgument);
        
        if( null == foundView ) {
            return notFoundResult(firstArgument);
        }
        
        return new Result(foundView.isEnabled());
    }

    @Override
    public String key() {
        return "is_enabled";
    }

    private Result notFoundResult(final String firstArgument) {
        return Result.failedResult(String.format("View with id %s was not found", firstArgument));
    }
}
