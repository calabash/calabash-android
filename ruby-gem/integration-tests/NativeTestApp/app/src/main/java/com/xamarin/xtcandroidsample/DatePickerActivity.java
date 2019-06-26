package com.xamarin.xtcandroidsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DatePickerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_sample);
        DatePicker datePicker = (DatePicker)this.findViewById(R.id.datePicker);
        datePicker.init(Settings.pickedDate.getYear() + 1900, Settings.pickedDate.getMonth(), Settings.pickedDate.getDay(), null);

        TimePicker timePicker = (TimePicker)this.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(Settings.pickedDate.getHours());
        timePicker.setCurrentMinute(Settings.pickedDate.getMinutes());
    }

    /* View action functions */
    public void onButtonClick(View view) {
        assert view.getClass().isInstance(Button.class);

        Button button = (Button)view;

        switch(button.getId()) {
            case R.id.buttonCancel:
                finish();
                break;

            case R.id.buttonSave:
                DatePicker datePicker = (DatePicker)this.findViewById(R.id.datePicker);
                TimePicker timePicker = (TimePicker)this.findViewById(R.id.timePicker);
                Settings.pickedDate.setYear(datePicker.getYear() - 1900);
                Settings.pickedDate.setMonth(datePicker.getMonth());
                Settings.pickedDate.setDate(datePicker.getDayOfMonth());
                Settings.pickedDate.setHours(timePicker.getCurrentHour());
                Settings.pickedDate.setMinutes(timePicker.getCurrentMinute());
                Settings.pickedDate.setSeconds(0);
                finish();
                break;
        }
    }
}
