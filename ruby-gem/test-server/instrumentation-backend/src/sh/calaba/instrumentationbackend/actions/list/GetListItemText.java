package sh.calaba.instrumentationbackend.actions.list;

import java.util.ArrayList;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.util.Log;
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
 * eg: (all items of 1st list) <code>performAction( 'get_list_item_text' )</code>
 * eg: (all items of 2nd list) <code>performAction( 'get_list_item_text', '2' )</code>
 * eg: (1st item of 2nd list) <code>performAction( 'get_list_item_text', '2' , '1' )</code>
 * 
 * @return <code>bonusInformation</code> contain an array of Strings, one for each row in the list (or only for the specified row):
 *   eg: {"title":"My Title", "subtitle":"Another text field for the same list item"}
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
public class GetListItemText implements Action {

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
		Log.d("GetListItemText", "Found " + listViews.size() + " list views");
		if( listViews == null || listViews.size() <= listIndex ) {
			return new Result(false, "Could not find list #" + (listIndex + 1));
		}

		ListView list = listViews.get(listIndex);
		Result result = new Result(true);

		if( rowIndex < 0 ) {
			int count = list.getChildCount();
			Log.d("GetListItemText", "Found " + count + " list items");
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
	 *   eg: {"title":"My Title", "subtitle":"Another text field for the same list item"}
	 */
	private String getListItemString( View row ) {
		StringBuilder json = new StringBuilder("{");

		if( row instanceof TextView ) {
			addViewInfo( json, (TextView)row );
		} else if( row instanceof ViewGroup ) {
			addViewInfo( json, (ViewGroup)row );
		}

		json.deleteCharAt( json.length() - 1 );		// remove the last comma
		json.append('}');
		return json.toString();
	}

	private void addViewInfo( StringBuilder json, TextView view ) {
		int resId = view.getId();
		String resIdName = view.getResources().getResourceEntryName(resId);

		//json.append("\"id\":\"").append(resIdName).append("\", \"text:\"").append( ((TextView)row).getText() ).append("\"");
		json.append('"').append(resIdName).append("\":\"").append( ((TextView)view).getText() ).append("\",");
	}

	private void addViewInfo( StringBuilder json, TableRow tableRow ) {
		int count = tableRow.getVirtualChildCount();
		for( int i = 0; i < count; i++ ) {
			View view = tableRow.getVirtualChildAt(i);
			if( view instanceof TextView ) {
				addViewInfo( json, (TextView)view );
			} else if( view instanceof ViewGroup ) {
				addViewInfo( json, (ViewGroup)view );
			}
		}
	}

	private void addViewInfo( StringBuilder json, ViewGroup viewGroup ) {
		int count = viewGroup.getChildCount();
		for( int i = 0; i < count; i++ ) {
			View view = viewGroup.getChildAt(i);
			if( view instanceof TextView ) {
				addViewInfo( json, (TextView)view );
			} else if( view instanceof TableRow ) {
				addViewInfo( json, (TableRow)view );
			} else if( view instanceof ViewGroup ) {
				addViewInfo( json, (ViewGroup)view );
			}
		}
	}

	@Override
	public String key() {
		return "get_list_item_text";
	}
}