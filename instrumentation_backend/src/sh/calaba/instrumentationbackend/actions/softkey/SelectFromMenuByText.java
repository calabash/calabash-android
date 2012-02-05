package sh.calaba.instrumentationbackend.actions.softkey;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.Actions;

import com.jayway.android.robotium.solo.Solo;

public class SelectFromMenuByText implements Action {

    @Override
    public Result execute(String... args) {
        Actions.parentInstrumentation.sendKeyDownUpSync(Solo.MENU);
        TestHelpers.wait(1);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "press_menu";
    }

}
