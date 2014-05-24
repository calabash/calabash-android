package sh.calaba.instrumentationbackend.actions.l10n;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class Assertl10nText implements Action{


    public Result execute(String... args) {
        String l10nKey = args[0];
        boolean shouldBeFound = Boolean.parseBoolean(args[1]);
        String pckg = (args.length > 2)? args[2] : null;

        String myLocalizedString = L10nHelper.getValue(l10nKey, pckg);


        boolean found = InstrumentationBackend.solo.searchText(myLocalizedString, true);

        if(shouldBeFound && !found) {
            return new Result(false, "Text'" + myLocalizedString + "' was not found");
        } else if(!shouldBeFound && found) {
            return new Result(false, "Text'" + myLocalizedString + "' was found");
        } else {
            return Result.successResult();
        }
    }

    public String key() {
        return "assert_l10n_text";
    }

}
