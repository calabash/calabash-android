package sh.calaba.instrumentationbackend.actions.view;

import android.view.View;

import java.util.Arrays;
import java.util.Map;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.query.Operation;
import sh.calaba.instrumentationbackend.query.Query;
import sh.calaba.instrumentationbackend.query.QueryResult;
import sh.calaba.instrumentationbackend.query.ast.InvalidUIQueryException;

/**
 * Created by john7doe on 06/01/15.
 */
public class ExecuteOnView {
    public Result execute(IOnViewAction onViewAction, String... args) {
        if (args.length != 1) {
            return Result.failedResult("Query for identifying view must be provided.");
        }

        RememberFirst rememberFirst = new RememberFirst();
        String message = "";
        try {
            Query query = new Query(args[0], Arrays.asList(rememberFirst));
            QueryResult queryResult = query.executeQuery();

            if (queryResult.isEmpty()) {
                return Result.failedResult("Query found no view(s).");
            }

            if(rememberFirst.first instanceof View) {
                message = onViewAction.doOnView((View) rememberFirst.first);
            } else if(rememberFirst.first instanceof Map) {
                message = onViewAction.doOnView((Map) rememberFirst.first);
            }
        } catch (Exception e) {
            return Result.fromThrowable(e);
        }
        return new Result(true, message);
    }


    private class RememberFirst implements Operation {
        Object first = null;

        @Override
        public Object apply(Object o) throws Exception {
            if (first == null) {
                first = o;
            }
            return o;
        }

        @Override
        public String getName() {
            return "First";
        }
    }
}
