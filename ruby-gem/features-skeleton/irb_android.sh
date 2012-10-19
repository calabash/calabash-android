#!/bin/bash
TEST_SERVER_PORT=34777 
PACKAGE_NAME=$1
MAIN_ACTIVITY=$2
if [ -z "$PACKAGE_NAME" ]; then
   echo 'The parameter about your mobile app package is missing: e.g. com.springsource.greenhouse.test'
   exit 1
fi

if [ -z "$MAIN_ACTIVITY" ]; then
   echo 'The parameter about your mobile app main activity is missing: e.g. com.springsource.greenhouse.MainActivity'
   exit 1
fi

   # use this line for calabash version 0.1
   # adb shell am instrument -e class sh.calaba.instrumentationbackend.InstrumentationBackend -w $PACKAGE_NAME/android.test.InstrumentationTestRunner &
   # use this for calabash-version 0.2
   # adb shell am instrument -e class sh.calaba.instrumentationbackend.InstrumentationBackend -w $PACKAGE_NAME/sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner &
   #use this for calabash version 0.3
   #adb shell am instrument -e target_package $PACKAGE_NAME -e main_activity $MAIN_ACTIVITY -e class sh.calaba.instrumentationbackend.InstrumentationBackend sh.calaba.android.test/sh.calaba.instrumentationbackend.CalabashInstrumentationTestRunner
   #sleep 7
   IRBRC=.irbrc TEST_SERVER_PORT=$TEST_SERVER_PORT PACKAGE_NAME=$PACKAGE_NAME MAIN_ACTIVITY=$MAIN_ACTIVITY irb
