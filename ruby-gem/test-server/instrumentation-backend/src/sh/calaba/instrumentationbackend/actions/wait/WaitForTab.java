package sh.calaba.instrumentationbackend.actions.wait;

import java.util.List;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Waits for a tab by name
 * 
 * @author Nicholas Albion
 */
public class WaitForTab implements Action {

    @Override
    public Result execute(String... args) {

        final int DEFAULT_TIMEOUT = 5 * 1000;
        int timeout = DEFAULT_TIMEOUT;

        switch (args.length) {
        case 0:
            return new Result(false, "Cannot check for correct screen. No tab name supplied!");
        case 1: {
        	return waitForTab( args[0], DEFAULT_TIMEOUT );
        }
        case 2: { // 1st arg is Activity name, 2nd arg is timeout
            try {
                timeout = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                return new Result(false, "Invalid timeout supplied. Should be an integer!"); 
            }            
            return waitForTab( args[0], timeout );
        }
        default:
            return new Result(false, "Too many argument supplied to wait_for_tab!");
        }
    }
    
    private Result waitForTab( String tabName, int timeout ) {
    	int tabHostIndex = 0;
    	long startTime = SystemClock.uptimeMillis();
    	// TabHost tabHost = InstrumentationBackend.solo.getView( TabHost.class, tabHostIndex );		// waits 10 seconds
    	
    	if( InstrumentationBackend.solo.waitForView( TabHost.class, 1, timeout ) ) {
        	TabHost tabHost = (TabHost)InstrumentationBackend.solo.getView(TabHost.class, tabHostIndex);
        	
        	boolean tabFound = false;
        	int numberOfTabs = tabHost.getChildCount();
        	for( int i = 0; i < numberOfTabs; i++ ) {
        		if( tabName.equals( tabHost.getTag(i) ) ) {
        			tabFound = true;
        			break;
        		}
        	}
        	if( tabFound == false ) {
        		Log.w("WaitForTab", "Tab was not found: " + tabName);
//        		return new Result(false, "Tab " + tabName + " not found");
        	}
        	
        	String currentTab;
        	do {
        		int tabIndex = tabHost.getCurrentTab();
        		View view = tabHost.getTabWidget().getChildTabViewAt(tabIndex);
        		if( view instanceof TextView ) {
        			currentTab = ((TextView)view).getText().toString();	
        			if( tabName.equals(currentTab) ) {
        				return new Result(true, "The current tab text view matches");
            		}
        		} else {
        			Log.i("WaitForTab", "current tab view: " + view.getId() + ", " + view.getClass().getSimpleName());
        			List<TextView> textViews = InstrumentationBackend.solo.getCurrentViews(TextView.class, view);
        			for( TextView textView : textViews ) {
        				currentTab = textView.getText().toString();
        				Log.i("WaitForTab", " child text view: " + currentTab);
            			if( tabName.equals(currentTab) ) {
            				return new Result(true, "The current tab text view matches");
                		}
					}
        		}
        		
        		currentTab = tabHost.getCurrentTabTag();
        		
        		if( tabName.equals(currentTab) ) {
        			return new Result(true, "The current tab's tag matches");
        		}
        		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
        	} while( (SystemClock.uptimeMillis() - startTime) < timeout );
        
            return new Result(false, "The current tab is: " + currentTab + ", was tab even found? " + tabFound);
        } else {
            return new Result(false, "Timed out waiting for a TabActivity. Current activity is " + InstrumentationBackend.solo.getCurrentActivity().getLocalClassName());
        }
    }

    @Override
    public String key() {
        return "wait_for_tab";
    }
}