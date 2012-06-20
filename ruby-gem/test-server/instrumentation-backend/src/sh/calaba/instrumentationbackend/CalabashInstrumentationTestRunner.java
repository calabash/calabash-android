package sh.calaba.instrumentationbackend;

import java.lang.reflect.Method;

import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;

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
			System.out.println("Calabash could not load Mono");
		}
        super.onCreate(arguments);
	}	
}
