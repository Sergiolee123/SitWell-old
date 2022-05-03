package com.fyp.sitwell.muscleTrainingRecommendation;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.fyp.sitwell.R;
import com.fyp.sitwell.report.DBHandler;

public class MuscleTrainingRecommendationActivity extends AppCompatActivity {

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_training_recommendation);
        dbHandler = new DBHandler(this);
        Cursor sitRecsCursor = dbHandler.getSelectedQuerySitRecs();
        Cursor exRecCursor = dbHandler.getRecentUserExerciseData();
        //exRecCursor.getInt(2) = strengthExerciseCount, exRecCursor.getInt(3) = relaxExerciseCount

        if(exRecCursor.getCount()!=0){
            exRecCursor.moveToNext();
            Log.e("print exRecCurso", exRecCursor.getInt(0)+","+exRecCursor.getString(1)+","+exRecCursor.getInt(2)+","+exRecCursor.getInt(3)+","+exRecCursor.getInt(4));
            //2:neck 3:back 4:shoulder 5:leftArm 6:rightArm 7:sitWell 8:Sitbad
        }

        if(sitRecsCursor.getCount()!=0){
            while( sitRecsCursor.moveToNext())
                Log.e("print sitRecsCursorr", sitRecsCursor.getInt(2)+","+sitRecsCursor.getInt(3)+","+sitRecsCursor.getInt(4)+","+sitRecsCursor.getInt(5)+","+sitRecsCursor.getInt(6)+","+sitRecsCursor.getInt(7)+","+sitRecsCursor.getInt(8));
        }



    }
}