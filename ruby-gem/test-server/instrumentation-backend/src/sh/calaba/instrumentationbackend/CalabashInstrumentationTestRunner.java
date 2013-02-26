package sh.calaba.instrumentationbackend;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import sh.calaba.instrumentationbackend.actions.HttpServer;

public class CalabashInstrumentationTestRunner extends InstrumentationTestRunner {
	@Override
    public void onCreate(Bundle arguments) {		
		try {
			Context context = getTargetContext ();
			Class<?> c = Class.forName("mono.MonoPackageManager");
			Method  method = c.getDeclaredMethod ("LoadApplication", Context.class, String.class, String[].class);
			method.invoke (null, context, null, new String[]{context.getApplicationInfo ().sourceDir});
			System.out.println("Calabash loaded Mono");
		} catch (Exception e) {
			System.out.println("Calabash did not load Mono. This is only a problem if you are trying to test a Mono application");
		}

        // Start the HttpServer as soon as possible in a not-ready state
        HttpServer.instantiate(Integer.parseInt(arguments.getString("test_server_port")));

        InstrumentationBackend.testPackage = arguments.getString("target_package");

        try {
            InstrumentationBackend.mainActivity = Class.forName(arguments.getString("main_activity"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        super.onCreate(arguments);

	}	
}
