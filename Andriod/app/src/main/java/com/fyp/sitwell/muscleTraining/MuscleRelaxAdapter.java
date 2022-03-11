package com.fyp.sitwell.muscleTraining;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.sitwell.R;

import java.util.Arrays;
import java.util.HashMap;

public class MuscleRelaxAdapter extends RecyclerView.Adapter<MuscleRelaxAdapter.ViewHolder>{

    private HashMap<String, Class<?extends MuscleTrainingInterface>> trainingMethod;
    private String[] trainingNames;

    public MuscleRelaxAdapter(HashMap<String, Class<?extends MuscleTrainingInterface>> trainingMethod){
        this.trainingMethod = trainingMethod;
        this.trainingNames = new String[trainingMethod.keySet().size()];
        trainingMethod.keySet().toArray(trainingNames);
        Log.e("abcdefg", Arrays.toString(trainingNames));
    }

    @NonNull
    @Override
    public MuscleRelaxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.muscle_relax_cardview, parent, false);
        return new MuscleRelaxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MuscleRelaxAdapter.ViewHolder holder, int position) {
        holder.title_exercise.setText(trainingNames[position]);
        Log.e("abcdefg", position+"");
        Log.e("abcdefg", trainingNames[position]);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MuscleRelaxTrainingActivity.class);
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
