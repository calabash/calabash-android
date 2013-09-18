package sh.calaba.instrumentationbackend.actions.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class Wifi implements Action {

	@Override
	public Result execute(String... args) {
		String command = args[0];
		WifiManager wifiManager = (WifiManager) InstrumentationBackend.solo.getCurrentActivity().getSystemService(Context.WIFI_SERVICE);
		
		if(command.equals("enable"))
		{
			if(wifiManager.isWifiEnabled())
				Log.d("WIFI", "Already enabled");
			else
			{
				wifiManager.setWifiEnabled(true);
				Log.d("WIFI", "Enabled");
			}
		}
		
		if(command.equals("disable"))
		{
			if(!wifiManager.isWifiEnabled())
				Log.d("WIFI", "Already disabled");
			else
			{
				wifiManager.setWifiEnabled(false);
				Log.d("WIFI", "Disabled");
			}
		}
		
		return Result.successResult();
	}

	@Override
	public String key() {
		return "status_wifi";
	}
}