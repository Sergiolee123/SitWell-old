package com.fyp.sitwell.alarm;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

    protected static void setId(Context context, int id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("alarm", MODE_PRIVATE);
        id += 1;
        sharedPreferences.edit()
                .putInt("id", id)
                .apply();
    }

    protected static int getId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("alarm", MODE_PRIVATE);
        return sharedPreferences.getInt("id", 0);
    }

    protected static void resetId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("alarm", MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("id", 0)
                .apply();
    }
}
