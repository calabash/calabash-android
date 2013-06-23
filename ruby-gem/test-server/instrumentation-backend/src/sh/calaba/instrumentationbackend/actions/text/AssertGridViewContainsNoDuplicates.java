package sh.calaba.instrumentationbackend.actions.text;

import java.util.ArrayList;
import java.util.LinkedList;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Optional arg is 1-based index - eg: assert_no_duplicates_in_grid, "1" examines the first grid
 * @author Nicholas Albion
 */
public class AssertGridViewContainsNoDuplicates implements Action {

	@Override
	public Result execute(String... args) {
		ArrayList<GridView> gridViews = InstrumentationBackend.solo.getCurrentViews(GridView.class);

		if( gridViews.size() == 0 ) {
			return new Result(false, "Could not find any grid views");
		}

		int index;
		LinkedList<String> duplicates;
		if( args.length == 1 ) {
			index = Integer.parseInt(args[0]) - 1;
			duplicates = assertNoDuplicatesInGrid( gridViews.get(index) );
		} else {
			index = gridViews.size() - 1;
			do {
				GridView gridView = gridViews.get(index);
				duplicates = assertNoDuplicatesInGrid(gridView);
				if( duplicates.size() != 0 ) {
					break;
				}
			} while( index-- != 0 );
		}

		if( duplicates.size() == 0 ) {
			return Result.successResult();
		} else {
			Result result = new Result(false, "Duplicates were found in GridView #" + (index + 1) + ": " + duplicates);
			result.setExtras( duplicates );
			return result;
		}
	}

	private LinkedList<String> assertNoDuplicatesInGrid( GridView gridView ) {
		//InstrumentationBackend.solo.getViews( gridView );
		ArrayList<TextView> textViews = InstrumentationBackend.solo.getCurrentViews(TextView.class, gridView );
		LinkedList<String> textValues = new LinkedList<String>();
		LinkedList<String> duplicates = new LinkedList<String>();

		for (TextView textView : textViews) {
			String text = textView.getText().toString();
			if( textValues.contains(text) ) {
				duplicates.add(text);
			} else {
				textValues.add(text);
			}
		}
		return duplicates;
	}

	@Override
	public String key() {
		return "assert_no_duplicates_in_grid";
	}
}