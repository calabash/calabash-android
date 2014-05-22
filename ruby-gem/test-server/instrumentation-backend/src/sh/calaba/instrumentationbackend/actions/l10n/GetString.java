package sh.calaba.instrumentationbackend.actions.l10n;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * Action is getting the translated value of the provided #l10nKey and is
 * returning it.
 *
 * @author Louis Davin
 * @see sh.calaba.instrumentationbackend.actions.l10n.L10nHelper
 */
public class GetString implements Action {

    @Override
    public Result execute(String... args) {
        String l10nKey = args[0];
        String pckg = (args.length > 1) ? args[1] : null;

        String localizedString = L10nHelper.getValue(l10nKey, pckg);
        return new Result(true, localizedString);
    }

    @Override
    public String key() {
        return "get_l10n_string";
    }

}
