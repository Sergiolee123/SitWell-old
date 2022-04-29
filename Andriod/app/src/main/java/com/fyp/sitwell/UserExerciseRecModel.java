package com.fyp.sitwell;

import android.util.Log;

public class UserExerciseRecModel {
    private String userID="";
    private String exerciseType="";
    private String exerciseDate="";
    private int exerciseCount=0;

    private final String TAG = "UserExerciseRecModel";

    public void resetAllCol(){
        exerciseType="";
        exerciseDate="";
        exerciseCount=0;
        Log.d(TAG, "reset all values in UserExerciseRecModel but record ID");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getExerciseDate() {
        return exerciseDate;
    }

    public void setExerciseDate(String exerciseDate) {
        this.exerciseDate = exerciseDate;
    }

    public int getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(int exerciseCount) {
        this.exerciseCount = exerciseCount;
    }
}
