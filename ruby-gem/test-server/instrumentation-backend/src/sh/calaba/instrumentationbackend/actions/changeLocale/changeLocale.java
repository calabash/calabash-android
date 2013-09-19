package sh.calaba.instrumentationbackend.actions.changeLocale;

import java.util.Locale;

import android.content.res.Configuration;
import android.util.Log;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class changeLocale implements Action{

	@Override
	public Result execute(String... args) {
		String language = args[0];
		Locale locale = new Locale(language); 
        Locale.setDefault(locale);
        Log.d("Language", locale.getLanguage().toString());
        Configuration config = new Configuration();
        config.locale = locale;
       InstrumentationBackend.solo.getCurrentActivity().getBaseContext().getResources().updateConfiguration(config, InstrumentationBackend.solo.getCurrentActivity().getBaseContext().getResources().getDisplayMetrics());
		return Result.successResult();
	}

	@Override
	public String key() {
		return "changeLocale";
	}
}