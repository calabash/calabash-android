package sh.calaba.instrumentationbackend;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;


public class ClearAppData2 extends InstrumentationTestRunner {
    @Override
    public void onCreate(Bundle arguments) {
        StatusReporter statusReporter = new StatusReporter(getContext());

        try {
            System.out.println("External cache dir: " + externalCacheDir());

            if (externalCacheDir() != null) {
                System.out.println("Deleting external cache dir...");
                delete(externalCacheDir());
            }

            System.out.println("Cache dir: " + cacheDir());

            if (cacheDir() != null) {
                System.out.println("Deleting cache dir...");
                delete(cacheDir());
            }

            System.out.println("External files dir: " + externalFilesDir());

            if (externalFilesDir() != null) {
                System.out.println("Deleting external files dir...");
                delete(externalFilesDir());
            }

            System.out.println("Files dir: " + filesDir());

            if (filesDir() != null) {
                System.out.println("Deleting files dir...");
                delete(filesDir());
            }

            System.out.println("Data dir: " + dataDir());

            // Delete everything but lib in the application sandbox
            for (File file : dataDir().listFiles()) {
                if (!"lib".equals(file.getName())) {
                    delete(file);
                }
            }

            removeOwnAccountTypes();
        } catch (Exception e) {
            statusReporter.reportFailure(e);
            throw new RuntimeException(e);
        }

        statusReporter.reportFinished(StatusReporter.FinishedState.SUCCESSFUL);
    }

    private void removeOwnAccountTypes() {
        if (getTargetContext().checkCallingOrSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            final AccountManager manager = AccountManager.get(getTargetContext());
            final Account[] accounts = manager.getAccounts();
            final List<String> typesToDelete = new ArrayList<String>();

            for (Account account : accounts) {
                System.out.println("Found " + account.name + " of type " + account.type);

                Account dummy = new Account("dummy", account.type);

                try {
                    // If we can add a new account, we own the account type
                    manager.addAccountExplicitly(dummy, "", null);
                    typesToDelete.add(account.type);
                } catch (SecurityException e) {
                    // We do not own the account type
                }
            }

            for (Account account : accounts) {
                if (typesToDelete.contains(account.type)) {
                    System.out.println("Deleting " + account.name + " of type " + account.type + "...");
                    manager.removeAccount(account, null, null);
                }
            }
        }
    }

    // If provided a file will delete it.
    // If provided a directory will recursively delete files but preserve directories
    // Will never delete mono runtime files!
    private void delete(File fileOrDirectory) {
        if (fileOrDirectory == null) {
            return;
        }

        // Don't delete mono runtime
        if (".__override__".equals(fileOrDirectory.getName())) {
            return;
        }

        if (fileOrDirectory.isDirectory()) {
            if (fileOrDirectory.listFiles() != null) {
                for(File f : fileOrDirectory.listFiles()) {
                    delete(f);
                }
            }

            if (fileOrDirectory.listFiles().length == 0) {
                System.out.println("Deleting: " + fileOrDirectory);
                fileOrDirectory.delete();
            }
        } else {
            System.out.println("Deleting: " + fileOrDirectory);
            fileOrDirectory.delete();
        }
    }

    private File cacheDir() {
        return getTargetContext().getCacheDir();
    }

    private File externalCacheDir() {
        if (Build.VERSION.SDK_INT >= 8) {
            return getTargetContext().getExternalCacheDir();
        } else {
            return null;
        }
    }

    private File filesDir() {
        return getTargetContext().getFilesDir();
    }


    private File externalFilesDir() {
        File externalCacheDir = externalCacheDir();

        if (externalCacheDir == null) {
            return null;
        }

        File parentDir = externalCacheDir.getParentFile();

        if (parentDir == null) {
            return null;
        }

        return new File(parentDir, "files");
    }

    private File dataDir() {
        try {
            PackageManager packageManager = getTargetContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getTargetContext().getPackageName(), 0);

            return new File(packageInfo.applicationInfo.dataDir);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
