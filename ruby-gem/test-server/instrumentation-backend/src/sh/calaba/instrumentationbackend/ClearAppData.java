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

    private void delete(File file_or_directory) {
        if (file_or_directory == null) {
            return;
        }

        if (file_or_directory.isDirectory()) {
            if (file_or_directory.listFiles() != null) {
                for(File f : file_or_directory.listFiles()) {
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
