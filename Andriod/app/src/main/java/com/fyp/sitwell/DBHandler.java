package com.fyp.sitwell;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "UserRecords";
    private static final String DB2_NAME = "UserProgress";
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

    public static final String progDaysLeft_col = "progDaysLeft";
    public static final String progStatus_col = "progStatus";
    public static final String ProgramRepeatedTimes_col = "ProgramRepeatedTimes";

    private static final int DB_VERSION = 1;
    private static final String TAG = "CheckCounts";

    private String userID ="", startTime="", endTime="";
    private int recordID =0 ,neckNum=0, backNum=0, SHLDRNum=0,  leftArmNum=0, rightArmNum=0, sitWellNum=0, sitPoorNum=0,  ProgramRepeatedTimes=0;
    private float duration=0, sitAccuracy =0;
    private int default_days=3;
    private HashSet<String> set=new HashSet();

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void calAccuracy() {
        try {
            float numerator = (sitWellNum + sitPoorNum) * 5 - (neckNum + backNum + SHLDRNum + leftArmNum + rightArmNum);
            float denominator = (sitWellNum + sitPoorNum) * 5;
            sitAccuracy = numerator / denominator *100;
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
        db.execSQL("PRAGMA foreign_keys = ON");
        String DB1_query = "CREATE TABLE " + DB_NAME + " (" +
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
                duration_col + " REAL NOT NULL," +
                "FOREIGN KEY ("+userID_col+")" +" REFERENCES "+DB2_NAME +"("+userID_col+")"
                +" ON DELETE CASCADE "+")";

        String DB2_query = "CREATE TABLE " + DB2_NAME + " (" +
                userID_col + " TEXT PRIMARY KEY," +
                progDaysLeft_col + " INTEGER DEFAULT 63," +
                progStatus_col + " REAL," +
                ProgramRepeatedTimes_col + " INTEGER DEFAULT 0"
                + ")";

        db.execSQL(DB1_query);
        db.execSQL(DB2_query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewRecord() {
        //the detection ends before the first time of detection, no data collects
        if(sitWellNum==0 && sitPoorNum==0) return;

        //check if the record on the day is stored
        String date = getFullDateTime();
        String [] arr = date.split(" ");
        Cursor cursor = this.getSameDateRec(arr[0]); //retrieve the same day rec
        Log.e(TAG,arr[0]);
        int cursorCount=cursor.getCount();
        Log.e(TAG,"cursorCount = "+cursorCount );

        //no related record is found
        if(cursorCount==0) {
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
                        " , accuracy = " + sitAccuracy + " , startTime = " + startTime + " , endTime = " + endTime + " , duration = " + duration;
                Log.d(TAG, StoredRecord);

                result = db.insert(DB_NAME, null, values);
                db.close();

                if (result == -1) {
                    Log.d(TAG, "new record insertion fails");
                    //return false;
                } else {
                    Log.d(TAG, "new record added");
                    // return true;
                }

            } catch (Exception e) {
                Log.d(TAG, "wrong");
            }

        }else if(cursorCount==1){ //found the same rec
            cursor.moveToNext();
            float oldRecAcc=0, oldRecDur=0,newAur=0, totalDur=0;

            String oldrec ="oldrec : " +  "recordID = " + cursor.getInt(0) + ", userID = " + cursor.getString(1) +  ", neckNum = " + cursor.getInt(2) + ", backCount = " + cursor.getInt(3) + ", SHLDRCount = " + cursor.getInt(4) +
                    " , leftArmNum = " + cursor.getInt(5) + ", rightArmNum = " + cursor.getInt(6) + ", sitWellNum = " + cursor.getInt(7) + ", sitPoorNum = " + cursor.getInt(8) +
                    " , accuracy = " + cursor.getFloat(9) + " , startTime = " + cursor.getString(10) + " , endTime = " + cursor.getString(11)  + " , duration = " + cursor.getFloat(12);

            String newrec = "newrec : " + "recordID = " + cursor.getInt(0) + ", userID = " + cursor.getString(1) +  ", neckNum = " + neckNum + ", backCount = " + backNum + ", SHLDRCount = " + SHLDRNum +
                    " , leftArmNum = " + leftArmNum + ", rightArmNum = " + rightArmNum + ", sitWellNum = " + sitWellNum + ", sitPoorNum = " + sitPoorNum +
                    " , accuracy = " + sitAccuracy + " , startTime = " + startTime + " , endTime = " + endTime  + " , duration = " + duration;

            neckNum+=cursor.getInt(2);
            backNum+=cursor.getInt(3);
            SHLDRNum+=cursor.getInt(4);
            leftArmNum+=cursor.getInt(5);
            rightArmNum+=cursor.getInt(6);
            sitWellNum+=cursor.getInt(7);
            sitPoorNum+=cursor.getInt(8);
            oldRecAcc= cursor.getFloat(9);
            oldRecDur= cursor.getFloat(12);
            totalDur= oldRecDur+duration;
            newAur= sitAccuracy*duration/totalDur + oldRecAcc*oldRecDur/totalDur;
            String finalrec = "finalred : " + "recordID = " + cursor.getInt(0) + ", userID = " + cursor.getString(1) +  ", neckNum = " +neckNum + ", backCount = " + backNum+ ", SHLDRCount = " + SHLDRNum +
                    " , leftArmNum = " + leftArmNum + ", rightArmNum = " +rightArmNum + ", sitWellNum = " + sitWellNum+ ", sitPoorNum = " + sitPoorNum +
                    " , accuracy = " + newAur + " , startTime = " + cursor.getString(10) + " , endTime = " + getFullDateTime()  + " , duration = " + totalDur;
            Log.e(TAG, oldrec);
            Log.e(TAG, newrec);
            Log.e(TAG, finalrec);
            this.updateOneRow(cursor.getInt(0),cursor.getString(1),neckNum, backNum, SHLDRNum,leftArmNum, rightArmNum, sitWellNum, sitPoorNum ,newAur,cursor.getString(10), getFullDateTime(), totalDur);
        }
        this.updateUserProgress();
    }

    private void updateUserProgress(){
        try{
            Log.e(TAG," check user :" + userID);
            boolean checkRepeateduser = checkRepeatedUser(this.userID);

        if(!checkRepeateduser){ //insert new record
            Log.e(TAG,"checkRepeateduser==false" );
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(userID_col, userID);
            values.put(progDaysLeft_col, default_days-1);//****
            if(!set.contains(getDateOnly()))
            set.add(getDateOnly());
            Log.e(TAG,"calProgStatus :" + calProgStatus(default_days-1) );
            values.put(progStatus_col, calProgStatus(default_days-1));
            values.put(ProgramRepeatedTimes_col,ProgramRepeatedTimes );
            db.insert(DB2_NAME, null, values);
            db.close();
            Log.e(TAG," check user inserted the new record" );
        }else{
            //check when to update the DaysLeft by using the date
            Log.e(TAG,"checkRepeateduser==true" );
            Cursor cursor = findRepeatedRow(this.userID);
            cursor.moveToNext();
            Log.e(TAG, "cursor.getString(0) = "+cursor.getString(0));
            Log.e(TAG, "cursor.getInt(1) = "+cursor.getInt(1));
            Log.e(TAG, "cursor.getFloat(2) = "+cursor.getFloat(2));
            Log.e(TAG, "cursor.getInt(3) = "+cursor.getInt(3));

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            //reset the progDaysLeft column to default 63
            if(cursor.getInt(1)==0){ //check if progDaysLeft ==0
                if(!set.contains(getDateOnly())){
                    set.add(getDateOnly());
                }
                values.put(progDaysLeft_col, default_days-1);//fk2
                values.put(ProgramRepeatedTimes_col, cursor.getInt(3)+1);
                values.put(progStatus_col,  calProgStatus(2));   //***
                if(set.size()==default_days)
                    set.clear();

            }else if(cursor.getInt(1)>0){
                if(!set.contains(getDateOnly())){
                    values.put(progDaysLeft_col, cursor.getInt(1)-1); //***
                }else{
                    values.put(progDaysLeft_col, cursor.getInt(1));
                    set.add(getDateOnly());
                }
                values.put(ProgramRepeatedTimes_col, cursor.getInt(3));
                float a = (float)(cursor.getInt(1));
                Log.e(TAG, "a = "+a );//***
                values.put(progStatus_col,  calProgStatus(a-1) ); //***
                Log.e(TAG,"(float)cursor.getInt(1)-1) = " + a );
            }
            db.update(DB2_NAME, values, userID_col+"=?", new String[]{userID});   //但當programcheck=0 要點樣RESET 做63  //this line has problems
            db.close();
            Log.e(TAG," check user updated the record" );

            for (String s : set) {
                Log.e(TAG, "DATE ONLY" + s);
            }
        }
        }catch (Exception e){ Log.e(TAG, e.getMessage()); }
    }



    private float calProgStatus(float progDaysLeft) {
        Log.e(TAG, "numerator = " + (float)default_days+" - "+progDaysLeft);
            float numerator =(float)default_days-progDaysLeft;
            float denominator = (float)default_days;
        return numerator/denominator ;
    }

    public Cursor getSameDateRec(String date){ //search By startTime
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DB_NAME + " where startTime LIKE '"+date+"%'", null);
        return cursor;
    }

    public void updateOneRow(int recordID, String userID, int neckNum, int backNum, int SHLDRNum, int leftArmNum, int rightArmNum, int sitWellNum, int sitPoorNum, float newAur, String startTime ,String endTime, float totalDur){

        String update_query = "UPDATE " + DB_NAME + " SET neckCount = " + neckNum + ", backCount = " + backNum + ", ShoulderCount = " + SHLDRNum
                + ", leftArmCount = " + leftArmNum + ", rightArmCount = " + rightArmNum + ", sitWellCount = " + sitWellNum + ", sitBadCount = " + sitPoorNum
                + ", sitAccuracy = " + newAur + ", startTime = " + startTime  +", endTime = " + endTime  + ", duration = " + totalDur + " WHERE recordID = " + recordID;

        Log.e(TAG, "updateOneRow scope "+update_query);
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(userID_col, userID);
        values.put(neckCount, neckNum);
        values.put(backCount, backNum);
        values.put(SHLDRCount, SHLDRNum);
        values.put(LT_ARM_Count, leftArmNum);
        values.put(RT_ARM_Count, rightArmNum);
        values.put(sitWellCount, sitWellNum);
        values.put(sitPoorCount, sitPoorNum);
        values.put(sit_accuracy_col, newAur);
        values.put(startTime_col, startTime);
        values.put(endTime_col, endTime);
        values.put(duration_col, totalDur);
        //db.update(DB_NAME, values, recordID_col+"="+recordID, null);
        db.update(DB_NAME, values, recordID_col+"= ?", new String[]{""+recordID});
        db.close();
    }

    private boolean checkRepeatedUser(String userID){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String select_query = "SELECT * from " + DB2_NAME + " WHERE " + userID_col
                    +" = " + "'" +userID+"'";
            Log.e(TAG, select_query);
            Cursor cursor = db.rawQuery(select_query, null);
            int a = cursor.getCount();
            db.close();

            if(a==1)
                return true;
            else if (a==0)
                return false;
        }catch (Exception e){
            Log.e(TAG, "checkRepeatedUser sth wrong");
            return false;
        }
        return false;
    }

    private String getFullDateTime(){
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String nowDate= formatter1.format(new Date());
        return nowDate;
    }

    private String getDateOnly(){
        String nowDate= getFullDateTime();
        String [] arr = nowDate.split(" ");
        Log.e(TAG,"getDateOnly() = "+ arr[0]);
        return arr[0];
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    public Cursor getAllData() { //result ASC
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DB_NAME, null);
        return cursor;
    }

    public Cursor getALLDataDESC(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DB_NAME + " order by " + recordID_col + " DESC", null);
        return cursor;
    }

    public Cursor findRepeatedRow(String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DB2_NAME + " WHERE userID = " + "'"+ userID +"'", null);
        return cursor;
    }

    //need to modify
    public Cursor updateRepeatedRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DB_NAME + " WHERE userID = " + "'"+ userID +"'", null);
        return cursor;
    }



    public Cursor getOneRowData(int recordID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+DB_NAME + " WHERE recordID = " + recordID, null);
        return cursor;
    }



    public boolean removeOneRow(int recordID){
        try{
            String delete_query = "DELETE FROM " + DB_NAME+ " WHERE recordID = " + recordID;
            SQLiteDatabase db= this.getWritableDatabase();
            db.execSQL(delete_query);
            db.close();
            return true;
        }catch (Exception e){
            return false;
        }
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
