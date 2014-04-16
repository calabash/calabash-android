package sh.calaba.instrumentationbackend.actions.scrolling;

import java.util.List;

import android.view.View;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.query.Query;

import com.jayway.android.robotium.solo.Solo;

/**
 * Scroll Action.
 * Scrolls first view matching given query in specified direction with optional scroll position
 *
 * @note Scroll does not support offset (same as iOS) so there's no way to find a work around
 * for "scroll from the edge" issue, when scrolling from left edge opens side menu instead of scrolling to previous page
 * since currently swipe is implemented using scroll, the only way to avoid the issue is to use drag
 *
 * @param args arguments
 * <ul>
 * 	<li>args[0] - (String) query string; mandatory</li>
 * 	<li>args[1] - (String) direction, "left", "right", "up" or "down"; mandatory</li>
 * 	<li>args[2] - (String) scroll position, float value from "0.0" to "1.0"; optional, applicable only to side scrolls</li>
 * </ul>
 * @return Result execution result.
 *
 * @author Maksym Grebenets (mgrebenets@gmail.com)
 */
public class Scroll implements Action {
    @Override
    public Result execute(String... args) {
    	if (args.length < 2) {
    		return Result.failedResult("You must provide a query and direction.");
    	}

    	String queryString = args[0];
    	// get the views for the query and pick the 1st one to scroll, if any
    	List<View> views = new Query(queryString).viewsForQuery();
    	View view = views.isEmpty() ? null : views.get(0);
    	String direction = args[1];
    	Float scrollPosition = new Float(args.length > 2 ? args[2] : "1");
    	if (direction.equalsIgnoreCase("up")) {
			InstrumentationBackend.solo.scrollUp();
    	} else if (direction.equalsIgnoreCase("down")) {
    		InstrumentationBackend.solo.scrollDown();
    	} else if (direction.equalsIgnoreCase("left")) {
    		if (view != null) {
				InstrumentationBackend.solo.scrollViewToSide(view, Solo.LEFT, scrollPosition);
			} else {
				InstrumentationBackend.solo.scrollToSide(Solo.LEFT);
			}
    	} else if (direction.equalsIgnoreCase("right")) {
    		if (view != null) {
				InstrumentationBackend.solo.scrollViewToSide(view, Solo.RIGHT, scrollPosition);
			} else {
				InstrumentationBackend.solo.scrollToSide(Solo.RIGHT);
			}
    	} else {
    		return Result.failedResult("Invalid direction to scroll: " + direction);
    	}

        return Result.successResult();
    }

    @Override
    public String key() {
        return "scroll";
    }

}
