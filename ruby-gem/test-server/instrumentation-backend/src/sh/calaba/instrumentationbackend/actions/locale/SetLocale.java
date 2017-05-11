package sh.calaba.instrumentationbackend.actions.locale;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;

/**
 * Allows change of locale
 *
 * To set the locale to Canadian French,
 * call like: perform_action('set_locale', 'fr', 'rCA')
 *
 * To set the locale to Spanish,
 * call like: perform_action('set_locale', 'es')
 *
 * @author Mike Derrick (mike33d@gmail.com)
 */
public class SetLocale implements Action {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Result execute(String... args) {
        Resources resources = InstrumentationBackend.solo.getCurrentActivity().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        // Allows region to be optionally specified
        if(args.length > 1) {
            config.locale = new Locale(args[0], args[1]);
        } else {
            config.locale = new Locale(args[0]);
        }
        InstrumentationBackend.solo.getCurrentActivity().getResources().updateConfiguration(config, displayMetrics);
        reloadActivity();
        TestHelpers.wait(1);
        return Result.successResult();
    }

    private void reloadActivity() {
        Intent intent = new Intent(InstrumentationBackend.solo.getCurrentActivity(), InstrumentationBackend.solo.getCurrentActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        InstrumentationBackend.solo.getCurrentActivity().startActivity(intent);
        TestHelpers.wait(1);
    }

    @Override
    public String key() {
        return "set_locale";
    }
}
