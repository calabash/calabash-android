package sh.calaba.instrumentationbackend;

import java.io.File;

import android.accounts.Account;
import android.accounts.AccountManager;
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
        try {
            final AccountManager manager = AccountManager.get(getTargetContext());
            final Account[] accounts = manager.getAccounts();

            for (Account account : accounts) {
                try {
                    manager.removeAccount(account, null, null);
                } catch (Exception e) {
                    System.out.println("Unable to remove " + account.name + " of type " + account.type);
                }
            }
        } catch (Exception e) {
            System.out.println("Error removing accounts");
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
    
	private File externalCacheDir() { 
        return getTargetContext().getExternalCacheDir();
    }

    private File cacheDir() {
        return getTargetContext().getCacheDir();
    }

}
