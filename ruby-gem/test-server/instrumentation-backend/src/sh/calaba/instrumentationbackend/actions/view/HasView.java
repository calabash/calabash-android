package sh.calaba.instrumentationbackend.actions.view;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;

public class HasView implements Action {

    @Override
    public Result execute(String... args) {
        final String idArgument = args[0];
        final View foundView = TestHelpers.getViewById(idArgument);
        
        if( null == foundView ) {
            return notFoundResult(idArgument);
        }
        
        return Result.successResult();
    }

    @Override
    public String key() {
        return "has_view";
    }

    private Result notFoundResult(final String firstArgument) {
        return Result.failedResult(String.format("View with id %s was not found", firstArgument));
    }
}

