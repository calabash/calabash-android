package sh.calaba.instrumentationbackend.actions.wait;


import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;


public class WaitForViewById implements Action {

    @Override
    public Result execute(String... args) {
        String viewId = args[0];
        
        try {
        	if( getViewById(viewId, 60000) != null ) {
        		return Result.successResult();
        	} else {
        		return new Result(false, "Waiting for view with id '" + viewId + "' to be visible timed out");
        	}
        } catch( InterruptedException e ) {
    		return Result.fromThrowable(e);
    	}
    }
    
    protected View getViewById( String viewId, long timeout ) throws InterruptedException {
        
    	System.out.println("Waiting for view with id '" + viewId + "' to appear");
    	
    	View view = TestHelpers.getViewById(viewId);
    	
    	// no view, quick exit
    	if(view == null){
    	    throw new IllegalArgumentException("Could not find view with id '" + viewId + "'");
    	}
    	
    	System.out.println("Waiting for view with id '" + viewId + "' found view " + view);
    	
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            if (view.getVisibility() == View.VISIBLE) {
                System.out.println("View with id '" + viewId + "' is visible, success");
                return view;
            } else {
            	System.out.println("View with id '" + viewId + "' is not visible, sleeping...");
            	Thread.sleep(500);
            }
        }
        
        return null;
    }

    @Override
    public String key() {
        return "wait_for_view_by_id";
    }
}