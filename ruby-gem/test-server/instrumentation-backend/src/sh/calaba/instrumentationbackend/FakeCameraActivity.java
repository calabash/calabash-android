package sh.calaba.instrumentationbackend;

import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FakeCameraActivity extends Activity {
    private File image;
    private File outputPath;
    ImageView imageView;
    ImageUtils.CameraOrientation cameraOrientation;

    public static final String EXTRA_IMAGE_PATH = "imagePath";

    @Override
    public void onCreate(Bundle arguments) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(arguments);

        Intent intent = getIntent();

        if (intent != null) {
            Bundle extras = intent.getExtras();

            if (extras != null) {
                image = new File(extras.getString(EXTRA_IMAGE_PATH));
                System.out.println("Image Path: " + image);

                if (extras.containsKey(MediaStore.EXTRA_OUTPUT)) {
                    Uri uri = (Uri) extras.get(MediaStore.EXTRA_OUTPUT);
                    System.out.println("URI: " + uri);
                    System.out.println("Path: " + uri.getPath());
                    outputPath = new File(uri.getPath());
                }
            } else {
                setResult(Activity.RESULT_CANCELED);
                finish();
                return;
            }
        } else {
            setResult(Activity.RESULT_CANCELED);
            finish();
            return;
        }

        cameraOrientation = ImageUtils.CameraOrientation.PORTRAIT;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), options);

        Bitmap croppedAndScaledImage = ImageUtils.generateCroppedAndScaled(bitmap, ImageUtils.getCameraMaxPictureSize(), cameraOrientation);

        setContentView(generateLayout());
        useImage(croppedAndScaledImage);

        final Bitmap postProcessedImage = ImageUtils.postProcessImage(croppedAndScaledImage);

        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (outputPath == null) {
                    Bitmap bitmap = ImageUtils.generateThumbnail(postProcessedImage);

                    Intent result = new Intent("inline-data");
                    result.putExtra("data", bitmap);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                } else {
                    try {
                        outputPath.getParentFile().mkdirs();
                        outputPath.createNewFile();

                        OutputStream outputStream = new FileOutputStream(outputPath);
                        postProcessedImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResult(Activity.RESULT_CANCELED);
                        finish();
                    }

                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
            }
        };

        startAnimation(callback);
    }

    private void startAnimation(final Runnable callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = new AlphaAnimation(0, 1);
                        animation.setDuration(2000);
                        imageView.startAnimation(animation);
                    }
                });

                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(callback);
            }
        }).start();
    }

    private View generateLayout() {
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relativeLayoutLayoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);
        relativeLayoutLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        relativeLayout.setLayoutParams(relativeLayoutLayoutParams);
        relativeLayout.setBackgroundColor(Color.WHITE);

        imageView = new ImageView(this);

        RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);

        imageViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageView.setLayoutParams(imageViewLayoutParams);
        imageView.setBackgroundColor(Color.BLUE);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        relativeLayout.addView(imageView);

        return relativeLayout;
    }

    private void useImage(Bitmap image) {
        int screenWidth, screenHeight;

        Display display = getWindowManager().getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 13) {
            Point screenSize = new Point();
            display.getSize(screenSize);
            screenWidth = screenSize.x;
            screenHeight = screenSize.y;
        } else {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }

        Bitmap scaledImage = Bitmap.createScaledBitmap(image, screenWidth, screenHeight, false);
        imageView.setImageBitmap(scaledImage);
    }
}
