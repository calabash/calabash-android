package sh.calaba.instrumentationbackend.intenthook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import sh.calaba.instrumentationbackend.FakeCameraActivity;
import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.actions.Utils;

public class TakePictureHook extends IntentHookWithDefault {
    private static String OUTPUT_PATH = "_cal_take_picture_image.jpg";
    private File imageFile;

    public TakePictureHook(File imageFile) throws IOException {
        super();

        if (imageFile == null) {
            throw new IllegalArgumentException("image file cannot be null");
        } else if (!imageFile.exists()) {
            throw new IllegalArgumentException("image file '" + imageFile + "' does not exist");
        }

        Context context = InstrumentationBackend.instrumentation.getTargetContext();

        // Make the image file world readable
        OutputStream fileOutputStream = context.openFileOutput(OUTPUT_PATH, Context.MODE_WORLD_READABLE);
        InputStream fileInputStream = new FileInputStream(imageFile);

        Utils.copyContents(fileInputStream, fileOutputStream);

        fileInputStream.close();
        fileOutputStream.close();

        // context.openFileOutput will place the file in /data/data/<pkg>/files/<file>
        this.imageFile = new File(context.getFilesDir(), OUTPUT_PATH);
    }

    @Override
    public IntentHookResult defaultHook(Activity target, Intent intent) {
        // Do not start our activity if the device would not have opened a camera app
        if (intent.resolveActivity(InstrumentationBackend.instrumentation.getTargetContext().getPackageManager()) != null) {
            Intent modifiedIntent = new Intent(intent);

            Context instrumentationContext = InstrumentationBackend.instrumentation.getContext();
            String pkg = instrumentationContext.getPackageName();

            modifiedIntent.setComponent(new ComponentName(pkg, FakeCameraActivity.class.getName()));
            modifiedIntent.putExtra(FakeCameraActivity.EXTRA_IMAGE_PATH, imageFile.getAbsolutePath());

            return new IntentHookResult(null, false, modifiedIntent);
        } else {
            return new IntentHookResult(null, false);
        }
    }

    @Override
    public void onRemoved() {
        //new File("/sdcard/tmp.jpg").delete();
    }
}
