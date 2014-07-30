Migrating to Calabash-android 0.5
=================================
Calabash 0.5 introduced new features and removed a lot of actions that can be substituted by the query method.
# New & changed features

## Entering text

Any text entry is now using the keyboard instead of setting the text directly. This might effect some tests as the keyboard pops up and you might need to dismiss it. This can be done using `hide_soft_keyboard`.

In addition the new `keyboard_enter_text` and `enter_text` methods have full unicode support.

## New ruby wrappers

### Text
####keyboard_enter_text(text, options={})
Enters **text** into the currently focused view.

####keyboard_enter_char(character, options={})
Enters the first (or only) character of **character** into the currently focused view.

####enter_text(uiquery, text, options={})
Taps an element found using **uiquery** when it exists and enters **text**.

Equivalent to:

```
tap_when_element_exists(uiquery, options)
sleep 0.5
keyboard_enter_text(text, options)
```

####clear_text(query_string, options={})
Clears the text of the view(s) found using `query(query_string)`

### Menus

####press_menu_button
Presses the menu button (works on all devices, whether or not they have a physical menu button).

####select_options_menu_item(identifier, options={})
Presses the menu button and selects a menu element marked by the given identifier.

####select_context_menu_item(view_uiquery, menu_item_query_string)
Long presses a view found using **view_uiquery** and taps a menu item found using **menu_item_query_string**.

**Example:** `select_context_menu_item("TextView text:'Menu'", "* text:'Item 1'")`

### Gestures

####press_back_button
Presses the back button.

####tap_when_element_exists(uiquery, options={})
Waits for an element and taps it when it appears on screen. It raises an exception if the element doesn't appear within the timeout.

####long_press_when_element_exists(uiquery, options={})
Waits for an element and long presses it when it appears on screen. It raises an exception if the element doesn't appear within the timeout.

####scroll(query_string, direction)
Scrolls halfway across the view found by **query_string**.

**Example:** `scroll("android.widget.ScrollView id:'scroller'", :down)`

####scroll_down and scroll_up
Scrolls the first instance of 'android.widget.ScrollView' either downwards or upwards.

####scroll_to(query_string, options={})
Scrolls the first parent with the class 'android.widget.ScrollView' of the query element found using `query(query_string)` until the element is visible on the screen. It will scroll either upwards or downwards depending on the location of the element.

**Example** `scroll_to("TextView id:'my view'")`

As all options are passed on to the when_element_exists method, it is possible to decide what to do when the view is eventually found.

**Example** `scroll_to("Button index:30", action: lambda {|v| touch(v)})`

This example is identical to `tap_when_element_exists("button index:1", scroll: true)`.

### Waiting

####wait_for_text(text, options={})
Waits for the specified text to be visible on the screen.

####wait_for_text_to_disappear(text, options={})
Waits for the specified text to disappear from the screen.

####wait_for_activity(activity_name, options={})
Waits for the current activity to have to name **activity_name**.

### Assertions

####has_text?(text)
Returns true if the specified text is visible on the screen.

####assert_text(text, should_find = true)
**If should_find is true**: Raises an exception if the specified text is NOT visible on the screen.
**If should_find is false**: Raises an exception if the specified text IS visible on the screen.


## View visibility

Fixed issues with visibility filtering. `query` now returns only the visible views - both in android views and webview elements.

This might break code if you are relying on elements not visible on the screen. You can still use the `all` filter. For example `query("all webview css:'*'")`.



# Removed actions

#### assert_no_duplicates_in_grid

This action is removed as it's not generic enough to be included in the core framework.

#### assert_text

Before:

```
performAction('assert_text', 'foo', true)
performAction('assert_text', 'foo', false)
```

After:

```
assert_text('foo')
assert_text('foo', false)
```

`assert_text('foo')` raises an exception if `foo` **is not** found.

`assert_text('foo', false)` raises an exception if `foo` **is** found.

The behaviour of `assert_text` also changed slightly:

* it is case insensitive
* it finds the text even if it's partially matching (using the CONTAINS predicate)
* it tries to find the text in all views not just `TextViews`

Also added:

```
has_text?('foo')
```

`has_text?` returns true or false.


#### assert_text_in_textview

Before:

```
performAction('assert_text_in_textview', 'foo', 'my description')
```

After:

```
raise 'Text not found' if query("* marked:'my description' {text CONTAINS[c] 'foo'}").empty?
```

#### clear_..._field

Removed three actions:
###### clear_named_field
###### clear_numbered_field
###### clear_id_field

Before:

```
perform_action('clear_named_field', 'name')
perform_action('clear_numbered_field', 1)
perform_action('clear_id_field', 'my_id')
```

After:

```
clear_text("android.widget.EditText marked:'name'")
clear_text("android.widget.EditText index:0")
clear_text("android.widget.EditText id:'my_id'")
```

