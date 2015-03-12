package sh.calaba.instrumentationbackend.actions.softkey;


import com.robotium.solo.Solo;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class DownKey implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.sendKey(Solo.DOWN);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "send_key_down";
    }

}
