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

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

public class TestHelpers {
	private static Map<String, Integer> resourceNamesToIds = new HashMap<String, Integer>();
	private static Map<Integer, String> resourceIdsToNames = new HashMap<Integer, String>();
	
	 public static void loadIds(Context context) {
		 
		 resourceNamesToIds = new HashMap<String, Integer>();
    	try {
			InputStream is = context.getResources().getAssets().open("ids.txt");
			BufferedReader input =  new BufferedReader(new InputStreamReader(is));
			String line = null;
			while (( line = input.readLine()) != null){
				line = line.trim();
				if (line.contains(InstrumentationBackend.TARGET_PACKAGE + ":id/")) {
					if (line.startsWith("resource")) {
						String[] tokens = line.split(" ");
						String name = tokens[2];
						name = name.replace(InstrumentationBackend.TARGET_PACKAGE + ":id/", "");
						name = name.replace(":", "");
							
						resourceNamesToIds.put(name, Integer.parseInt(tokens[1].substring(2), 16));
						resourceIdsToNames.put(Integer.parseInt(tokens[1].substring(2), 16), name);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
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

    public static View getViewById(String resName) {
        Integer intID = resourceNamesToIds.get(resName);
        System.out.println("getViewById: Looking for view " + resName + " as id " + intID);
        if (intID == null) {
            throw new RuntimeException("getViewById: Looking for view " + resName + " which does not have an id");
        }
        int id = intID.intValue();
        View view = InstrumentationBackend.solo.getView(id);
        if (view != null) {
            if (id == view.getId()) {
                System.out.println("Did find view " + resName + ".");
                return view;
            }
            System.out.println("Did find view " + resName + " but getId returned: " + view.getId());
        }
        System.out.println("Did not find view " + resName);
        return view;
    }
    
    public static Drawable getDrawableById(String resName) {
    	int id;
// This would probably work too...
if( true ) {
    	try {
//    		if( resName.startsWith("drawable/") == false ) {
//    			resName = "drawable/" + resName;
//    		}
//    		TypedValue outValue = new TypedValue();
//    		InstrumentationBackend.solo.getCurrentActivity().getResources().getValue( resName, outValue, false );
//    		id = outValue.resourceId;
    		id = InstrumentationBackend.solo.getCurrentActivity().getResources().getIdentifier( resName, "drawable", InstrumentationBackend.solo.getCurrentActivity().getPackageName() );
    	} catch( NotFoundException e ) {
    		throw new RuntimeException("getDrawableById: Looking for drawable " + resName + " but was not found");
    	}
} else {
    	Integer intID = resourceNamesToIds.get(resName);
        System.out.println("getDrawableById: Looking for drawable " + resName + " as id " + intID);
        if (intID == null) {
            throw new RuntimeException("getDrawableById: Looking for drawable " + resName + " which does not have an id");
        }
        id = intID.intValue();
}
        Drawable drawable = InstrumentationBackend.solo.getCurrentActivity().getResources().getDrawable(id);
        if (drawable != null) {
            System.out.println("Did find drawable " + resName + ": " + drawable);
            return drawable;
        } else {
        	System.out.println("Did not find drawable " + resName);
        	return drawable;
        }
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
