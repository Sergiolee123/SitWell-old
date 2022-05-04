package com.fyp.sitwell.muscleTrainingRecommendation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.fyp.sitwell.R;
import com.fyp.sitwell.report.DBHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MuscleTrainingRecommendationActivity extends AppCompatActivity {

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_training_recommendation);
        TextView textView = findViewById(R.id.recommendation);

        dbHandler = new DBHandler(this);
        Cursor sitRecsCursor = dbHandler.getSelectedQuerySitRecs();
        Cursor exRecCursor = dbHandler.getRecentUserExerciseData();

        int leftArm=0, rightArm=0, neck=0, back=0, shoulder=0, sitwell=0, sitbad=0;
        double strength=0, relax=0;

        if(exRecCursor.getCount()!=0){
            //exRecCursor.getInt(2) = strengthExerciseCount, exRecCursor.getInt(3) = relaxExerciseCount
            exRecCursor.moveToNext();
            strength=exRecCursor.getInt(2);
            relax=exRecCursor.getInt(3);
            Log.e("print exRecCurso", exRecCursor.getInt(0)+","+exRecCursor.getString(1)+","+exRecCursor.getInt(2)+","+exRecCursor.getInt(3)+","+exRecCursor.getInt(4));
        }

        if(sitRecsCursor.getCount()!=0){
            while( sitRecsCursor.moveToNext()){
                //2:neck 3:back 4:shoulder 5:leftArm 6:rightArm 7:sitWell 8:Sitbad
                neck+=sitRecsCursor.getInt(2);
                back+=sitRecsCursor.getInt(3);
                shoulder+=sitRecsCursor.getInt(4);
                leftArm+=sitRecsCursor.getInt(5);
                rightArm+=sitRecsCursor.getInt(6);
                sitwell+=sitRecsCursor.getInt(7);
                sitbad+=sitRecsCursor.getInt(8);
                Log.e("print sitRecsCursorr", sitRecsCursor.getInt(2)+","+sitRecsCursor.getInt(3)+","+sitRecsCursor.getInt(4)+","+sitRecsCursor.getInt(5)+","+sitRecsCursor.getInt(6)+","+sitRecsCursor.getInt(7)+","+sitRecsCursor.getInt(8));
                }
            }

        SharedPreferences pref = getSharedPreferences("PrefsFile", MODE_PRIVATE);
        String pref_height = pref.getString("height","");
        String pref_weight = pref.getString("weight","");
        String pref_age = pref.getString("age","");
        double height = Double.parseDouble(pref_height.charAt(0) + "." + pref_height.substring(1));
        double weight = Double.parseDouble(pref_weight);

        StringBuilder suggest = new StringBuilder();

        if(sitwell>sitbad){
            suggest.append("You are doing well! Keep going\n\n");
        }else{
            suggest.append("You still need improvement, Keep going\n\n");
        }

        if(relax/strength>5){
            suggest.append("You should do more muscle strength\n\n");
        } else if(strength>relax){
            suggest.append("You should do more muscle relax\n\n");
        }

        if(weight/height>24){
            suggest.append("You should do more core muscle strengthen training\n\n");
            suggest.append("You also should control your body fat\n\n");
        }

        if(Integer.parseInt(pref_age)>40){
            suggest.append("You should do more relaxing during your work, you can set the relax alarm period to 1 hour\n\n");
        }

        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(neck);
        arrayList.add(back);
        arrayList.add(shoulder);
        arrayList.add(leftArm);
        arrayList.add(rightArm);
        Collections.sort(arrayList);
        if(neck==arrayList.get(4)){
            suggest.append("You should do more neck muscle training\n\n");
        } else if(back==arrayList.get(4)){
            suggest.append("You should do more back muscle training\n\n");
        } else if(shoulder==arrayList.get(4)){
            suggest.append("You should do more shoulder muscle training\n\n");
        } else if(leftArm==arrayList.get(4)){
            suggest.append("You should do more leftArm muscle training\n\n");
        } else if(rightArm==arrayList.get(4)){
            suggest.append("You should do more rightArm muscle training\n\n");
        }

        textView.setText(suggest.toString());
    }
}