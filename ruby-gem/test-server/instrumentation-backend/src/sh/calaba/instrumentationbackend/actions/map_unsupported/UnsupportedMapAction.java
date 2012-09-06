package sh.calaba.instrumentationbackend.actions.map_unsupported;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class UnsupportedMapAction implements Action {

	@Override
	public Result execute(String... args) {
		throw new RuntimeException(
			"Google Maps is not supported by default.\n" + 
			"You can add support by following running calabash-android with --google-maps-support\n" + 
			"See more at: https://github.com/calabash/calabash-android/wiki/Google-Maps-Support"
			);
	}

	@Override
	public String key() {
		return null;
	}
}
