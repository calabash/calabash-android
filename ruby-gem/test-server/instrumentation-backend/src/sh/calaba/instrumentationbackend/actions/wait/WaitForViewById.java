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
        		return new Result(false, "Timed out while waiting for view with id:'" + viewId + "'");
        	}
        } catch( InterruptedException e ) {
    		return Result.fromThrowable(e);
    	}
    }
    
    protected View getViewById( String viewId, long timeout ) throws InterruptedException {
    	System.out.println("Waiting for view with id '" + viewId + "'");
        long endTime = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < endTime) {
            View view = TestHelpers.getViewById(viewId);
            System.out.println("Waiting for view with id '" + viewId + "' found view " + view);

            if (view != null) {
                System.out.println("Waiting for view with id '" + viewId + "' Success");
                return view;
            } else {
            	System.out.println("Waiting for view with id '" + viewId + "' sleeping...");
            	Thread.sleep(500);
            }
        }
        
        System.out.println("Waiting for view with id '" + viewId + "' Timed out");
        return null;
    }

    @Override
    public String key() {
        return "wait_for_view_by_id";
    }
}