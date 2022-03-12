package com.fyp.sitwell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DecimalFormat;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "Userdetails";
    public static final String userID_col ="userID";
    public static final String recordID_col ="recordID";
    public static final String neckCount = "neckCount";
    public static final String backCount = "backCount";
    public static final String SHLDRCount = "ShoulderCount";
    public static final String LT_ARM_Count = "leftArmCount";
    public static final String RT_ARM_Count = "rightArmCount";
    public static final String sitWellCount = "sitWellCount";
    public static final String sitPoorCount = "sitBadCount";
    public static final String duration_col = "duration";
    public static final String sit_accuracy_col = "sitAccuracy";
    public static final String startTime_col = "startTime";
    public static final String endTime_col = "endTime";

    private static final int DB_VERSION = 1;
    private static final String TAG = "CheckCounts";

    private String userID ="", startTime="", endTime="";
    private int recordID =0 ,neckNum=0, backNum=0, SHLDRNum=0,  leftArmNum=0, rightArmNum=0, sitWellNum=0, sitPoorNum=0 ;
    private float duration=0, sitAccuracy =0;

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void calAccuracy() {
        try {
            float numerator = (sitWellNum + sitPoorNum) * 5 - (neckNum + backNum + SHLDRNum + leftArmNum + rightArmNum);
            float denominator = (sitWellNum + sitPoorNum) * 5;
            sitAccuracy = numerator / denominator;
            String calMsg = "numerator = " + numerator + ", denominator = " + denominator + ", sitAccuracy = " + sitAccuracy;
            Log.d(TAG, calMsg );
        }catch (Exception e){ e.printStackTrace();}
    }


    public void printDetails(){
        String str = " printDetails =  userID = " + userID + ", recordID = " + recordID + ", neckNum = " + neckNum + ", backCount = " + backNum + ", SHLDRCount = " + SHLDRNum +
                " , leftArmNum = " + leftArmNum + ", rightArmNum = " + rightArmNum + ", sitWellNum = " + sitWellNum + ", sitPoorNum = " + sitPoorNum +
                " , duration = " + duration + " , accuracy = " + sitAccuracy + " , startTime = " + startTime +" , endTime = " + endTime ;
        Log.d(TAG,str );
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DB_NAME + " (" +
                recordID_col + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                userID_col + " TEXT NOT NULL,"+
                neckCount +" INTEGER,"+
                backCount +" INTEGER,"+
                SHLDRCount +" INTEGER,"+
                LT_ARM_Count +" INTEGER,"+
                RT_ARM_Count + " INTEGER,"+
                sitWellCount + " INTEGER,"+
                sitPoorCount + " INTEGER," +
                sit_accuracy_col + " REAL,"+
                startTime_col + " TEXT NOT NULL,"+
                endTime_col + " TEXT NOT NULL,"+
                duration_col + " REAL NOT NULL"
                +")";

        Log.d(TAG, query);
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewRecord() {
        long result;
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(userID_col, userID);
            values.put(neckCount, neckNum);
            values.put(backCount, backNum);
            values.put(SHLDRCount, SHLDRNum);
            values.put(LT_ARM_Count, leftArmNum);
            values.put(RT_ARM_Count, rightArmNum);
            values.put(sitWellCount, sitWellNum);
            values.put(sitPoorCount, sitPoorNum);
            values.put(sit_accuracy_col, sitAccuracy);
            values.put(startTime_col, startTime);
            values.put(endTime_col, endTime);
            values.put(duration_col, duration);

            String StoredRecord = " userID = " + userID + ", recordID = " + recordID + ", neckNum = " + neckNum + ", backCount = " + backNum + ", SHLDRCount = " + SHLDRNum +
                    " , leftArmNum = " + leftArmNum + ", rightArmNum = " + rightArmNum + ", sitWellNum = " + sitWellNum + ", sitPoorNum = " + sitPoorNum +
                    " , accuracy = " + sitAccuracy +  " , startTime = " + startTime +  " , endTime = " + endTime +  " , duration = " + duration ;
            Log.d(TAG, StoredRecord);

            result = db.insert(DB_NAME, null, values);
            db.close();

        if(result==-1){
            Log.d(TAG, "new record insertion fails");
            //return false;
        }else{
            Log.d(TAG, "new record added");
           // return true;
        }

        }catch (Exception e){ Log.d(TAG, String.valueOf(e));}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    public Cursor getAllData() { //ASC
        SQLiteDatabase db = this.getWritableDatabase();
        android.database.Cursor cursor = db.rawQuery("Select * from " + DB_NAME, null);
        return cursor;
    }

    public Cursor getALLDataDESC(){
        SQLiteDatabase db = this.getWritableDatabase();
        android.database.Cursor cursor = db.rawQuery("Select * from " + DB_NAME + " order by " + recordID_col + " DESC", null);
        return cursor;
    }

    public Cursor getOneRowData(int recordID){
        SQLiteDatabase db = this.getWritableDatabase();
        android.database.Cursor cursor = db.rawQuery("Select * from "+DB_NAME + " WHERE recordID = " + recordID, null);
        return cursor;
    }

    public void removeOneRow(int recordID){
        String delete_query = "DELETE FROM " + DB_NAME+ " WHERE recordID = " + recordID;
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(delete_query);
        db.close();
    }

    public void resetAllCol(){
        userID="";
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

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

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
}
