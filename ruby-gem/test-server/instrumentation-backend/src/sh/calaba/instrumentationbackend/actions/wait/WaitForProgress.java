package sh.calaba.instrumentationbackend.actions.wait;


import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;
import android.view.View;
import android.widget.ProgressBar;


public class WaitForProgress implements Action {

    @Override
    public Result execute(String... args) {
        int timeout = 60000;
        long waitUntil = System.currentTimeMillis() + timeout;

        while (numberOfVisibleProgressBars() > 0) {
            if (System.currentTimeMillis() > waitUntil) {
                return new Result(false, "Timedout waiting for no visible progressbars (" + numberOfVisibleProgressBars() +" still visible).");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Result.successResult();
    }

    @Override
    public String key() {
        return "wait_for_no_progress_bars";
    }

    private int numberOfVisibleProgressBars() {
        int visibleProgressBars = 0;
        for (ProgressBar v : InstrumentationBackend.solo.getCurrentViews(ProgressBar.class)) {
            if (v != null & v.getVisibility() != View.GONE && v.getVisibility() != View.INVISIBLE && v.isShown()) {
                visibleProgressBars++;
                System.out.println("Found visible progressbar number:" + visibleProgressBars);
            }
        }
        return visibleProgressBars;
    }

}
