package sh.calaba.instrumentationbackend.actions.helpers;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * Command is inspecting the current dialog and returns a list of details about the current
 * displayed UI items.
 * 
 * @author Dominik Dary
 * 
 */
public class InspectCurrentDialog implements Action {

  @Override
  public Result execute(String... args) {
    Solo solo = InstrumentationBackend.solo;
    StringBuffer viewInfo = new StringBuffer();
    viewInfo.append("<views activity='" + solo.getCurrentActivity().getComponentName() + "'>");
    for (View view : solo.getViews()) {
      if ((view instanceof RelativeLayout) || (view instanceof LinearLayout)
          || (view instanceof ScrollView) || (view instanceof FrameLayout)) {
        // for automation are this ui elements normally not so interesting
        continue;
      }
      viewInfo.append("<view>");
      viewInfo.append("<type>" + view.getClass().getSimpleName() + "</type>");

      try {
        viewInfo.append("<name>"
            + InstrumentationBackend.solo.getCurrentActivity().getResources()
                .getResourceName(view.getId()) + "</name>");

      } catch (Exception e) {
        // in case the resource name cannot be identified
        viewInfo.append("<name></name>");
      }

      if (view instanceof TextView) {
        viewInfo.append("<textViewText>" + ((TextView) view).getText() + "</textViewText>");
      } else if (view instanceof Button) {
        viewInfo.append("<buttonText>" + ((Button) view).getText() + "</buttonText>");
      } else if (view instanceof Spinner) {
        viewInfo.append("<spinnerText>" + ((Spinner) view).getSelectedItem() + "</spinnerText>");
      } else if (view instanceof EditText) {
        viewInfo.append("<editText>" + ((EditText) view).getText() + "</editText>");
      }

      viewInfo.append("</view>");
    }

    viewInfo.append("</views>");
    Result result = new Result(true);
    result.addBonusInformation(viewInfo.toString());
    return result;
  }

  @Override
  public String key() {
    return "inspect_current_dialog";
  }

}
