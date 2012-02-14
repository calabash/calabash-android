package sh.calaba.instrumentationbackend.actions.text;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;


public class AssertText implements Action {

    @Override
    public Result execute(String... args) {
        String text = args[0];
        boolean found = InstrumentationBackend.solo.searchText(text, true);
        boolean shouldBeFound = Boolean.parseBoolean(args[1]);
        
        if(shouldBeFound && !found) {
            return new Result(false, "Text'" + text + "' was not found");
        } else if(!shouldBeFound && found) {
            return new Result(false, "Text'" + text + "' was found");
        } else {
            return Result.successResult();
        }
    }

    @Override
    public String key() {
        return "assert_text";
    }

}
