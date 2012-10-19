require 'rubygems'
require 'irb/completion'
require 'irb/ext/save-history'

ARGV.concat [ "--readline",
              "--prompt-mode",
              "simple" ]

# 25 entries in the list
IRB.conf[:SAVE_HISTORY] = 50

# Store results in home directory with specified file name
IRB.conf[:HISTORY_FILE] = ".irb-history"

require 'calabash-android/operations'
include Calabash::Android::Operations

start_test_server_in_background
