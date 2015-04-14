package sh.calaba.instrumentationbackend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageUtils {
    private static boolean hasReadCameraParameters = false;
    private static List<Camera.Size> cameraParametersSupportedPictureSizes;
    private static List<Integer> cameraParametersSupportedPictureFormats;

    public static List<Size> getCameraSupportedPictureSizes() {
        obtainCameraParameters();

        List<Camera.Size> cameraSizes = cameraParametersSupportedPictureSizes;
        List<Size> sizes = new ArrayList<Size>(cameraSizes.size());

        for (Camera.Size cameraSize : cameraSizes) {
            sizes.add(new Size(cameraSize.width, cameraSize.height));
        }

        return sizes;
    }

    public static List<Integer> getCameraSupportedPictureFormats() {
        obtainCameraParameters();

        return cameraParametersSupportedPictureFormats;
    }

    public static Size getCameraMaxPictureSize() {
        List<Size> sizes = getCameraSupportedPictureSizes();
        Collections.sort(sizes);

        return sizes.get(sizes.size() - 1);
    }

    public static Bitmap generateCroppedAndScaled(Bitmap image, Size maxResolution, CameraOrientation cameraOrientation) {
        ImageUtils.Size imageSize = new ImageUtils.Size(image.getWidth(), image.getHeight());

        int desiredWidth, desiredHeight;
        int cameraWidth, cameraHeight;

        if (cameraOrientation == CameraOrientation.PORTRAIT) {
            cameraWidth = maxResolution.min();
            cameraHeight = maxResolution.max();
        } else {
            cameraWidth = maxResolution.max();
            cameraHeight = maxResolution.min();
        }

        double aspectRatioCamera = (double)cameraWidth / (double)cameraHeight;
        double aspectRatioImage = (double)imageSize.getWidth() / (double)imageSize.getHeight();

        if (aspectRatioCamera > aspectRatioImage) {
            desiredWidth = imageSize.getWidth();
            desiredHeight = (int) Math.floor(imageSize.getWidth() / aspectRatioCamera);
        } else if (aspectRatioCamera < aspectRatioImage) {
            desiredWidth = (int) Math.floor(imageSize.getHeight() * aspectRatioCamera);
            desiredHeight = imageSize.getHeight();
        } else {
            desiredWidth = imageSize.getWidth();
            desiredHeight = imageSize.getHeight();
        }

        Bitmap croppedImage = Bitmap.createBitmap(image, 0, 0, desiredWidth, desiredHeight);

        return Bitmap.createScaledBitmap(croppedImage, cameraWidth, cameraHeight, false);
    }

    public static Bitmap generateThumbnail(Bitmap bitmap) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.outWidth = bitmap.getWidth();
        opts.outHeight = bitmap.getHeight();
        int sampleSize = computeSampleSize(opts, -1, 5 * 1024);

        int thumbWidth = bitmap.getWidth() * 4 / sampleSize;
        int thumbHeight = bitmap.getHeight() * 4 / sampleSize;

        return Bitmap.createScaledBitmap(bitmap, thumbWidth, thumbHeight, false);
    }

    public static Bitmap postProcessImage(Bitmap image) {
        Bitmap modifiedImage = image.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(modifiedImage);
        Paint paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStrokeWidth(modifiedImage.getWidth()*modifiedImage.getHeight()*0.00001f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, modifiedImage.getWidth(), modifiedImage.getHeight(), paint);

        return modifiedImage;
    }

    public static boolean isCameraValid() {
        return (getCameraSupportedPictureFormats().contains(ImageFormat.JPEG));
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    private static void obtainCameraParameters() {
        if (!hasReadCameraParameters) {
            android.hardware.Camera camera = android.hardware.Camera.open();
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            cameraParametersSupportedPictureSizes = parameters.getSupportedPictureSizes();
            cameraParametersSupportedPictureFormats = parameters.getSupportedPictureFormats();
            camera.release();
            hasReadCameraParameters = true;
        }
    }

    public enum CameraOrientation {PORTRAIT, LANDSCAPE}

    public static class Size implements Comparable<Size> {
        private int width;
        private int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int max() {
            return Math.max(getWidth(), getHeight());
        }

        public int min() {
            return Math.min(getWidth(), getHeight());
        }

        public int getArea() {
            return getWidth() * getHeight();
        }

        @Override
        public int compareTo(Size other) {
            int area = getArea();
            int otherArea = other.getArea();

            if (area < otherArea) {
                return -1;
            } else if(area > otherArea) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
