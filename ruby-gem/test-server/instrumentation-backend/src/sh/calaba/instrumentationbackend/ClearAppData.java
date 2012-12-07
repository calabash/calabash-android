package sh.calaba.instrumentationbackend;

import android.content.Context;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import sh.calaba.instrumentationbackend.actions.HttpServer;

import java.io.File;
import java.lang.reflect.Method;

public class ClearAppData extends InstrumentationTestRunner {
	@Override
    public void onCreate(Bundle arguments) {
        if (externalCacheDir() != null) {
            delete(externalCacheDir().getParentFile());
        }
        if (cacheDir() != null) {
            delete(cacheDir().getParentFile());
        }
	}

    /** Deletes the file or all the files contained in a folder recursively. **/
    private static void delete(File file_or_directory) {
        if (file_or_directory == null) {
            return;
        }

        if (file_or_directory.isDirectory()) {
            Files[] files = file_or_directory.listFiles();
            if (files != null) {
                for(File f : files) {
                    delete(f);
                }
            }
        } else {
            file_or_directory.delete();
        }
    }

    private File externalCacheDir() {
        return getTargetContext().getExternalCacheDir();
    }

    private File cacheDir() {
        return getTargetContext().getCacheDir();
    }

}
