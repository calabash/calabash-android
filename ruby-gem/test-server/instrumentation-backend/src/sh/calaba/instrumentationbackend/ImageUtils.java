/*
 * This file contains code from:
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modified by Tobias Roikjer <tobias.roikjer@xamarin.com>
 * For use with Calabash Android
 */

package sh.calaba.instrumentationbackend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageUtils {
    private static boolean hasReadCameraParameters = false;
    private static List<Camera.Size> cameraParametersSupportedPictureSizes;
    private static List<Integer> cameraParametersSupportedPictureFormats;

    public static List<Size> getCameraSupportedPictureSizes() throws CameraException {
        obtainCameraParameters();

        List<Camera.Size> cameraSizes = cameraParametersSupportedPictureSizes;
        List<Size> sizes = new ArrayList<Size>(cameraSizes.size());

        for (Camera.Size cameraSize : cameraSizes) {
            sizes.add(new Size(cameraSize.width, cameraSize.height));
        }

        return sizes;
    }

    public static List<Integer> getCameraSupportedPictureFormats() throws CameraException {
        obtainCameraParameters();

        return cameraParametersSupportedPictureFormats;
    }

    public static Size getCameraMaxPictureSize() throws CameraException {
        List<Size> sizes = getCameraSupportedPictureSizes();
        Collections.sort(sizes);

        return sizes.get(sizes.size() - 1);
    }

    public static class CropAndScale {
        public int cropWidth;
        public int cropHeight;
        public int scaleWidth;
        public int scaleHeight;

        public CropAndScale(int cropWidth, int cropHeight, int scaleWidth, int scaleHeight) {
            this.cropWidth = cropWidth;
            this.cropHeight = cropHeight;
            this.scaleWidth = scaleWidth;
            this.scaleHeight = scaleHeight;
        }
    }

    public static CropAndScale cropAndScale(Size imageSize, Size maxResolution, CameraOrientation cameraOrientation) {
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

        System.out.println("CAMERA ARC: " + aspectRatioCamera);
        System.out.println("CAMERA ARI: " + aspectRatioImage);
        System.out.println("CAMERA DW: " + desiredWidth);
        System.out.println("CAMERA DH: " + desiredHeight);

        return new CropAndScale(desiredWidth, desiredHeight, cameraWidth, cameraHeight);

        /*Bitmap croppedImage = Bitmap.createBitmap(image, 0, 0, desiredWidth, desiredHeight);
        Canvas canvas = new Canvas(croppedImage);
        canvas.scale(cameraWidth, cameraHeight);

        return croppedImage;*/

        //Bitmap croppedImage = Bitmap.createBitmap(image, 0, 0, desiredWidth, desiredHeight);
        //image = Bitmap.createScaledBitmap(croppedImage, cameraWidth, cameraHeight, false);
        //croppedImage.recycle();
    }

    public static Bitmap generateThumbnail(Bitmap image) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.outWidth = image.getWidth();
        opts.outHeight = image.getHeight();
        int sampleSize = computeSampleSize(opts, -1, 5 * 1024);

        int thumbWidth = image.getWidth() * 4 / sampleSize;
        int thumbHeight = image.getHeight() * 4 / sampleSize;

        return Bitmap.createScaledBitmap(image, thumbWidth, thumbHeight, false);
    }

    public static void postProcessImage(Bitmap image) {
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();

        paint.setColor(Color.RED);
        paint.setStrokeWidth(image.getWidth() * image.getHeight() * 0.00001f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, image.getWidth(), image.getHeight(), paint);
    }

    public static boolean isCameraValid() throws CameraException {
        return (getCameraSupportedPictureFormats().contains(ImageFormat.JPEG));
    }

    public static void writeBitmapPixelsToFile(Bitmap in, File out, int width, int height) throws IOException {
        int imageWidth = in.getWidth();
        int imageHeight = in.getHeight();

        if (width > imageWidth || height > imageHeight) {
            throw new RuntimeException("Pixel size cannot be larger than the image size.");
        }

        RandomAccessFile randomAccessFile  = new RandomAccessFile(out, "rw");

        try {
            final int maxReadHeight = 100;
            FileChannel fileChannel = randomAccessFile.getChannel();
            MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, width * height * 4);
            ByteBuffer byteBuffer = ByteBuffer.allocate(maxReadHeight * width * 4);
            IntBuffer intBuffer = byteBuffer.asIntBuffer();

            for (int y = 0; y < height; y += maxReadHeight) {
                int h = Math.min(maxReadHeight, height - y);
                int[] pixels = new int[width * h];
                in.getPixels(pixels, 0, width, 0, y, width, h);

                if (h == height - y) {
                    byteBuffer = ByteBuffer.allocate(h * width * 4);
                    intBuffer = byteBuffer.asIntBuffer();
                } else {
                    byteBuffer.rewind();
                    intBuffer.rewind();
                }

                /*for (int i = 0; i < pixels.length; i++) {
                    pixels[i] = pixels[i] << 8;
                }*/

                intBuffer.put(pixels);
                map.put(byteBuffer);
            }
        } finally {
            randomAccessFile.close();
        }
    }

    public static void loadBitmapFromFile(Bitmap image, File in, int originalWidth, int originalHeight) throws IOException {
        int outputWidth = image.getWidth();
        int outputHeight = image.getHeight();
        int xTimes = (int) Math.ceil((double) outputWidth / (double)originalWidth);
        int yTimes = (int) Math.ceil((double) outputHeight / (double)originalHeight);

        RandomAccessFile randomAccessFile = new RandomAccessFile(in, "r");

        try {
            FileChannel fileChannel = randomAccessFile.getChannel();
            MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, originalWidth * originalHeight * 4);

            for (int y = 0; y < originalHeight; y++) {
                byte[] bytes = new byte[originalWidth * 4];
                map.get(bytes);

                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                IntBuffer intBuffer = byteBuffer.asIntBuffer();

                int[] intArray = new int[intBuffer.limit()];
                intBuffer.get(intArray);

                for (int i = 0; i < xTimes; i++) {
                    for (int j = 0; j < yTimes; j++) {
                        int w;

                        if (i < xTimes - 1 || (outputWidth % originalWidth) == 0) {
                            w = originalWidth;
                        } else {
                            w = outputWidth % originalWidth;
                        }

                        if (j * originalHeight + y < outputHeight) {
                            image.setPixels(intArray, 0, originalWidth, i * originalWidth, j * originalHeight + y, w, 1);
                        }
                    }
                }
            }
        } finally {
            randomAccessFile.close();
        }
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

    private static void obtainCameraParameters() throws CameraException {
        if (!hasReadCameraParameters) {
            android.hardware.Camera camera;

            // Find the first back facing camera
            try {
                camera = android.hardware.Camera.open();
            } catch (RuntimeException e) {
                throw new CameraException(e);
            }

            if (Build.VERSION.SDK_INT >= 9) {
                System.out.println("CAMERA: Number of cameras: " + android.hardware.Camera.getNumberOfCameras());

                if (camera == null) {
                    System.out.println("CAMERA: was null");
                    camera = android.hardware.Camera.open(0);
                }
            }

            if (camera == null) {
                throw new CameraException("Could not find any camera");
            }

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

    public static class CameraException extends Exception {
        public CameraException(Throwable throwable) {
            super(throwable);
        }

        public CameraException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public CameraException(String detailMessage) {
            super(detailMessage);
        }

        public CameraException() {
            super();
        }
    }
}
