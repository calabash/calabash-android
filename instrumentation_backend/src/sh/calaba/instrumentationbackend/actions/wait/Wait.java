package sh.calaba.instrumentationbackend.actions.wait;


import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;


public class Wait implements Action {

    @Override
    public Result execute(String... args) {
        int seconds = Integer.parseInt(args[0]);

        TestHelpers.wait(seconds);

        return Result.successResult();
    }

    @Override
    public String key() {
        return "wait";
    }
}
