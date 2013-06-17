package sh.calaba.instrumentationbackend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

public class TestHelpers {

    public static TextView getTextViewByDescription(String description) {
        View view = getViewByDescription(description);
        if (view != null && view instanceof TextView) {
            return (TextView) view;
        } else {
            return null;
        }
    }

    public static <T extends View> T getViewByDescription(String description, Class<T> c) {
        return (T) getViewByDescription(description);
    }

    public static View getViewByDescription(String description) {
        for (View view : InstrumentationBackend.solo.getCurrentViews()) {
            String viewDescription = view.getContentDescription() + "";
            if (viewDescription != null && viewDescription.equalsIgnoreCase(description)) {
                return view;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <ViewType extends View> ViewType getViewById(String resName, Class<? extends View> expectedViewType) {
        final View theView = getViewById(resName);

        if (null == theView) {
            return null;
        }

        if (!expectedViewType.isInstance(theView)) {
            throw new RuntimeException(String.format("getViewById:  Expected to find a View of type %s but found one of type %s", expectedViewType.getClass().getName(), theView.getClass().getName()));
        }

        return (ViewType) theView;
    }

    public static View getViewById(String resName) {
        int id = getIdFromString(resName);
        if (id == 0) {
            return null;
        }

        return InstrumentationBackend.solo.getView(id);
    }

    /**
     * Converts a string identifier name to the corresponding {@code R.id.*}
     * (Integer) resource identifier.
     *
     * If the string appears to be a number, it will be converted to an
     * integer and assumed to be an existing identifier.
     *
     * @param resName the resource identifier name
     * @return the corresponding {@code R.id.[resName]} identifier
     */
    public static int getIdFromString(String resName) {
        try
        {
            // Check if th string is just a direct number (unlikely)
            return Integer.parseInt(resName);
        } catch (NumberFormatException nfe) {
            // Assume this is an "R.id.<name>" string.
        }

        final Activity activity = InstrumentationBackend.solo.getCurrentActivity();
        return activity.getResources().getIdentifier(resName, "id", activity.getPackageName());
    }

    public static Drawable getDrawableById(String resName) {
        int id;
        try {
    	    id = InstrumentationBackend.solo.getCurrentActivity().getResources().getIdentifier(resName, "drawable", InstrumentationBackend.solo.getCurrentActivity().getPackageName());
    	} catch( NotFoundException e ) {
    		throw new RuntimeException("getDrawableById: Looking for drawable " + resName + " but was not found");
    	}
        Drawable drawable = InstrumentationBackend.solo.getCurrentActivity().getResources().getDrawable(id);
        if (drawable != null) {
            System.out.println("Did find drawable " + resName + ": " + drawable);
        } else {
        	System.out.println("Did not find drawable " + resName);
        }
        return drawable;
    }

    public static int[] parseTime(String timeString) {
        String[] splitTimeString = timeString.split(":");
        int hour = Integer.parseInt(splitTimeString[0]);
        int minute = Integer.parseInt(splitTimeString[1]);

        if (hour < 0 || hour > 23) {
            throw new RuntimeException("Failed to parse time: Hours was '" + hour + "' and must be between 00 and 23. Time format is HH-mm (ex: '23:49')");
        } else if (minute < 0 || minute > 59) {
            throw new RuntimeException("Failed to parse time: Minutes was '" + minute + "' and must be between 00 and 59. Time format is HH-mm (ex: '23:49')");
        } else {
            int[] time = { hour, minute };
            return time;
        }
    }

    public static int[] parseDate(String dateString) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            formatter.setLenient(false);
            Date date = (Date) formatter.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int[] dateInts = new int[3];
            dateInts[0] = cal.get(Calendar.DAY_OF_MONTH);
            dateInts[1] = cal.get(Calendar.MONTH);
            dateInts[2] = cal.get(Calendar.YEAR);

            return dateInts;
        } catch (Exception e) {
            throw new RuntimeException("Could not parse date:'" + dateString + "'. Date format is dd-MM-yyyy (ex: 31-12-2011).", e);
        }
    }
    public static void wait(int durationInSeconds) {
    	wait(new Double(durationInSeconds));
    }


    public static void wait(double durationInSeconds) {
        try {
			Thread.sleep((int)(durationInSeconds * 1000));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
    }

}
