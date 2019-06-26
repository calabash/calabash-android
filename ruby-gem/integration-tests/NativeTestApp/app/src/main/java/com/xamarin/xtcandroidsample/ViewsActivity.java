package com.xamarin.xtcandroidsample;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.views_sample);

        RadioButton radioButton = null;

        RadioGroup radioGroup = (RadioGroup)this.findViewById(R.id.radioGroup);
        int children = radioGroup.getChildCount();

        for (int i = 0; i < children; i++)
        {
            RadioButton child = (RadioButton)radioGroup.getChildAt(i);

            if (Settings.radioSelectedValue.equals(child.getText().toString()))
            {
                radioButton = child;
                break;
            }
        }

        if (radioButton != null) radioButton.setChecked(true);

        CheckBox checkBox = (CheckBox)this.findViewById(R.id.checkBox);
        checkBox.setChecked(Settings.checkBoxChecked);

        SeekBar seekBar = (SeekBar)this.findViewById(R.id.seekBar);
        seekBar.setProgress(Settings.seekBarProgress);

        Spinner spinner = (Spinner)this.findViewById(R.id.spinner);
        ListView listView = (ListView)this.findViewById(R.id.listView);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.coffee_names, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        ArrayAdapter<CharSequence> listViewAdapter = ArrayAdapter.createFromResource(this, R.array.fruits, android.R.layout.simple_list_item_1);
        listView.setAdapter(listViewAdapter);

        EditText editText = (EditText) findViewById(R.id.editTextSecond);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Send key pressed!", Toast.LENGTH_SHORT);
                    toast.show();

                    handled = true;
                }

                return handled;
            }
        });
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
                RadioGroup radioGroup = (RadioGroup)this.findViewById(R.id.radioGroup);
                int id = radioGroup.getCheckedRadioButtonId();

                if (id != -1)
                {
                    RadioButton radioButton = (RadioButton)this.findViewById(id);
                    Settings.radioSelectedValue = radioButton.getText().toString();
                }

                CheckBox checkBox = (CheckBox)this.findViewById(R.id.checkBox);
                Settings.checkBoxChecked = checkBox.isChecked();

                SeekBar seekBar = (SeekBar)this.findViewById(R.id.seekBar);
                Settings.seekBarProgress = seekBar.getProgress();

                finish();
                break;
        }
    }
}
