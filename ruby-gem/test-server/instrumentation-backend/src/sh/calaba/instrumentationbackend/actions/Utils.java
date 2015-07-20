package sh.calaba.instrumentationbackend.actions;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void copyContents(InputStream from, OutputStream to) throws IOException {
        byte[] buffer = new byte[4*1024];

        int read;

        while ((read = from.read(buffer)) != -1) {
            to.write(buffer, 0, read);
        }
    }
}