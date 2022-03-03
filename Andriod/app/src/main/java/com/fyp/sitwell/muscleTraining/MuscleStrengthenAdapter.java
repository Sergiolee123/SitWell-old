package com.fyp.sitwell.muscleTraining;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.sitwell.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MuscleStrengthenAdapter extends RecyclerView.Adapter<MuscleStrengthenAdapter.ViewHolder> {
    private HashMap<String, Class<?extends MuscleTrainingInterface>> trainingMethod;
    private String[] trainingNames;

    public MuscleStrengthenAdapter(HashMap<String, Class<?extends MuscleTrainingInterface>> trainingMethod){
        this.trainingMethod = trainingMethod;
        this.trainingNames = new String[trainingMethod.keySet().size()];
        trainingMethod.keySet().toArray(trainingNames);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.muscle_strengthen_cardview, parent, false);
        return new MuscleStrengthenAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title_exercise.setText(trainingNames[position]);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MuscleTrainingActivity.class);
            intent.putExtra("class",trainingMethod.get(trainingNames[position]));
           holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trainingNames.length;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title_exercise;

        public ViewHolder(View view){
            super(view);

            title_exercise = view.findViewById(R.id.title_exercise);
        }
    }
}
