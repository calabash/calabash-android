package sh.calaba.instrumentationbackend.actions.view;

import android.view.View;

import java.util.Map;

/**
 * Created by john7doe on 06/01/15.
 */
public interface IOnViewAction {
    String doOnView(View view);
    String doOnView(Map viewMap);
}
