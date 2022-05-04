package com.fyp.sitwell.habitCorrection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.sitwell.R;

public class HabitCorrectionSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_correction_setting);
        EditText text = findViewById(R.id.edit_text_habit);
        Button button = findViewById(R.id.habit_setting_btn);
        TextView textView = findViewById(R.id.show_alert_time);
        String s = "The app will alert you after incorrect sitting " + HabitLocalStorage.getId(this) + " times";
        textView.setText(s);


        button.setOnClickListener(v -> {
            int times = 1;
            try {
                times = Integer.parseInt(text.getText().toString());
            }catch (Exception e){
                Toast.makeText(this, "Please input a integer number", Toast.LENGTH_LONG).show();
                return;
            }
            if(times>9){
                Toast.makeText(this, "Please input a number in 1-9", Toast.LENGTH_LONG).show();
                return;
            }

            HabitLocalStorage.setId(this, times);
            String ss = "The app will alert you after incorrect sitting " + HabitLocalStorage.getId(this) + " times";
            textView.setText(ss);
        });
    }
}
