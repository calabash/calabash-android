Ruby API
========

When writing custom steps, you'll need to use the Ruby API to interact with your application. This document describes the API at a high level. If you want to see details you can look at the source code in the files at [ruby-gem/lib/calabash-android](../ruby-gem/lib/calabash-android/). There are functions in the source code which aren't documented here. Those are way more likely to change (so be warned if you rely on those).

Calabash Android has a client-server architecture. The Calabash Ruby API is the client side which speaks HTTP with the test server that running on the device along with your app. To get an architectural overview of Calabash Android please read the blog posts:

[AN OVERVIEW OF CALABASH ANDROID](http://blog.lesspainful.com/2012/03/07/Calabash-Android/)

# General
### `start_test_server_in_background`
Starts the test server and the app under test (AUT). If the app is already running it will be restarted.

### `reinstall_apps`
Will reinstall both the test server and the AUT to be sure the newest versions are installed.


# Query
### `query(uiquery, *args)`
Query returns an [array](http://www.ruby-doc.org/core-1.9.3/Array.html) of its results. The query function gives powerful query capability from your test code. You can find views and other application objects, and make assertions about them or extract data from them.

Calabash Android tries to return results that carry useable information by default. For view objects this includes coordinates, class and contentdescription:

    irb(main):002:0> query("button index:1")
    => [{"id"=>"save", "enabled"=>true, "contentDescription"=>nil, "class"=>"android.widget.Button", "text"=>"Save", "rect"=>{"center_y"=>724.0, "center_x"=>645.5, "height"=>64, "y"=>692, "width"=>71, "x"=>610}, "description"=>"android.widget.Button{4267b4a0 VFED..C. ........ 497,243-568,307 #7f070023 app:id/save}"}]

A view is represented as a ruby [Hash](http://www.ruby-doc.org/core-1.9.3/Hash.html) (hash map) so you can look into the result

    irb(main):003:0> query("button index:1").first.keys
    => ["id", "enabled", "contentDescription", "class", "text", "rect", "description"]

The `*args` parameter lets you perform methods on the query result *before* it is returned to your Ruby script code (remember that the query is evaluated as Java code inside the app and the result is sent back to the Ruby code). The form `*args` is Ruby-speak for a variable number of args. For example, if you have a button you can do

    irb(main):005:0> query("button", "text")
    => ["Optional Settings", "Save", "Cancel", "Get a free blog at WordPress.com"]

This calls a 'getter' method "text" (that is text(), getText() or isText()) on each of the buttons in the view (it always returns an array). You can perform a sequence of methods:

    irb(main):007:0> query("button", "text", "length")
    => [17, 4, 6, 32]

    irb(main):008:0> query("button", "text", "toLowerCase")
    => ["optional settings", "save", "cancel", "get a free blog at wordpress.com"]


For methods with arguments you can use hashes. In Ruby 1.9 this has quite nice syntax:

    irb(main):033:0> query("edittext index:1", setText:"1234")
    => ["<VOID>"]

On Ruby 1.8 you can't use key:val as literal Hash syntax so you must do:

    irb(main):034:0> query("edittext index:1", :setText => "1234")
    => ["<VOID>"]

Behind the scenes the Java method `setText` will be execute with the argument `"12345"` on all view elements that were matched by the query.

Notice that the string `<VOID>` is Calabash's way of returning from a Java method with return type `void`.

For more complex methods you use Arrays of Hashes. Here is a complex Ruby 1.9 example:

    TODO: Example


### `element_does_not_exist(uiquery)`
### `element_exists(uiquery)`
### `view_with_mark_exists(expected_mark)`
The `element_exists` function returns true if an element exists matching query `uiquery`.
The `element_does_not_exist` function returns true if an element matching query `uiquery` does not exist.

The function `view_with_mark_exists(expected_mark)` is shorthand for

    element_exists("* marked:'#{expected_mark}'")

# Waiting
### `wait_for(options, &block)`

Waits for a condition to occur. Takes a hash of options and a block to be called repeatedly. The options (which are described below) have the following defaults:


    {
     :timeout => 10, #maximum number of seconds to wait
     :retry_frequency => 0.2, #wait this long before retrying the block
     :post_timeout => 0.1, #wait this long after the block returns true
     :timeout_message => "Timed out waiting...", #error message in case options[:timeout] is exceeded
     :screenshot_on_error => true # take a screenshot in case of error
    }


The `timeout` argument should be a number indicating the maximal number of seconds you are willing to wait (after that amount of time the step will cause your test to fail). The `:post_timeout` (0.1 by default) is an number of seconds to wait *after the condition becomes true*.

The `&block` parameter is Ruby syntax for saying that this method takes a block of code. This block specifies the condition to wait for. The block should return `true` when the the condition occurs.

The `:retry_frequency` is a small sleep that is made between each call to the specified block. This describes how often Calabash should poll for the condition to be true.


Here is a simple example:

    irb(main):030:0> wait_for(:timeout => 5) { query("button marked:'Save'").size > 0 }

This will check for the existence of a view matching: "button marked:'Save'". It will wait *at most* 5 seconds (failing if more than 5 seconds pass). It will issue the query repeatedly until it is found or 5 seconds pass.

A typical form uses `element_exists`.

    irb(main):031:0> wait_for(:timeout => 5) { element_exists("button marked:'Save'") }

In Ruby short blocks are written with braces (like: `{ element_exists("button marked:'Save'") }`), and more complicated blocks are written using `do`-`end`. For example:

    wait_for(:timeout => 30) do
        res = query("checkbox marked:'Geotag Posts'", 'checked')
        res.first == true
    end

A Ruby block always returns the value of its last expression (`res.first == true` in this case).

*Notes:* Waiting for a condition to occur is superior to using the `sleep` function. With `sleep` you end up either specifying too long waits which slows the test down or you become sensitive to timing issues. Sometimes you do need sleep (to wait for animations to complete), but try to use waiting as much as possible.

### wait_for_element_exists(uiquery, options={})

A high-level waiting function. This captures the common practice of waiting for UI elements, i.e., combining `wait_for` and `element_exists`.

Takes a query and waits for it to return a results. Calls `wait_for` supplying `options`.


    irb(main):009:0> wait_for_elements_exist( "* marked:'Please sign in'", :timeout => 10)


### wait_for_elements_exist(elements_arr, options={})

Like `wait_for_element_exists` but takes an *array* of queries and waits for all of those queries to return results. Calls `wait_for` supplying `options`.


    irb(main):008:0> wait_for_elements_exist( ["button marked:'Save'", "* marked:'Please sign in'"], :timeout => 2)

### wait_for_element_does_not_exist(uiquery, options={})

Similar to `wait_for_element_exists`, but waits for an element to not exist.


### wait_for_elements_do_not_exist(elements_arr, options={})

Similar to `wait_for_elements_exist`, but waits for all of the elements to not exist.


# Assertions
#### `fail(msg="Error. Check log for details.")`
Will fail the test with message `msg`. Takes a screenshot.

### `check_element_exists(query)`
### `check_element_does_not_exist(query)`
### `check_view_with_mark_exists(expected_mark)`
Asserts that an element exists using the query function on the parameter `query`.

The function `check_view_with_mark_exists(expected_mark)` is shorthand for

    check_element_exists("view marked:'#{expected_mark}'")

# Touch
### `touch(uiquery, options={})`

Touches a view found by performing the query `uiquery`. It is recommended that `uiquery` only produce one match, but the default is to just touch the first of the results if there are several.

The `touch` method is one of the most used in Calabash. It is mostly used in its simplest form:

    irb(main):037:0> touch("* marked:'Save'")

Which uses content descriptions, ids or texts. This form is so common that there is a short-hand for it: `tap`:

    irb(main):038:0> tap 'Save'

For flexibility you can also pass in a hash representation of a view and the the touch event will be calculated based on those values and no query will be executed. `touch` will also accept a list of hashes in which case Calabash will touch the first one view in the list.

The following are all equivalent

    touch("button index:0")
    touch("button")
    touch(query("button index:0"))
    touch(query("button").first)
    touch(query("button"))
    
# Entering text
### `keyboard_enter_text(text, options={})`

Enters **text** into the currently focused view.

### `enter_text(uiquery, text, options={})`

Taps the first element returned by **uiquery**, then enters **text** into the view.

# Screenshot
### `screenshot(options={:prefix=>nil, :name=>nil})`
Takes a screenshot of the app.

    screenshot({:prefix => "/tmp", :name=>"my.png"})

If prefix and name are nil it will use default values (which is currently the line in the current feature).

### `screenshot_embed(options={:prefix=>nil, :name=>nil, :label => nil})`
Takes a screenshot of the app and embeds to cucumber reporters (e.g. html reports).

    screenshot_embed({:prefix => "/tmp", :name=>"my.png", :label => "Mine"})

If prefix and name are nil it will use default values (which is currently the line in the current feature).

Label is the label used in the cucumber report output (equals to name if not specified).

# Pull and push files and folders from and to the device
### `pull(remote, local)`
Pulls a file from the device to local computer:

    pull("/sdcard/file.jpg", "file.jpg")

### `push(local, remote)`
Pushes a file from the local computer to the device:

    push("file.jpg", "/sdcard/file.jpg")

Uses [adb](http://developer.android.com/tools/help/adb.html) so same rules apply: 

* Won't be able to pull or push from restricted folders such as /data/data
* If destination path already exists, it's overwritten without warning
* For files, full destination path must be provided, ie:

Won't work:
```
push("file.jpg", "/sdcard/folder")
```

Will work:
```
push("file.jpg", "/sdcard/folder/file.jpg")
```

# Read, write and clear SharedPreferences
Simple API over [SharedPreferences](http://developer.android.com/guide/topics/data/data-storage.html#pref), all
methods require the name of the SharedPreferences file as the first argument. Supports ints, floats, booleans and strings.

It is important to notice that depending on your application you might need to poke around with SharedPreferences
before or after your application or activity starts. In that case you will need to call these methods either
before or after your scenario.

To do so, you can tag a particular scenario and edit your application lifecycle hooks as explained [here](https://groups.google.com/forum/?fromgroups=#!topic/calabash-android/Ql3iluRMijg).

### `get_preferences(name)`
Returns a hash with the preferences available for the given name:

    preferences = get_preferences("my_preferences")

### `set_preferences(name, hash)`
Sets the given hash as preferences for the given name:

    set_preferences("my_preferences", {:name => "wadus", :email => "wadus@wadus.com", :id => 8, :active => true})

### `clear_preferences(name)`
Clears the preferences for the given name:

    clear_preferences("my_preferences")
