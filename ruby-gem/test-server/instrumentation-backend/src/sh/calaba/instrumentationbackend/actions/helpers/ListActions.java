package sh.calaba.instrumentationbackend.actions.helpers;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class ListActions implements Action {

    @Override
    public Result execute(String... args) {
        Result result = new Result(true, "Available actions");
        for(String key: InstrumentationBackend.actions.getActions().keySet()) {
            result.addBonusInformation(key);
        }
        
        return result;
    }

    @Override
    public String key() {
        return "list_actions";
    }

}
