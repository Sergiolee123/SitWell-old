package com.fyp.sitwell.report;

import android.util.Log;

public class UserExerciseRecModel {
    private String userID="";
    private int strengthExerciseCount =0;
    private int relaxCount=0;
    private int progRepeatedTimes=0;

    private final String TAG = "UserExerciseRecModel";

    public void resetAllCol(){
        relaxCount=0;
        strengthExerciseCount =0;
        Log.d(TAG, "reset all values in UserExerciseRecModel but userID");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getStrengthExerciseCount() {
        return strengthExerciseCount;
    }

    public void setStrengthExerciseCount(int strengthExerciseCount) {
        this.strengthExerciseCount = strengthExerciseCount;
        Log.e("strengthExerciseCount","value = "+strengthExerciseCount);
    }

    public int getRelaxCount() {
        return relaxCount;
    }

    public void setRelaxCount(int relaxCount) {
        this.relaxCount = relaxCount;
    }

    public int getProgRepeatedTimes() {
        return progRepeatedTimes;
    }

    public void setProgRepeatedTimes(int progRepeatedTimes) {
        this.progRepeatedTimes = progRepeatedTimes;
    }

}
