package com.fyp.sitwell.muscleTraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fyp.sitwell.R;

import java.util.HashMap;

public class MuscleRelaxActivity extends AppCompatActivity {

    private static HashMap<String, Class<?extends MuscleTrainingInterface>> trainingMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_muscle_relax);
        setTrainingMethod();

        RecyclerView recyclerView = findViewById(R.id.recycler_relax);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MuscleRelaxAdapter(trainingMethod));



    }

    private void setTrainingMethod(){
        trainingMethod = new HashMap<>(5);
        trainingMethod.put("Neck Muscle relax", NeckMuscleRelax.class);
        trainingMethod.put("Wrist relax", WristMuscleRelax.class);
    }
}