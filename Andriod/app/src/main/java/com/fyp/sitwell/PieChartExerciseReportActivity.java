package com.fyp.sitwell;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;

public class PieChartExerciseReportActivity extends AppCompatActivity {


    private DBHandler dbHandler;
    private static Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_exercise_report);
        dbHandler = new DBHandler(this);

    }

}