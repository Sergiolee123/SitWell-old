package com.fyp.sitwell;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class EndLocalStorage {

    protected static void setId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("courseEnd", MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("id", 1)
                .apply();
    }

    protected static int getId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("courseEnd", MODE_PRIVATE);
        return sharedPreferences.getInt("id", 0);
    }

    protected static void resetId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("courseEnd", MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("id", 0)
                .apply();
    }
}
