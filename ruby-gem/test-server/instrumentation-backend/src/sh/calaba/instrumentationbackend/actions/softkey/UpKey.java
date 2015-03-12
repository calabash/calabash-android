package sh.calaba.instrumentationbackend.actions.softkey;


import com.robotium.solo.Solo;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class UpKey implements Action {

    @Override
    public Result execute(String... args) {
        InstrumentationBackend.solo.sendKey(Solo.UP);
        return Result.successResult();
    }

    @Override
    public String key() {
        return "send_key_up";
    }

}
