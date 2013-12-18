
WAIT_TIMEOUT = (ENV['WAIT_TIMEOUT'] || 30).to_f
STEP_PAUSE = (ENV['STEP_PAUSE'] || 0.5).to_f

require 'calabash-android/steps/additions_manual_steps'
require 'calabash-android/steps/app_steps'
require 'calabash-android/steps/assert_steps'
require 'calabash-android/steps/check_box_steps'
require 'calabash-android/steps/context_menu_steps'
require 'calabash-android/steps/date_picker_steps'
require 'calabash-android/steps/enter_text_steps'
require 'calabash-android/steps/location_steps'
require 'calabash-android/steps/navigation_steps'
require 'calabash-android/steps/press_button_steps'
require 'calabash-android/steps/progress_steps'
require 'calabash-android/steps/rotation_steps'
require 'calabash-android/steps/screenshot_steps'
require 'calabash-android/steps/search_steps'
require 'calabash-android/steps/spinner_steps'
require 'calabash-android/steps/time_picker_steps'
require 'calabash-android/steps/list_steps'