Note:
* the new method raises an exception if the view can't be found
* the `clear_text` method clears **all** the views that it founds not just the first one

#### click_by_selector

Before:

```
performAction('click_by_selector', 'css_selector')
```

After:

```
touch("webview css:'css_selector'")
```


#### click_on_text

Before:

```
performAction("click_on_text", 'my_text')
```

After:

```
touch("* {text CONTAINS 'my_text'}")
```


#### click_on_view_by_description

Before:

```
performAction("click_on_view_by_description", "my description")
```

After:

```
tap_when_element_exists("* contentDescription:'my description'")
```


#### click_on_view_by_id

Before:

```
performAction('click_on_view_by_id', 'identifier')
```

After:

```
tap_when_element_exists("* marked:'identifier'")
```


#### enter_query_into_numbered_field


Before:

```
performAction('enter_query_into_numbered_field', 'foo', 1)
```

After:

```
enter_text("android.widget.SearchView index:0", 'foo')
```


#### enter_text_...

Removed three actions:
###### enter_text_into_numbered_field
###### enter_text_into_id_field
###### enter_text_into_named_field

Before:

```
  perform_action('enter_text_into_named_field', 'foo', 'name')
  perform_action('enter_text_into_numbered_field', 'foo', 1)
  perform_action('enter_text_into_id_field', 'foo', 'my_id')
```

After:

```
  enter_text("android.widget.EditText marked:'name'", 'foo')
  enter_text("android.widget.EditText index:0", 'foo')
  enter_text("EditText id:'my_id'", 'foo')
```


#### get_list_...

Removed three actions:
###### get_list_data
###### get_list_item_properties
###### get_list_item_text

Use queries instead.


#### get_selected_spinner_item_text

Before:

```
performAction('get_selected_spinner_item_text', 'my_id')
```

After:

```
query("android.widget.Spinner id:'my_id'", :getSelectedItem).first
```


#### get_text_by_id

Before:

```
performAction("get_text_by_id", 'my_id')
```

After:

```
query("* id:'my_id'", :text).first
```


#### get_view_property

Before:

```
performAction('get_view_property', 'identifier', 'property')
```

After:

```
query("* marked:'identifier'", :property)
```


#### has_view

Before:

```
performAction('has_view', 'identifier')
```

After:

```
check_element_exists("* marked:'identifier'")
```


#### inspect_current_dialog

Before:
```
performAction('inspect_current_dialog')
```

After:
```
query("*")
```


#### is_enabled

Before:

```
performAction('is_enabled', 'identifier')
```

After:

```
raise 'View is disabled' unless query("* marked:'identifier' index:0", :isEnabled).first
```


#### long_press_list_item

**Removed**. Use long_press(query) instead.


#### long_press_on_view_by_id

Before:

```
performAction("long_press_on_view_by_id", "my_id")
```

After:

```
long_press("* id:'my_id'")
```


#### press

Before:

```
performAction('press', 'my_id')
```

After:

```
touch("* marked:'my_id'")
```


#### press_button_number

Before:

```
performAction('press_button_number', 3)
```

After:

```
tap_when_element_exists("android.widget.Button index:2")
```


#### press_button_with_text

Before:

```
performAction('press_button_with_text', 'my_text')
```

After:

```
tap_when_element_exists("android.widget.Button {text CONTAINS 'my_text'}")
```


#### press_image_button_description

Before:

```
performAction('press_image_button_description', 'identifier')
```

After:

```
tap_when_element_exists("android.widget.ImageButton marked:'identifier'")
```


#### press_image_button_number

Before:

```
performAction('press_image_button_number', 2)
```

After:

```
tap_when_element_exists("android.widget.ImageButton index:1")
```


#### press_list_item

**Removed**. Use touch(query) instead.


#### press_long_on_text


Before:

```
performAction("press_long_on_text", 'foo')
```

After:

```
long_press("* marked:'foo'")
```

#### press_long_on_text_and_select_with_...

Removed three actions:

###### press_long_on_text_and_select_with_id
###### press_long_on_text_and_select_with_text
###### press_long_on_text_and_select_with_index


Before:

```
performAction("press_long_on_text_and_select_with_id", 'MyButton', 'menu_id')
performAction("press_long_on_text_and_select_with_text", 'MyButton', 'Option 1')
performAction("press_long_on_text_and_select_with_index", 'MyButton', 1)
```

After:

```
select_context_menu_item("* {text CONTAINS[c] 'MyButton'}", "android.widget.TextView id:'menu_id'")
select_context_menu_item("* {text CONTAINS[c] 'MyButton'}", "android.widget.TextView marked:'Option 1'")
select_context_menu_item("* {text CONTAINS[c] 'MyButton'}", "android.widget.TextView index:0")
```


#### scroll_down

Before:

