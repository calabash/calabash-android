package com.xamarin.xtcandroidsample;

import android.app.Application;

import java.util.Date;


public class Settings extends Application {
    public static Date pickedDate;
    public static String spellCheckedInput;
    public static String radioSelectedValue;
    public static boolean checkBoxChecked;
    public static int seekBarProgress;

    @Override
    public void onCreate() {
        Settings.pickedDate = new Date();
        Settings.spellCheckedInput = "";
        Settings.radioSelectedValue = "";
        Settings.checkBoxChecked = false;
        Settings.seekBarProgress = 0;

        super.onCreate();
    }
}