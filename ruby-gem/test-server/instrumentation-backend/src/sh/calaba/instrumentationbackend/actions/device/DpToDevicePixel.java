package sh.calaba.instrumentationbackend.actions.device;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

public class DpToDevicePixel implements Action {
    @Override
    public Result execute(String... args) {
        if (args.length != 1) {
            return Result.failedResult("This action takes one argument (float pixels).");
        }

        float dip;

        try {
            dip = Float.valueOf(args[0]);
        } catch (NumberFormatException e) {
            return Result.failedResult("First argument is not a valid float");
        }

        Context context = InstrumentationBackend.instrumentation.getTargetContext();
        Resources resources = context.getResources();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());

        return new Result(true, Float.toString(pixels));
    }

    @Override
    public String key() {
        return "dp_to_device_pixel";
    }
}
