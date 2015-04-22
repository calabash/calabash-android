package sh.calaba.instrumentationbackend;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FakeCameraActivity extends Activity {
    private File image;
    private File outputPath;
    ImageView imageView;
    RelativeLayout relativeLayout;
    ImageUtils.CameraOrientation cameraOrientation;

    public static final String EXTRA_IMAGE_PATH = "imagePath";

    @Override
    public void onCreate(Bundle arguments) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(arguments);

        setContentView(generateLayout());

        Intent intent = getIntent();

        if (intent != null) {
            Bundle extras = intent.getExtras();

            if (extras != null) {
                image = new File(extras.getString(EXTRA_IMAGE_PATH));
                System.out.println("Image Path: " + image);

                if (extras.containsKey(MediaStore.EXTRA_OUTPUT)) {
                    Object object = extras.get(MediaStore.EXTRA_OUTPUT);

                    if (object == null) {
                        showErrorMessage("'" + MediaStore.EXTRA_OUTPUT + "' (MediaStore.EXTRA_OUTPUT) cannot be null");
                        return;
                    }

                    if (!(object instanceof Uri)) {
                        showErrorMessage("'" + MediaStore.EXTRA_OUTPUT + "' (MediaStore.EXTRA_OUTPUT) should be a Uri, not '" + object.getClass() + "'");
                        return;
                    }

                    Uri uri = (Uri) object;
                    System.out.println("URI: " + uri);
                    System.out.println("Path: " + uri.getPath());
                    outputPath = new File(uri.getPath());

                    if (!new File(outputPath.getParent()).exists()) {
                        showErrorMessage("Directory '" + outputPath.getParent() + "' does not exist");
                        return;
                    }
                }
            } else {
                showErrorMessage("Calabash: invalid command, not extras given");
                return;
            }
        } else {
            showErrorMessage("Calabash: invalid command, not intent given");
            return;
        }

        System.out.println("CAMERA: MAX WIDTH: " + ImageUtils.getCameraMaxPictureSize().getWidth());
        System.out.println("CAMERA: MAX HEIGHT: " + ImageUtils.getCameraMaxPictureSize().getHeight());

        cameraOrientation = ImageUtils.CameraOrientation.PORTRAIT;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap unchangedImage = BitmapFactory.decodeFile(image.getAbsolutePath(), options);

        ImageUtils.CropAndScale cropAndScale = ImageUtils.cropAndScale(new ImageUtils.Size(unchangedImage.getWidth(), unchangedImage.getHeight()), ImageUtils.getCameraMaxPictureSize(), cameraOrientation);
        final Bitmap mutableBitmap = Bitmap.createBitmap(cropAndScale.scaleWidth, cropAndScale.scaleHeight, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mutableBitmap);

        Rect from = new Rect(0, 0, cropAndScale.cropWidth, cropAndScale.cropHeight);
        Rect to = new Rect(0,0, cropAndScale.scaleWidth, cropAndScale.scaleHeight);
        canvas.drawBitmap(unchangedImage, from, to, new Paint());

        unchangedImage.recycle();

        int width = mutableBitmap.getWidth() / 8;
        int height = mutableBitmap.getHeight() / 8;
        Bitmap useImage = Bitmap.createScaledBitmap(mutableBitmap, width, height, false);
        
        useImage(useImage);

        useImage.recycle();

        Paint paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStrokeWidth(mutableBitmap.getWidth() * mutableBitmap.getHeight() * 0.00001f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, mutableBitmap.getWidth(), mutableBitmap.getHeight(), paint);

        Runnable callback = new Runnable() {
            @Override
            public void run() {
                if (outputPath == null) {
                    Bitmap bitmap = ImageUtils.generateThumbnail(mutableBitmap);

                    Intent result = new Intent("inline-data");
                    result.putExtra("data", bitmap);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                } else {
                    try {
                        outputPath.createNewFile();

                        OutputStream outputStream = new FileOutputStream(outputPath);
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        mutableBitmap.recycle();
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
        relativeLayout = new RelativeLayout(this);
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
        imageView.setBackgroundColor(Color.BLACK);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        relativeLayout.addView(imageView);

        return relativeLayout;
    }

    private void showErrorMessage(String message) {
        FrameLayout frameLayout = new FrameLayout(this);
        RelativeLayout.LayoutParams frameLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        frameLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        frameLayout.setLayoutParams(frameLayoutParams);
        frameLayout.setPadding(inPx(5), inPx(5), inPx(5), inPx(5));
        frameLayout.setBackgroundColor(0xffbb3333);


        TextView textView = new TextView(this);
        ViewGroup.LayoutParams textViewLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textViewLayoutParams);
        textView.setText(message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        textView.setTextColor(0xff000000);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        frameLayout.addView(textView);

        relativeLayout.addView(frameLayout);
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

    private int inPx(int dp) {
        Resources resources = getResources();

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