```
performAction('scroll_down')
```

After:

```
scroll_down
```


#### scroll_up

Before:

```
performAction('scroll_up')
```

After:

```
scroll_up
```


#### select_from_menu
Before:

```
  perform_action('select_from_menu', 'Settings')
```

After:

```
  select_options_menu_item('Settings')
```

Note: `select_options_menu_item` can take either an id, content description or text.


#### select_item_from_named_spinner


Before:

```
performAction("select_item_from_named_spinner", 'my_spinner_id', 'Option 1')
```

After:

```
query("android.widget.Spinner id:'my_spinner_id'", :getSelectedItem).first
touch_when_element_exists("* text:'Option 1'")
```


#### select_tab
Before:

```
perform_action('select_tab', 1)
perform_action('select_tab', 'tab3')
```

After:

```
  touch("android.widget.TabWidget descendant TextView index:0")
  touch("android.widget.TabWidget descendant TextView {text LIKE[c] 'tab3'}")
```

#### set_date_with_description
Before:

```
performAction('set_date_with_description', '16-02-2012', 'identifier')
```

After:

```
set_date("android.widget.DatePicker marked:'identifier'", 2012, 2, 16)
```
or (not recommended):

```
set_date("android.widget.DatePicker marked:'identifier'", '16-02-2012')
set_date("android.widget.DatePicker marked:'identifier'", '2012-02-16')
```

#### set_date_with_index
Before:

```
performAction('set_date_with_index', '16-02-2012', 1)
```

After:

```
set_date("android.widget.DatePicker index:0", 2012, 2, 16)
```
or (not recommended):

```
set_date("android.widget.DatePicker index:0",, '16-02-2012')
set_date("android.widget.DatePicker index:0",, '2012-02-16')
```

#### set_time_with_description
Before:

```
performAction('set_time_with_description', '16:42', 'identifier')
```

After:

```
set_time("android.widget.TimePicker marked:'identifier'", 16, 42)
```
or (not recommended):

```
set_time("android.widget.TimePicker marked:'identifier'", '16:42')
```

#### set_time_with_index
Before:

```
performAction('set_time_with_index', '16:42', 1)
```

After:

```
set_time("android.widget.TimePicker index:0", 16, 42)
```
or (not recommended):

```
set_time("android.widget.TimePicker index:0", '16:42')
```


#### set_text

Before:

```
set_text("WebView css:'input[type=\"text\"]'", "Hello World")
```

After

```
enter_text("WebView css:'input[type=\"text\"]'", "Hello World")
```



#### toggle_numbered_checkbox

Before:

```
performAction('toggle_numbered_checkbox', 1)
```

After:

```
tap_when_element_exists("android.widget.CheckBox index:0")
```


#### wait

Before:

```
performAction('wait', 5)
```

After:

```
sleep(5)
```

Note: In general you should avoid using sleeps in your test scripts


#### wait_for_button

Before:

```
performAction('wait_for_button', 'identifier')
```

After:

```
wait_for_element_exists("android.widget.Button marked:'identifier'")
```


#### wait_for_dialog_to_close

**Removed**. Wait for traits to appear/disappear instead.


#### wait_for_no_progress_bars

Before:

```
performAction('wait_for_no_progress_bars')
```

After:

```
wait_for_element_does_not_exist("android.widget.ProgressBar")
```


#### wait_for_screen

The `wait_for_screen` action is removed. You can use the `wait_for_activity` ruby method instead.

Before:

```
perform_action('wait_for_screen', 'MainActivity')
perform_action('wait_for_screen', 'MainActivity', 5)
```

After:

```
wait_for_activity('MainActivity')
wait_for_activity('MainActivity', timeout: 5)
```


#### wait_for_tab

Use query and wait for instead.
Not working correctly anyway - it's only working for TabActivities
new implementation - still for tab activities

Before:

```
performAction(wait_for_tab, "tab3")
performAction(wait_for_tab, "tab3", 5)
```

After:

```
  wait_for do
    query("android.widget.TabWidget descendant TextView {text LIKE[c] 'tab3'}", :isSelected).first
  end

  wait_for(timeout: 5) do
    query("android.widget.TabWidget descendant TextView {text LIKE[c] 'tab3'}", :isSelected).first
  end
```


#### wait_for_text

Before:

```
  performAction('wait_for_text', 'foo')
  performAction('wait_for_text', 'foo', 5)
```

After:

```
  wait_for_text('foo')
  wait_for_text('foo', timeout: 5)
```


#### wait_for_view

Before:

```
performAction('wait_for_view', 'identifier')
```

After:

```
wait_for_element_exists("* marked:'identifier'")
```


#### wait_for_view_by_id

Before:

```
performAction('wait_for_view_by_id', 'identifier')
```

After:

```
wait_for_element_exists("* marked:'identifier'")
```
