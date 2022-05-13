package com.fyp.sitwell;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.sitwell.report.DBHandler;

import org.w3c.dom.Text;

public class CourseEndActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private TextView reviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_end);
        reviewTextView= findViewById(R.id.reviewTextView);
        dbHandler= new DBHandler(this);
        showReviewTxtView();
    }

    private void showReviewTxtView(){
        Cursor c  = dbHandler.getFirstWeekData();
        Cursor c2  = dbHandler.getLastWeekData();
        float firstWeekAvg = 0 , lastWeekAvg=0;

        while(c.moveToNext()){
            firstWeekAvg+=c.getFloat(9);
        }
        firstWeekAvg=firstWeekAvg/7;

        while(c2.moveToNext()){
            lastWeekAvg+=c.getFloat(9);
        }
        lastWeekAvg=lastWeekAvg/7;

        if(lastWeekAvg>firstWeekAvg){
            reviewTextView.setText("You have made huge improvements\n\nYou can end this program now");

        }

        if(firstWeekAvg>lastWeekAvg){
            reviewTextView.setText("You still need improvement!\n\nYou should repeat this program again");
        }




    }

}