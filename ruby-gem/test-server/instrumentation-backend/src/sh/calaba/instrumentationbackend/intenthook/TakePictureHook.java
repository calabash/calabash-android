package sh.calaba.instrumentationbackend.intenthook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sh.calaba.instrumentationbackend.FakeCameraActivity;
import sh.calaba.instrumentationbackend.InstrumentationBackend;

public class TakePictureHook extends IntentHookWithDefault {
    private static String OUTPUT_PATH = "_cal_take_picture_image.jpg";
    private File imageFile;

    public TakePictureHook(byte[] imageData) throws IOException {
        super();

        if (imageData == null) {
            throw new IllegalArgumentException("ImageData cannot be null");
        }

        Context context = InstrumentationBackend.instrumentation.getTargetContext();

        FileOutputStream fileOutputStream = context.openFileOutput(OUTPUT_PATH, Context.MODE_WORLD_READABLE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        imageFile = new File(context.getFilesDir(), OUTPUT_PATH);
        fileOutputStream.close();
    }

    @Override
    public IntentHookResult defaultHook(Activity target, Intent intent) {
        Intent modifiedIntent = new Intent(intent);

        Context instrumentationContext = InstrumentationBackend.instrumentation.getContext();
        String pkg = instrumentationContext.getPackageName();

        modifiedIntent.setComponent(new ComponentName(pkg, FakeCameraActivity.class.getName()));
        modifiedIntent.putExtra(FakeCameraActivity.EXTRA_IMAGE_PATH, imageFile.getAbsolutePath());

        return new IntentHookResult(null, false, modifiedIntent);
    }

    @Override
    public void onRemoved() {
        //new File("/sdcard/tmp.jpg").delete();
    }
}
