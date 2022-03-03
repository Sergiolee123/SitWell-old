package com.fyp.sitwell.muscleTraining;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.fyp.sitwell.R;

import java.util.Arrays;
import java.util.HashMap;

public class MuscleStrengthenActivity extends AppCompatActivity {

    private static HashMap<String, Class<?extends MuscleTrainingInterface>> trainingMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_muscle_strengthen);
        setTrainingMethod();

        RecyclerView recyclerView = findViewById(R.id.recycler_strengthen);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MuscleStrengthenAdapter(trainingMethod));



    }

    private void setTrainingMethod(){
        trainingMethod = new HashMap<>();
        trainingMethod.put("lying lateral leg lift", GluteStrengthen.class);
    }

}