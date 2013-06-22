package sh.calaba.instrumentationbackend.actions.list;

import java.lang.reflect.Method;
import java.util.ArrayList;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * args: 
 * <ul> 
 *   <li>1-based list index (first list is used if not specified)</li>
 *   <li>1-based row index (returns all rows if &lt;=0 or not specified)</li>
 * </ul>
 * 
 * eg: (all items of 1st list) <code>performAction( 'get_list_item_properties' )</code>
 * eg: (all items of 2nd list) <code>performAction( 'get_list_item_properties', '2' )</code>
 * eg: (1st item of 2nd list) <code>performAction( 'get_list_item_properties', '2' , '1' )</code>
 * 
 * @return <code>bonusInformation</code> contain an array of Strings, one for each row in the list (or only for the specified row):
 *   eg: {[{"id":"title", "text":"My Title", "compoundDrawables":["left"]}, {"id":"subtitle", "text":"Another text field for the same list item"}]}
 *   
 *   
 * In ruby, we can then parse the response:
 * <pre>
 *   response_table = result['bonusInformation']
 *   response_table.each_with_index do | row_data, index |
 *     row_data = JSON.parse( row_data )
 *     response_table[index] = row_data
 *   end
 * </pre>
 * 
 * @author Nicholas Albion
 */
public class GetListItemProperties implements Action {

	@Override
	public Result execute(String... args) {
		int listIndex;
		int rowIndex = -1;

		if( args.length == 0 ) {
			listIndex = 0;
		} else {
			if( args.length > 1 ) {
				rowIndex = (Integer.parseInt(args[1]) - 1);
			}
			listIndex = (Integer.parseInt(args[0]) - 1);
		}


		ArrayList<ListView> listViews = InstrumentationBackend.solo.getCurrentViews(ListView.class);
		if( listViews == null || listViews.size() <= listIndex ) {
			return new Result(false, "Could not find list #" + (listIndex + 1));
		}

		ListView list = listViews.get(listIndex);
		Result result = new Result(true);

		if( rowIndex < 0 ) {
			int count = list.getChildCount();
			for( int i = 0; i < count; i++ ) {
				result.addBonusInformation( getListItemString( list.getChildAt(i) ) );
			}
		} else {
			result.addBonusInformation( getListItemString( list.getChildAt(rowIndex) ) );
		}

		return result;
	}

	/**
	 * @return <code>bonusInformation</code> contain an array of Strings, one for each row in the list (or only for the specified row):
	 *   eg: {[{"id":"title", "text":"My Title", "compoundDrawables":{"left":"drawable_id"}}, {"id":"subtitle", "text":"Another text field for the same list item"}]}
	 */
	private String getListItemString( View row ) {
		StringBuilder json = new StringBuilder("{");

		if( row instanceof TextView ) {
			addViewInfo( json, (TextView)row );
		} else if( row instanceof ViewGroup ) {
			addViewInfo( json, (ViewGroup)row );
		}

		json.append('}');
		return json.toString();
	}

	private void addViewInfo( StringBuilder json, TextView view ) {
		int resId = view.getId();
		String resIdName = view.getResources().getResourceEntryName(resId);

		json.append("\"id\":\"").append(resIdName).append("\", \"text\":\"").append( ((TextView)view).getText() ).append("\"");
		
		json.append(", \"color\":").append( view.getCurrentTextColor() );
		Drawable background = view.getBackground();
		if( background instanceof ColorDrawable ) {
			try {
				// As pointed out by kbielenberg, ColorDrawable.getColor() was only added in level 11/Android 3/Honeycomb
				Method getColor = ColorDrawable.class.getMethod("getColor");
				Integer color = (Integer)getColor.invoke(background);
				json.append(", \"background\":").append( color );
			} catch (Exception e) {}
		}

		StringBuilder compoundStr = new StringBuilder();
		Drawable[] compoundDrawables = view.getCompoundDrawables();	//  left, top, right, and bottom
		if( compoundDrawables[0] != null ) {
			compoundStr.append("\"left\"");
		}
		if( compoundDrawables[1] != null ) {
			if( compoundStr.length() != 0 ) {
				compoundStr.append(',');
			}
			compoundStr.append("\"top\"");
		}
		if( compoundDrawables[2] != null ) {
			if( compoundStr.length() != 0 ) {
				compoundStr.append(',');
			}
			compoundStr.append("\"right\"");
		}
		if( compoundDrawables[3] != null ) {
			if( compoundStr.length() != 0 ) {
				compoundStr.append(',');
			}
			compoundStr.append("\"bottom\"");
		}
		if( compoundStr.length() != 0 ) {
			json.append(", \"compoundDrawables\":[").append(compoundStr).append(']');
		}
	}

	private void addViewInfo( StringBuilder json, TableRow tableRow ) {
		json.append("\"cells\":[");

		int count = tableRow.getVirtualChildCount();
		for( int i = 0; i < count; i++ ) {
			if( i != 0 ) {
				json.append(", ");
			}
			json.append("{\"column\":").append(i).append(", ");

			View view = tableRow.getVirtualChildAt(i);
			if( view instanceof TextView ) {
				addViewInfo( json, (TextView)view );
			} else if( view instanceof ViewGroup ) {
				addViewInfo( json, (ViewGroup)view );
			}

			json.append('}');
		}

		json.append(']');
	}

	private void addViewInfo( StringBuilder json, ViewGroup viewGroup ) {
		json.append("\"children\":[");

		int count = viewGroup.getChildCount();
		for( int i = 0; i < count; i++ ) {
			if( i != 0 ) {
				json.append(", ");
			}
			json.append('{');

			View view = viewGroup.getChildAt(i);
			if( view instanceof TextView ) {
				addViewInfo( json, (TextView)view );
			} else if( view instanceof TableRow ) {
				addViewInfo( json, (TableRow)view );
			} else if( view instanceof ViewGroup ) {
				addViewInfo( json, (ViewGroup)view );
			}

			json.append('}');
		}

		json.append(']');
	}

	@Override
	public String key() {
		return "get_list_item_properties";
	}
}