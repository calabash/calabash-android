package sh.calaba.instrumentationbackend.actions.spinner;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.Spinner;

/**
 * Action for selecting a item from a Spinner
 * First arg is the ID of the Spinner
 * Second arg is the text of the item to be selected
 *
 */
public class SelectSpinnerItemById implements Action {

    @Override
    public Result execute(String... args) {
        String id = args[0];
        String item = args[1];
        View view = TestHelpers.getViewById(id);
        if(view == null) {
        	
            return new Result(false, "No view found with id: '" + id + "'");
        } else if (!(view instanceof Spinner)) {
            return new Result(false, "Expected Spinner found: '" + view.getClass() + "'");
        } else {
        	//Open spinner
            InstrumentationBackend.solo.clickOnView(view);
            //Select item
            InstrumentationBackend.solo.clickOnText(item);
            return Result.successResult();
        }
    }

    @Override
    public String key() {
        return "select_item_from_id_spinner";
    }

}
