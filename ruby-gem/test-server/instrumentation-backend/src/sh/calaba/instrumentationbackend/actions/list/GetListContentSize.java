package sh.calaba.instrumentationbackend.actions.list;

import java.util.ArrayList;

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

/**
 * Get List View content size.
 *
 * @note Similar to iOS :contentSize getter.
 * iOS uses scroll/table view delegates to calculate total offset size.
 * For Android List Adapter is like delegates in iOS, so List Adapter
 * needs to be queried to get total content size of the list.
 *
 * @param args arguments
 * <ul>
 * 	<li>args[0] - (String) index, 0-based index of the list view; optional</li>
 * 	<li>args[1] - (String) className, specific class name of the list view, by default using ListView; optional</li>
 * 	<li>args[2] - (String) adapterClassName, specific class name of list view adapter, by default using ListAdapter; optional </li>
 * </ul>
 * @return Result execution result. Content size stored in "bonusInformation" field.
 *
 * @example Example of parsing in Ruby
 * <pre>
 * 		result = performAction('get_list_content_size')
 * 		bonusInfo = result['bonusInformation'].first
 * 		content_size = JSON.parse(bonusInfo)
 *		puts "width: " + content_size[:width]
 *		puts "height: " + content_size[:height]
 * </pre>
 */
public class GetListContentSize implements Action {
	@Override
	public Result execute(String... args) {

		int listIndex = (args.length == 0 ? 0 : (Integer.parseInt(args[0]) - 1));

		// parse 2nd and 3rd args for list view and adapter class names and use them
		// try {
  //           Class TClass = Class.forName(args.length < 2 ? "ListView" : args[1]);
  //           Class TAdapterClass = Class.forName(args.length < 3 ? "ListAdapter" : args[2]);
  //       } catch (ClassNotFoundException e) {
  //           return Result.failedResult(e.getMessage());
  //       }

		// TODO: use list and adapter classes (improvement)

		ArrayList<ListView> listViews = InstrumentationBackend.solo.getCurrentViews(ListView.class);
		if( listViews == null || listViews.size() <= listIndex ) {
			return Result.failedResult("Could not find list #" + listIndex);
		}

		ListView listView = listViews.get(listIndex);
		ListAdapter mAdapter = listView.getAdapter();

        float totalHeight = 0;
        float listWidth = listView.getWidth();
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }

        // add up dividers height
        int count = mAdapter.getCount();
		totalHeight += (listView.getDividerHeight() * (count > 0 ? count - 1 : 0));

		// build JSON string with results
		JSONObject json = new JSONObject();
		try {
			json.put("width", listWidth);
			json.put("height", totalHeight);
		} catch (JSONException e) {
            e.printStackTrace();
            return Result.failedResult(e.getMessage());
        }

		Result result = Result.successResult();
		result.addBonusInformation(json.toString());
		return result;
	}

	@Override
	public String key() {
		return "get_list_content_size";
	}
}