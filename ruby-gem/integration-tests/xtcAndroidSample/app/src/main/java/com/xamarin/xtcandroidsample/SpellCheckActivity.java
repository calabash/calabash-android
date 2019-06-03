package com.xamarin.xtcandroidsample;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SpellCheckActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spell_check_sample);
        EditText textViewSpellCheckedInput = (EditText)this.findViewById(R.id.editTextSpellChecked);
        textViewSpellCheckedInput.setText(Settings.spellCheckedInput);
        textViewSpellCheckedInput.setSelection(textViewSpellCheckedInput.getText().length());
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
                EditText editText = (EditText)this.findViewById(R.id.editTextSpellChecked);
                Settings.spellCheckedInput = editText.getText().toString();
                finish();
                break;
        }
    }
}
