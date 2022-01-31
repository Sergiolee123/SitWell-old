package com.fyp.sitwell;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CardView mSittingView, mRelaxView, mExerciseView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();

        mSittingView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(Main2Activity.this, "Sitting view clicked.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mRelaxView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(Main2Activity.this, "Relax view clicked.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        mExerciseView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(Main2Activity.this, "Exercise view clicked.",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void init(){
        mSittingView  = findViewById(R.id.card_sitting);
        mRelaxView    = findViewById(R.id.card_sitting);
        mExerciseView = findViewById(R.id.card_sitting);

    }
}