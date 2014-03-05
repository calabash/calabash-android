package sh.calaba.instrumentationbackend.actions.softkey;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.Actions;

import com.robotium.solo.Solo;

public class SelectFromMenuByText implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.clickOnMenuItem(args[0]);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "select_from_menu";
    }

}
