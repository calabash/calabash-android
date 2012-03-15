package sh.calaba.instrumentationbackend.actions.zooz;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.EditText;


/*
 * Warning: Unlike most other actions this action will allow you to click on views that are not visible.
 */
public class SetExpiration implements Action {

    @Override
    public Result execute(String... args) {
    	InstrumentationBackend.solo.clickOnView(InstrumentationBackend.solo.getView(EditText.class, 1));
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<View> views = InstrumentationBackend.solo.getViews();
		System.out.println("list_all_views: " + views.size());
    	for (final View v : views) {
    		System.out.println(v.getClass().getName());
    		
    		if (v.getClass().getName().equals("com.zooz.android.lib.widgets.DateSlider.YearlyScroller")) {
	    		InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						for (Method m : v.getClass().getMethods()) {
		    				System.out.println("Method: " + Modifier.toString(m.getModifiers()) + " " + m.getName() + " : " + m.toGenericString());
		    				if (m.getName().equals("setTime")) {
		    					System.out.println("CALLING setTime ");
			    				Object arglist[] = new Object[1];
			    	            arglist[0] = System.currentTimeMillis();
			    	            try {
									m.invoke(v, arglist);
								} catch (Exception e) {
									e.printStackTrace();
								}
		    				}
	
		    			}
					}
				});
    		}
    	}
    	return Result.successResult();
    }

    @Override
    public String key() {
        return "set_expiration";
    }

   
    
}
