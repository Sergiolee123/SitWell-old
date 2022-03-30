package com.fyp.sitwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.fyp.sitwell.muscleTraining.MuscleRelaxActivity;

public class MainFragment  extends Fragment {
    private CardView mSittingView, mRelaxView, mExerciseView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main ,container ,false);


        mSittingView  = (CardView) view.findViewById(R.id.card_sitting);
        mRelaxView    = (CardView) view.findViewById(R.id.card_relax);
        mExerciseView = (CardView) view.findViewById(R.id.card_exercise);
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
                startActivity(new Intent(getActivity(), MuscleStrengthActivity .class));
            }
        });
        return view;
    }
}
