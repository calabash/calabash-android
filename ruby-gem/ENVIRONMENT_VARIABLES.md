## Calabash Android Environment Variables

Calabash Android references Unix environment variables to control its runtime behavior.

## Conventions

Paths or values with spaces need double or single quotes.

#### Example: Quoting values with spaces.

```
APP_PATH="~/path with/a spaces/in it"   # Correct!
APP_PATH=~/path with/a spaces/in it     # Incorrect.
```

## Environment Variables

### `ADB_DEVICE_ARG`

#### Example

```
ADB_DEVICE_ARG=4df7ee16Xa083009
ADB_DEVICE_ARG=C4F12B41QB6E89E
```

### `APP_PATH`

The path to apk that is being tested.

Use this variable if your apk is located in a non-default location or your environment requires special configuration (e.g. example a CI environment).

### `MAIN_ACTIVITY`

Calabash Android will automatically try to detect the main activity of
the application.  Use this variable to specify an alternative main activity.

#### Example

```
MAIN_ACTIVITY="com.example.myactivity" calabash-android run example.apk
```

### `CALABASH_IRBRC`

Use this variable to load a custom .irbrc when opening calabash-ios console.  This is useful if you have multiple calabash projects and want to share an .irbrc across all of them.

#### .irbrc load order rules

1. If `CALABASH_IRBRC` is defined, then that .irbrc is loaded.
2. If there is a .irbrc in the directory where `console` is called, then that file is loaded.
3. Otherwise, the defaults scripts/.irbrc is loaded.

#### Special

Calling `calabash-ios console` sets the `IRBRC` environment variable.

#### Example

```
$ CALABASH_IRBRC="~/.irbrc-calabash" calabash-ios console
```

### `RESET_BETWEEN_SCENARIOS`

Use this variable to reset your app's data between cucumber Scenarios.

#### Pro Tip:  Reset the app data before certain Scenarios.

Use a `Before` hook + a `tag` to control when calabash will reset the app data.

### `SCREENSHOT_PATH`

Use this variable to apply a 'prefix' to a screenshot when saving.  See the examples.

#### Note

The behavior of this variable is subject to change.

#### Special

If the the *path* portion of SCREENSHOT_PATH does not exist, `screenshot` will raise an error.

@see {Calabash::Android::Operations#screenshot}

#### Example: Specify a prefix

```
SCREENSHOT_PATH=galaxy_s5_                   => galaxy_s5_screenshot_0.png
SCREENSHOT_PATH="screenshots/nexus5-" => screenshots/nexus5-screenshot_0.png
```

#### Example: Specify a directory

```
# correct!
SCREENSHOT_PATH=/path/to/a/directory/ => path/to/a/directory/screenshot_0.png
# incorrect :(
SCREENSHOT_PATH=/path/to/a/directory  => path/to/a/directoryscreenshot_0.png
```

### `TEST_APP_PATH`

Calabash Android will detect the test-server apt based on the checksum of the application and the Calabash Android version.

Use this variable if your test-server is located in a non-default location or environment requires special configuration (e.g. example a CI environment).
