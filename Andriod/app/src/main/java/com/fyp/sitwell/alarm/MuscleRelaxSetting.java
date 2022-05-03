package com.fyp.sitwell.alarm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.sitwell.R;

import java.util.Date;

public class MuscleRelaxSetting extends AppCompatActivity {

    Button button;
    CheckBox oneHour, twoHour, threeHour;
    TimePicker startTime, endTime;
    TextView errorText;
    int time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = 0;
        setContentView(R.layout.activity_muscle_relax_setting);
        errorText = findViewById(R.id.error_text_relex);
        button = findViewById(R.id.relax_setting_btn);
        oneHour = findViewById(R.id.one_hour);
        twoHour = findViewById(R.id.two_hour);
        threeHour = findViewById(R.id.three_hour);
        startTime = findViewById(R.id.relax_timePicker_start);
        endTime = findViewById(R.id.relax_timePicker_end);

        button.setOnClickListener(v -> {

            if(!isOneOfTheButtonChecked()){
                errorText.setText("You should select at least one checkbox");
                return;
            }

            if(isMoreThenOneCheckBoxChecked()){
                errorText.setText("You should select only one checkbox");
                return;
            }

            if(oneHour.isChecked()){
                time = 1;
            }else if(twoHour.isChecked()){
                time = 2;
            }else{
                time = 3;
            }

            int settingPeriodTime = endTime.getHour() - startTime.getHour();
            if(settingPeriodTime < 1){
                errorText.setText("Start time should early then end time");
                return;
            }
            MuscleRelaxAlarm.cancelAlarm(this);
            Date date = new Date();
            date.setHours(startTime.getHour());
            date.setMinutes(endTime.getMinute());
            Date date1 = new Date(date.getTime());
            for(int i = 1; i < settingPeriodTime+1; i += time){
                if(date.getHours()+i>23){
                    date1.setHours(date.getHours()+i-24);
                    MuscleRelaxAlarm.setAlarm(this, date1.getTime());
                }else{
                    date1.setHours(date.getHours()+i);
                    MuscleRelaxAlarm.setAlarm(this, date1.getTime());
                }

            }

        });

    }

    private boolean isOneOfTheButtonChecked(){
        return oneHour.isChecked() || twoHour.isChecked() || threeHour.isChecked();
    }

    private boolean isMoreThenOneCheckBoxChecked(){
        int count = 0;
        if(oneHour.isChecked()){
            count++;
        }
        if(twoHour.isChecked()){
            count++;
        }
        if(threeHour.isChecked()){
            count++;
        }

        return count>1;
    }
}
