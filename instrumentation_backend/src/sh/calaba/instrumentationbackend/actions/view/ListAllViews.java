package sh.calaba.instrumentationbackend.actions.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;

public class ListAllViews implements Action {

    @Override
    public Result execute(String... args) {
    	ArrayList<View> views = InstrumentationBackend.solo.getViews();
    	System.out.println("list_all_views: " + views.size());
    	for (final View v : views) {
    		System.out.println(v.getClass());
    		
    		if (v.getClass().toString().equals("class com.zooz.android.lib.widgets.dateWheel.DateWheel")) {
    			try {
    			
    			for (Field f : v.getClass().getDeclaredFields()) {
						System.out.println("Field: " + f.getName());
                }
    			InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						for (Method m : v.getClass().getMethods()) {
		    				System.out.println("Method: " + Modifier.toString(m.getModifiers()) + " " + m.getName() + " : " + m.toGenericString());
		    				if (m.getName().equals("changeDate")) {
		    					System.out.println("CALLING changeDate ");
			    				Object arglist[] = new Object[2];
			    	            arglist[0] = new Integer(11);
			    	            arglist[1] = new Integer(2012);        
			    	            try {
									m.invoke(v, arglist);
								} catch (Exception e) {
									e.printStackTrace();
								}
		    				}
		    	            
		    			}
					}
				});
    			
    			
    			} catch (Exception e) {
    				e.printStackTrace();
    				throw new RuntimeException(e);
    			}
    			
    		}
    	}
        return Result.successResult();
    }

    @Override
    public String key() {
        return "list_all_views";
    }
    
}
