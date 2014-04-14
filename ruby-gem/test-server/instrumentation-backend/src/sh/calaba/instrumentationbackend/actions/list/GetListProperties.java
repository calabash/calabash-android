package sh.calaba.instrumentationbackend.actions.list;

import java.util.List;

import android.widget.ListView;
import android.widget.ListAdapter;
import android.view.View;
import android.view.View.MeasureSpec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import sh.calaba.instrumentationbackend.query.Query;

/**
 * Get List View properties.
 * - Contents Size (similar to iOS :contentSize getter)
 * - Content Offset (similar to iOS :contentOffset getter)
 *
 * @note
 * iOS uses scroll/table view delegates to calculate total offset size.
 * For Android List Adapter is like delegates in iOS, so List Adapter
 * needs to be queried to get total content size of the list.
 *
 * @param args arguments
 * <ul>
 * 	<li>args[0] - (String) query, used to find list view, if more than 1 list views match the query, first will be used; default is "*"; optional</li>
 * 	<li>args[1] - (String) className, specific class name of the list view, by default using ListView; optional</li>
 * 	<li>args[2] - (String) adapterClassName, specific class name of list view adapter, by default using ListAdapter; optional </li>
 * </ul>
 * @return Result execution result, stored in "bonusInformation" field.
 *
 * @example Example of parsing in Ruby
 * <pre>
 * 		result = performAction("get_list_properties", "android.widget.ListView index:3")
 * 		bonusInfo = result["bonusInformation"].first
 * 		list_properties = JSON.parse(bonusInfo)
 *		puts "width: " + list_properties["width"]
 *		puts "height: " + list_properties["height"]
 *		puts "offset_y: " + list_properties["offset_y"]
 * </pre>
 */
public class GetListProperties implements Action {
	@Override
	public Result execute(String... args) {

		String queryString = (args.length == 0 ? "*" : args[0]);

		// parse 2nd and 3rd args for list view and adapter class names and use them
		// try {
  //           Class TClass = Class.forName(args.length < 2 ? "ListView" : args[1]);
  //           Class TAdapterClass = Class.forName(args.length < 3 ? "ListAdapter" : args[2]);
  //       } catch (ClassNotFoundException e) {
  //           return Result.failedResult(e.getMessage());
  //       }
		// TODO: use list and adapter classes (improvement)


		List<ListView> listViews = new Query(queryString).viewsForQuery();
		if( listViews == null || listViews.isEmpty()) {
			return Result.failedResult("Could not find any views for the query: " + queryString);
		}

		ListView listView = listViews.get(0);
		ListAdapter mAdapter = listView.getAdapter();

        int totalHeight = 0;
        int listWidth = listView.getWidth();
        int widthSpec = MeasureSpec.makeMeasureSpec(listWidth, MeasureSpec.AT_MOST);
        int offsetX = 0, offsetY = 0;
        String rowHeights = "";
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View listItem = mAdapter.getView(i, null, listView);

            listItem.measure(
                    widthSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            totalHeight += listItem.getMeasuredHeight();
            rowHeights += (i > 0 ? "," : "") + listItem.getMeasuredHeight();

            if (i < listView.getFirstVisiblePosition()) {
            	offsetY += listItem.getMeasuredHeight();
            	offsetY += (i > 0 ? listView.getDividerHeight() : 0);	// count in dividers
            	// ???: padding top?
            }
        }

        // add up dividers height, and padding
        int count = mAdapter.getCount();
		totalHeight += (listView.getDividerHeight() * (count > 0 ? count - 1 : 0));
		totalHeight += listView.getPaddingTop() + listView.getPaddingBottom();

		// build JSON string with results
		JSONObject json = new JSONObject();
		try {
			json.put("width", listWidth);
			json.put("height", totalHeight);
			json.put("row_heights", rowHeights);
			json.put("offset_x", offsetX);
			json.put("offset_y", offsetY);
		} catch (JSONException e) {
            return Result.failedResult(e.getMessage());
        }

		Result result = Result.successResult();
		result.addBonusInformation(json.toString());
		return result;
	}

	@Override
	public String key() {
		return "get_list_properties";
	}
}