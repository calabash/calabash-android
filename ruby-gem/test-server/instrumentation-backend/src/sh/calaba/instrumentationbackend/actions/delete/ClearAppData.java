package sh.calaba.instrumentationbackend.actions.delete;

import sh.calaba.instrumentationbackend.InstrumentationBackend;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.actions.Action;

import java.io.File;

import android.util.DisplayMetrics;
import android.content.Context;
import android.accounts.Account;
import android.accounts.AccountManager;

/*
 *
 * deleteDirectoryContents and deleteRecursively are based on Guava
 * which is Apache licensed.
 *
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ClearAppData implements Action {

	public Context targetContext = null;

	/**
	 * <p>
	 * Deletes all the files within a directory. Does not delete the directory
	 * itself.
	 * 
	 * <p>
	 * If the file argument is a symbolic link the link will be
	 * deleted.
	 * 
	 * @param directory
	 *            the directory to delete the contents of
	 */
	public static void deleteDirectoryContents(File directory) {
		if (directory == null || (!directory.isDirectory()))
			return;

		final File[] files = directory.listFiles();

		if (files == null)
			return;

		for (File file : files) {
			deleteRecursively(file);
		}
	}

	/**
	 * <p>
	 * Deletes a file or directory and all contents recursively.
	 * 
	 * <p>
	 * If the file argument is a symbolic link the link will be deleted but not
	 * the target of the link. If the argument is a directory, symbolic links
	 * within the directory will not be followed.
	 * 
	 * @param file
	 *            the file to delete
	 */
	public static void deleteRecursively(File file) {
		if (file == null)
			return;

		if (file.isDirectory()) {
			deleteDirectoryContents(file);
		}

		file.delete();
	}

	@Override
	public Result execute(String... args) {

    final File internalCache = targetContext.getCacheDir();

    if (internalCache != null) {
      // Delete contents of app folder on internal storage.
      final File internalApp = new File(internalCache.getParent());
      deleteDirectoryContents(internalApp);
    }

    final File externalCache = targetContext.getExternalCacheDir();

    if (externalCache != null) {
      // Delete contents of app folder on external storage.
      final File externalApp = new File(externalCache.getParent());
      deleteDirectoryContents(externalApp);
    }

    // Delete accounts
    final AccountManager manager = AccountManager.get(targetContext);
    final Account[] accounts = manager.getAccounts();

    for (Account account : accounts) {
      try {
        manager.removeAccount(account, null, null);
      } catch (Exception e) {}
    }

		return Result.successResult();
	}

	@Override
	public String key() {
		return "clear_app_data";
	}
}
