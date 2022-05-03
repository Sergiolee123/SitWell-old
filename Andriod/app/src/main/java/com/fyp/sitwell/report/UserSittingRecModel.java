package com.fyp.sitwell.report;

import android.util.Log;

public class UserSittingRecModel {
    private String userID ="", startTime="", endTime="";
    private int recordID =0 ,neckNum=0, backNum=0, SHLDRNum=0,  leftArmNum=0, rightArmNum=0, sitWellNum=0, sitPoorNum=0, programRepeatedTimes=0;
    private float duration=0, sitAccuracy =0;

    private final String TAG = "UserSittingRecModel";

    public void resetAllCol(){
        neckNum=0;
        backNum=0;
        SHLDRNum=0;
        leftArmNum=0;
        rightArmNum=0;
        sitWellNum=0;
        sitPoorNum=0;
        duration=0;
        sitAccuracy =0;
        startTime="";
        endTime="";
        Log.d(TAG, "reset all values but record ID");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getRecordID() { return recordID; }


    public int getNeckNum() {
        return neckNum;
    }

    public void setNeckNum(int neckNum) {
        this.neckNum = neckNum;
    }

    public int getBackNum() {
        return backNum;
    }

    public void setBackNum(int backNum) {
        this.backNum = backNum;
    }

    public int getSHLDRNum() {
        return SHLDRNum;
    }

    public void setSHLDRNum(int SHLDRNum) {
        this.SHLDRNum = SHLDRNum;
    }

    public int getLeftArmNum() {
        return leftArmNum;
    }

    public void setLeftArmNum(int leftArmNum) {
        this.leftArmNum = leftArmNum;
    }

    public int getRightArmNum() {
        return rightArmNum;
    }

    public void setRightArmNum(int rightArmNum) {
        this.rightArmNum = rightArmNum;
    }

    public int getSitWellNum() {
        return sitWellNum;
    }

    public void setSitWellNum(int sitWellNum) {
        this.sitWellNum = sitWellNum;
    }

    public int getSitPoorNum() {
        return sitPoorNum;
    }

    public void setSitPoorNum(int sitPoorNum) {
        this.sitPoorNum = sitPoorNum;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getSitAccuracy() {
        return sitAccuracy;
    }

    public void setSitAccuracy(float sitAccuracy) {
        this.sitAccuracy = sitAccuracy;
    }

    public int getProgramRepeatedTimes() {
        return programRepeatedTimes;
    }

    public void setProgramRepeatedTimes(int programRepeatedTimes) {
        this.programRepeatedTimes = programRepeatedTimes;
    }
}
