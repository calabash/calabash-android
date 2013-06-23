package sh.calaba.instrumentationbackend.actions.view;

import java.util.LinkedList;
import java.util.List;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.app.Activity;
import android.app.TabActivity;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Select the tab by 1-based index or by name
 * 
 * @author Nicholas Albion
 */
@SuppressWarnings("deprecation")
public class SelectTab implements Action {
	@Override
	public Result execute(String... args) {
		
		Activity currentActivity = InstrumentationBackend.solo.getCurrentActivity();
		
		if( currentActivity instanceof TabActivity == false ) {
			Activity parent = currentActivity.getParent();
			if( parent != null ) {
				currentActivity = parent;
			}
		}
		
		if( currentActivity instanceof TabActivity ) {
			final TabHost tabHost = ((TabActivity)currentActivity).getTabHost();
			
			String tabName = args[0];
			if( tabName.length() == 1 ) {
				char c = tabName.charAt(0);
				if( c >= '0' && c <= '9' ) {
					int index = (c - '0') - 1;
					tabHost.setCurrentTab( index );
					return Result.successResult();
				}
			}

			List<String> foundTabs = new LinkedList<String>();
			int numberOfTabs = ((TabActivity)currentActivity).getTabWidget().getTabCount();
			Log.i("SelectTab", "numberOfTabs: " + numberOfTabs);
			
			String resultMessage = null;
			int tabIndex;
FIND_TAB:			
			for( tabIndex = 0; tabIndex < numberOfTabs; tabIndex++ ) {
				if( tabName.equals( tabHost.getTag(tabIndex) ) ) {
					resultMessage = "Selected the tab by tag";
					break;
				}
				
				String currentTab;
				View view = tabHost.getTabWidget().getChildTabViewAt(tabIndex);
        		if( view instanceof TextView ) {
        			currentTab = ((TextView)view).getText().toString();	
        			if( tabName.equals(currentTab) ) {
        				resultMessage = "Selected the tab by text view";
        				break;
            		}
        			foundTabs.add(currentTab);
        		} else {
        			Log.i("SelectTab", "current tab view: " + view.getId() + ", " + view.getClass().getSimpleName());
        			List<TextView> textViews = InstrumentationBackend.solo.getCurrentViews(TextView.class, view);
        			for( TextView textView : textViews ) {
        				currentTab = textView.getText().toString();
        				Log.i("SelectTab", " child text view: " + currentTab);
            			if( tabName.equals(currentTab) ) {
            				resultMessage = "Selected the tab by child text view";
            				break FIND_TAB;
                		}
            			foundTabs.add(currentTab);
					}
        		}
			}
			
			if( resultMessage != null ) {
				final int matchingTabIndex = tabIndex;
				
				currentActivity.runOnUiThread( new Runnable() {
					@Override
					public void run() {
						tabHost.setCurrentTab( matchingTabIndex );
					}
				});
				
				return new Result(true, resultMessage);
			}
			
			Result result = new Result(false, "Was unable to find a matching tab");
			result.setExtras( foundTabs );
			return result;
		} else {
			return new Result(false, "The current activity is not a TabActivity: " + currentActivity.getLocalClassName() );
		}
	}

	@Override
	public String key() {
		return "select_tab";
	}
}
