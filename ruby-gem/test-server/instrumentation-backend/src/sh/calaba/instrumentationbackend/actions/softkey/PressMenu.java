package sh.calaba.instrumentationbackend.actions.softkey;


import com.robotium.solo.Solo;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.actions.Actions;


public class PressMenu implements Action {

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
