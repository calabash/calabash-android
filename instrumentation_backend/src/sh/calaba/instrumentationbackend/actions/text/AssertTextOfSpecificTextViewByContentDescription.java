package sh.calaba.instrumentationbackend.actions.text;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.widget.TextView;

/**
 * Asserts that a TextView with the given content description (args[1]) and that it has the expected text (args[0]) 
 * 
 */
public class AssertTextOfSpecificTextViewByContentDescription implements Action {

	@Override
	public Result execute(String... args) {
		String expectedText = args[0];
		TextView view = TestHelpers.getTextViewByDescription(args[1]);
		if (view == null) {
			return new Result(false, "No view found with content description: '" + args[1] + "'");
		} else if (view.getText().toString().equalsIgnoreCase(expectedText)) {
			return Result.successResult();
		} else {
			return new Result(false, "Expected: '" + expectedText + "', found:" + view.getText() + "'");
		}
	}

	@Override
	public String key() {
		return "assert_text_in_textview";
	}

}
