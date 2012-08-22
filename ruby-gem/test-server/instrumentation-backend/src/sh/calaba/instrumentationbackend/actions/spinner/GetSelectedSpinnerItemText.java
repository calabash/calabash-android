package sh.calaba.instrumentationbackend.actions.spinner;

import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;
import android.widget.Spinner;

public class GetSelectedSpinnerItemText implements Action {

    @Override
    public Result execute(String... args) {
        final String idArgument = args[0];
        final Spinner foundView = TestHelpers.getViewById(idArgument, Spinner.class);
        
        if( null == foundView ) {
            return notFoundResult(idArgument);
        }
        
        return new Result(true, getSelectedSpinnerText(foundView));
    }

    private String getSelectedSpinnerText(final Spinner foundView) {
        Object selectedItem = foundView.getSelectedItem();
        return (null != selectedItem) ? selectedItem.toString() : "";
    }

    private Result notFoundResult(final String idArgument) {
        return Result.failedResult(String.format("Could not find %s with id %s.", Spinner.class.getName(), idArgument));
    }

    @Override
    public String key() {
        return "get_selected_spinner_item_text";
    }

}
