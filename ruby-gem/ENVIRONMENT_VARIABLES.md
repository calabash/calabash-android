## Calabash Android Environment Variables

Calabash Android references Unix environment variables to control its runtime behavior.

## Conventions

Variables that take boolean values should be passed as `0` or `1`, _not_ as `true` or `false`.

#### Example: Turn on verbose logging.

```
DEBUG=1      # Correct!
DEBUG=true   # Incorrect.
```

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

@Tobias here.

#### Example

### `CALABASH_IRBRC`

@Tobias - Maybe docs here for IRBRC?  Update the load-order rules?

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

### `CALABASH_NO_DEPRECATION`

@Tobias Do you have something like this?

Calabash deprecation warnings getting you down?  Use this variable to turn off deprecation warnings.

It is not recommended that you turn off deprecation warnings. One morning you will wake up and find that everything is broken; it will make you grumpy.

#### Example

```
CALABASH_NO_DEPRECATION=1 cucumber
```

#### Pro Tip: Read the deprecation warnings.

Read the deprecation warnings for the replacement API.

### `DEBUG`

Set this variable to `1` to enable verbose logging.

#### Example

```
DEBUG=1 cucumber
```

#### Pro Tip: Reduce console spam from third-party gems.

If you are seeing a bunch of spam from tools like bundler you should unset this variable.

```
shell: unset DEBUG
 ruby: ENV.delete('DEBUG')
```

### `RESET_BETWEEN_SCENARIOS`

@Tobias - Update this to describe the behavior on Calabash Android

***The behavior of this variable differs depending on test platform.  Read this carefully.***

Use this variable to reset your app's sandbox between cucumber Scenarios.

Outside of the Xamarin Test Cloud, it is not possible to reset an app's sandbox on physical devices.  The app must be deleted and re-installed.  Calabash cannot delete .ipas from or deploy .ipas to physical devices.

When testing locally on physical devices, this variable is ignored.

To recap:

1. You can use this variable when targeting simulators.
2. You can use this variable when testing on the Xamarin Test Cloud.
3. This variable is ignored during local testing against physical devices.

#### Pro Tip:  Reset the app sandbox before certain Scenarios.

Use a `Before` hook + a `tag` to control when calabash will reset the app sandbox.

See this Stack Overflow post: http://stackoverflow.com/questions/24493634/reset-ios-app-in-calabash-ios 

### `SCREENSHOT_PATH`

@Tobias How about this?

Use this variable to apply a 'prefix' to a screenshot when saving.  See the examples.

#### Note

The behavior of this variable is subject to change.

#### Special

If the the *path* portion of SCREENSHOT_PATH does not exist, `screenshot` will raise an error.

@see {Calabash::Cucumber::FailureHelpers#screenshot}

#### Example: Specify a prefix

```
SCREENSHOT_PATH=ipad_                   => ipad_screenshot_0.png
SCREENSHOT_PATH="screenshots/iphone5s-" => screenshots/iphone5s-screenshot_0.png
```

#### Example: Specify a directory

```
# correct!
SCREENSHOT_PATH=/path/to/a/directory/ => path/to/a/directory/screenshot_0.png
# incorrect :(
SCREENSHOT_PATH=/path/to/a/directory  => path/to/a/directoryscreenshot_0.png
```

### `TEST_APP_PATH`

@Tobias - here

#### Example


## Variables for Predefined Steps

@Tobias - Update or remove this section.

Using the predefined steps is not recommended; they are provided as a way of introducing BDD concepts, cucumber, gherkin, and calabash.  Every project should cultivate its own vernacular - a shared language
between developers, clients, and users.

There is an internal debate about whether or not deprecate  the predefined steps.

Outside of the predefined steps, these variable have no effect.

### `WAIT_TIMEOUT`

If you are using the calabash predefined steps, you can use this variable to globally control the timeout for the `wait_*` methods.

Defaults to 30 seconds.

#### Pro Tip: Enforce a global wait timeout.

It is good idea to set a global timeout for your project and to never deviate from that timeout without documenting why.  A common bad practice is to fix failing tests by increasing the timeout.  This slows down the tests and hides bugs.  In practice, a 14 second wait time is recommended.

### `STEP_PAUSE`

If you are using the calabash predefined steps, you can use this variable to globally control how long to `sleep`.

Defaults to 0.5 seconds.

#### Pro Tip: Avoid sleeps.

You should avoid `sleep` whenever possible.  You should always prefer to `wait` for something and then proceed.  Waiting has the advantage that you only ever wait as long as you need to, which optimizes test run times.

#### Pro Tip: Enforce a global sleep value.

There are times when sleep cannot be avoided.   It is very good idea to set a global sleep value and to never deviate from that value without documenting why.  A common bad practice is to fix failing tests by
increasing sleep times.  This slows down the tests and hides bugs.

* @see {Calabash::Cucumber::Core#wait_tap}
* @see {Calabash::Cucumber::WaitHelpers}

## Variables for Gem Developers

@Tobias - Update or remove this section.

These variables are reserved for gem developers.  Normal users should not set alter these variables.
