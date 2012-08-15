package sh.calaba.instrumentationbackend.actions.view;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;


/*
 * Warning: Unlike most other actions this action will allow you to click on views that are not visible.
 */
public class ClickOnViewById implements Action {

    @Override
    public Result execute(String... args) {
        final View view = TestHelpers.getViewById(args[0]);

        if(view == null) {
            return new Result(false, "Could not find view with id: '" + args[0] + "'");
        }
		try {
			System.out.println("Clicking on view: " + view.getClass());
			System.out.println(view.getLeft());
			System.out.println(view.getTop());
			System.out.println(view.getWidth());
			System.out.println(view.getHeight());
			int[] xy = new int[2];

			view.getLocationOnScreen(xy);
			System.out.println(xy[0]);
			System.out.println(xy[1]);
			
			InstrumentationBackend.solo.clickOnView(view);		
		} catch(junit.framework.AssertionFailedError e) {
			System.out.println("solo.clickOnView failed - using fallback");
			if (view.isClickable()) {
				InstrumentationBackend.solo.getCurrentActivity().runOnUiThread(new Runnable() {
					public void run() {
						view.performClick();
					}	
				});
			}
		}
        return Result.successResult();
    }

    @Override
    public String key() {
        return "click_on_view_by_id";
    }

   
    
}
