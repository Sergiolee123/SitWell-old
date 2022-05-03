package com.fyp.sitwell;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fyp.sitwell.habitCorrection.HabitCorrectionActivity;
import com.fyp.sitwell.muscleTraining.MuscleRelaxActivity;
import com.fyp.sitwell.muscleTraining.MuscleStrengthenActivity;
import com.fyp.sitwell.report.DBHandler;
import com.google.firebase.auth.FirebaseAuth;

public class MainFragment  extends Fragment {
    private CardView mSittingView, mRelaxView, mExerciseView;
    private TextView remainingDayTextView;
    private DBHandler dbHandler;
    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main ,container ,false);
        dbHandler= new DBHandler(view.getContext());
        mFirebaseAuth = FirebaseAuth.getInstance();
        dbHandler.userID = mFirebaseAuth.getCurrentUser().getUid();
        mSittingView  = (CardView) view.findViewById(R.id.card_sitting);
        mRelaxView    = (CardView) view.findViewById(R.id.card_relax);
        mExerciseView = (CardView) view.findViewById(R.id.card_exercise);
        remainingDayTextView=(TextView)view.findViewById(R.id.RemainDayTxtView);
        setUpRemainDayText();
        mSittingView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Sitting view clicked.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HabitCorrectionActivity.class));
            }
        });

        mRelaxView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Relax view clicked.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MuscleRelaxActivity.class));
            }
        });

        mExerciseView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Exercise view clicked.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MuscleStrengthenActivity.class));
            }
        });
        return view;
    }

    private void setUpRemainDayText(){
        Cursor c1 = dbHandler.checkRepeatedUserInProgress();
        if(c1.getCount()==0){
            remainingDayTextView.setText("Remaining Days: 63");
        }else{
            Cursor cursor = dbHandler.getUserProgress();
            cursor.moveToNext();
            remainingDayTextView.setText("Remaining Days: "+cursor.getInt(0));
        }

    }
}
