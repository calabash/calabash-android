package sh.calaba.instrumentationbackend;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;

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
	
    //If provided a file will delete it. 
    //If provided a directory will recursively delete files but preserve directories
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

    @SuppressLint("NewApi")
	private File externalCacheDir() { 
        return getTargetContext().getExternalCacheDir();
    }

    private File cacheDir() {
        return getTargetContext().getCacheDir();
    }

}
