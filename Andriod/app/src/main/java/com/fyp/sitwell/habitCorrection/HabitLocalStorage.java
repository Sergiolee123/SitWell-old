package com.fyp.sitwell.habitCorrection;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class HabitLocalStorage {

    protected static void setId(Context context, int id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("habit", MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("id", id)
                .apply();
    }

    protected static int getId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("habit", MODE_PRIVATE);
        return sharedPreferences.getInt("id", 1);
    }

}
