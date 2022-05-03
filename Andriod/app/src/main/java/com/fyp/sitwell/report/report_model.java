package com.fyp.sitwell.report;

import java.text.DecimalFormat;

public class report_model {

    private String recordID, duration, sitAccuracy, startTime, endTime;
    private int neckNum, backNum, SHLDRNum, leftArmNum, rightArmNum, sitWellNum, sitPoorNum;


    public report_model(String recordID, int neckNum, int backNum, int SHLDRNum, int leftArmNum, int rightArmNum, int sitWellNum, int sitPoorNum, float sitAccuracy , String startTime, String endTime, float duration) {
        this.recordID = recordID;
        this.neckNum = neckNum;
        this.backNum = backNum;
        this.SHLDRNum = SHLDRNum;
        this.leftArmNum = leftArmNum;
        this.rightArmNum = rightArmNum;
        this.sitWellNum = sitWellNum;
        this.sitPoorNum = sitPoorNum;
        this.duration = convertDurUnit(duration);
        this.sitAccuracy = String.valueOf(sitAccuracy*100)+" %";
        this.startTime=startTime;
        this.endTime=endTime;
    }

    public String convertDurUnit(float duration){
        String msg = "";
        final DecimalFormat df =  new DecimalFormat("0.00");

        if(duration<60){
            msg += duration + " s";
        }else if(duration/60 < 60){
            msg += df.format(duration/60) + " min";
        }else{
            msg += df.format(duration/60/60) + " hr";
        }
        return msg;
    }

    public String getRecordID() {
        return recordID;
    }

    public int getNeckNum() {
        return neckNum;
    }

    public int getBackNum() {
        return backNum;
    }

    public int getSHLDRNum() {
        return SHLDRNum;
    }

    public int getLeftArmNum() {
        return leftArmNum;
    }

    public int getRightArmNum() {
        return rightArmNum;
    }

    public int getSitWellNum() {
        return sitWellNum;
    }

    public int getSitPoorNum() {
        return sitPoorNum;
    }

    public String getDuration() {
        return duration;
    }

    public String getSitAccuracy() {
        return sitAccuracy;
    }

    public String getStartTime() {  return startTime;  }

    public String getEndTime() {  return endTime;  }


}