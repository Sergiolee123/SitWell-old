package com.fyp.sitwell.report;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "CheckCounts";
    private static final String TAG2 = "Date";

    private static UserSittingRecModel userSittingRec= new UserSittingRecModel();
    private static UserExerciseRecModel userExerciseRec = new UserExerciseRecModel();

    private int ProgramRepeatedTimes=0;
    private static int default_days=63;
    private static HashSet<String> dateSet = new HashSet<>(); //check Date
    public static String userID;


    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DBConstant.DB_NAME, null, DBConstant.DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");
        String DB1_query = "CREATE TABLE " + DBConstant.DB_NAME + " (" +
                DBConstant.recordID_col + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DBConstant.userID_col + " TEXT NOT NULL,"+
                DBConstant.neckCount +" INTEGER,"+
                DBConstant.backCount +" INTEGER,"+
                DBConstant.SHLDRCount +" INTEGER,"+
                DBConstant.LT_ARM_Count +" INTEGER,"+
                DBConstant. RT_ARM_Count + " INTEGER,"+
                DBConstant.sitWellCount + " INTEGER,"+
                DBConstant.sitPoorCount + " INTEGER," +
                DBConstant.sit_accuracy_col + " REAL NOT NULL,"+
                DBConstant.startTime_col + " TEXT NOT NULL,"+
                DBConstant.endTime_col + " TEXT NOT NULL,"+
                DBConstant.duration_col + " REAL NOT NULL,"+
                DBConstant.programRepeatedTimes_col + " INTEGER NOT NULL," +
                "FOREIGN KEY ("+DBConstant.programRepeatedTimes_col +")" +" REFERENCES "+DBConstant.DB2_NAME +"("+DBConstant.programRepeatedTimes_col+"),"+
                "FOREIGN KEY ("+DBConstant.userID_col+")" +" REFERENCES "+DBConstant.DB2_NAME +"("+DBConstant.userID_col+")"
                +" ON DELETE CASCADE "+")";

        String DB2_query = "CREATE TABLE " + DBConstant.DB2_NAME + " (" +
                DBConstant.userID_col + " TEXT PRIMARY KEY," +
                DBConstant.progDaysLeft_col + " INTEGER DEFAULT 63," +
                DBConstant.progStatus_col + " REAL," +
                DBConstant.programRepeatedTimes_col + " INTEGER DEFAULT 0,"+
                DBConstant.datesString_col + " TEXT NOT NULL"
                + ")";

        String DB3_query = "CREATE TABLE " + DBConstant.DB3_NAME + " (" +
                DBConstant.exRecID_col + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                DBConstant.userID_col + " TEXT NOT NULL," +
                /*DBConstant.exericseTypes_col + " TEXT NOT NULL CHECK ("+DBConstant.exericseTypes_col+"="+"'"+"RELAX" +"'" +" OR " +
                DBConstant.exericseTypes_col+"="+"'"+"STRENGTH" +"'" +" )," +*/
                DBConstant.strengthExerciseCount_col +" INTEGER DEFAULT 0,"+
                DBConstant.relaxExerciseCount_col + " INTEGER DEFAULT 0,"+
                DBConstant.programRepeatedTimes_col + " INTEGER NOT NULL," +
                "FOREIGN KEY ("+DBConstant.programRepeatedTimes_col +")" +" REFERENCES "+DBConstant.DB2_NAME +"("+DBConstant.programRepeatedTimes_col+"),"+
                "FOREIGN KEY ("+DBConstant.userID_col+")" +" REFERENCES "+DBConstant.DB2_NAME +"("+DBConstant.userID_col+")"
                +" ON DELETE CASCADE "+
                ")";

        db.execSQL(DB1_query);
        db.execSQL(DB2_query);
        db.execSQL(DB3_query);
    }

    //sth incomplete here only insert the record in Usersitting Record table, but does not update User Progress Table
    public void insertRandomSittingRecord(){
        long result;
        int insertRecNum= 21;
        while(insertRecNum-->0) {
            try (SQLiteDatabase db = getWritableDatabase()) {
                ContentValues values = new ContentValues();
                Random rand = new Random();
                //int int_random = new Random().nextInt(50);
                values.put(DBConstant.userID_col, userID);
                values.put(DBConstant.neckCount, rand.nextInt(50));
                values.put(DBConstant.backCount, rand.nextInt(50));
                values.put(DBConstant.SHLDRCount, rand.nextInt(50));
                values.put(DBConstant.LT_ARM_Count, rand.nextInt(50));
                values.put(DBConstant.RT_ARM_Count, rand.nextInt(50));
                values.put(DBConstant.sitWellCount, rand.nextInt(20));
                values.put(DBConstant.sitPoorCount, rand.nextInt(20));
                int acc = rand.nextInt(100);
                float accf = (float) acc;
                values.put(DBConstant.sit_accuracy_col, accf);
                values.put(DBConstant.startTime_col, userSittingRec.getStartTime());
                values.put(DBConstant.endTime_col, userSittingRec.getEndTime());
                int acc2 = rand.nextInt(100);
                float accf2 = (float) acc2;
                values.put(DBConstant.duration_col, accf2);
                values.put(DBConstant.programRepeatedTimes_col, 0);

                String StoredRecord = " userID = " + userSittingRec.getUserID() + ", recordID = " + userSittingRec.getRecordID() + ", neckNum = " + userSittingRec.getNeckNum() + ", backCount = " + userSittingRec.getBackNum() + ", SHLDRCount = " + userSittingRec.getSHLDRNum() +
                        " , leftArmNum = " + userSittingRec.getLeftArmNum() + ", rightArmNum = " + userSittingRec.getRightArmNum() + ", sitWellNum = " + userSittingRec.getSitWellNum() + ", sitPoorNum = " + userSittingRec.getSitPoorNum() +
                        " , accuracy = " + userSittingRec.getSitAccuracy() + " , startTime = " + userSittingRec.getStartTime() + " , endTime = " + userSittingRec.getEndTime() + " , duration = " + userSittingRec.getDuration();

                Log.d(TAG, StoredRecord);

                result = db.insert(DBConstant.DB_NAME, null, values);
                db.close();

                if (result == -1) {
                    Log.d(TAG, "new generated record insertion fails");
                    //return false;
                } else {
                    Log.d(TAG, "new generated record added");
                    // return true;
                }

            } catch (Exception e) {
                Log.d(TAG, "sth wrong");
            }
        }
    }

    // this method is use to add new course to our sqlite database.
    public void addNewSittingRecord() {
        //the detection ends before the first time of detection, no data collects
        if(userSittingRec.getSitWellNum()==0 && userSittingRec.getSitPoorNum()==0) return;

        setProgRepeatedTimeInSitRec();

        //check if the record on the day is stored
        String date = getFullDateTime();
        String [] arr = date.split(" ");
        Cursor cursor = this.getSameDateRec(arr[0]); //retrieve the same day rec
        Log.e(TAG,arr[0]);
        int sitTotalRecCount=cursor.getCount();
        Log.e(TAG,"cursorCount = "+sitTotalRecCount );

        //no related record is found
        if(sitTotalRecCount==0) {
            try (SQLiteDatabase db = getWritableDatabase()) {
                ContentValues values = new ContentValues();
                values.put(DBConstant.userID_col, userSittingRec.getUserID());
                values.put(DBConstant.neckCount, userSittingRec.getNeckNum());
                values.put(DBConstant.backCount, userSittingRec.getBackNum());
                values.put(DBConstant.SHLDRCount, userSittingRec.getSHLDRNum());
                values.put(DBConstant.LT_ARM_Count, userSittingRec.getLeftArmNum());
                values.put(DBConstant.RT_ARM_Count, userSittingRec.getRightArmNum());
                values.put(DBConstant.sitWellCount, userSittingRec.getSitWellNum());
                values.put(DBConstant.sitPoorCount, userSittingRec.getSitPoorNum());
                values.put(DBConstant.sit_accuracy_col, userSittingRec.getSitAccuracy());
                values.put(DBConstant.startTime_col, userSittingRec.getStartTime());
                values.put(DBConstant.endTime_col, userSittingRec.getEndTime());
                values.put(DBConstant.duration_col, userSittingRec.getDuration());
                values.put(DBConstant.programRepeatedTimes_col, userSittingRec.getProgramRepeatedTimes());
                Log.e("userSittingRec.getProgramRepeatedTimes()",""+userSittingRec.getProgramRepeatedTimes());

               String StoredRecord = " userID = " + userSittingRec.getUserID() + ", recordID = " + userSittingRec.getRecordID() + ", neckNum = " + userSittingRec.getNeckNum() + ", backCount = " + userSittingRec.getBackNum() + ", SHLDRCount = " + userSittingRec.getSHLDRNum() +
                       " , leftArmNum = " + userSittingRec.getLeftArmNum() + ", rightArmNum = " + userSittingRec.getRightArmNum() + ", sitWellNum = " + userSittingRec.getSitWellNum() + ", sitPoorNum = " + userSittingRec.getSitPoorNum() +
                       " , accuracy = " + userSittingRec.getSitAccuracy() + " , startTime = " + userSittingRec.getStartTime() + " , endTime = " + userSittingRec.getEndTime()  + " , duration = " + userSittingRec.getDuration();
                Log.d(TAG, StoredRecord);

                db.insert(DBConstant.DB_NAME, null, values);

            } catch (Exception e) {
                Log.d(TAG, "wrong");
            }

        }else if(sitTotalRecCount==1){ //found the same rec
            cursor.moveToNext();
            float oldRecAcc, oldRecDur,newAur, totalDur;

            String oldrec ="oldrec : " +  "recordID = " + cursor.getInt(0) + ", userID = " + cursor.getString(1) +  ", neckNum = " + cursor.getInt(2) + ", backCount = " + cursor.getInt(3) + ", SHLDRCount = " + cursor.getInt(4) +
                    " , leftArmNum = " + cursor.getInt(5) + ", rightArmNum = " + cursor.getInt(6) + ", sitWellNum = " + cursor.getInt(7) + ", sitPoorNum = " + cursor.getInt(8) +
                    " , accuracy = " + cursor.getFloat(9) + " , startTime = " + cursor.getString(10) + " , endTime = " + cursor.getString(11)  + " , duration = " + cursor.getFloat(12);

            String newrec = "newrec : " + "recordID = " + cursor.getInt(0) + ", userID = " + cursor.getString(1) +  ", neckNum = " + userSittingRec.getNeckNum() + ", backCount = " + userSittingRec.getBackNum() + ", SHLDRCount = " + userSittingRec.getSHLDRNum() +
                    " , leftArmNum = " + userSittingRec.getLeftArmNum() + ", rightArmNum = " + userSittingRec.getRightArmNum() + ", sitWellNum = " + userSittingRec.getSitWellNum() + ", sitPoorNum = " + userSittingRec.getSitPoorNum() +
                    " , accuracy = " + userSittingRec.getSitAccuracy() + " , startTime = " + userSittingRec.getStartTime() + " , endTime = " + userSittingRec.getEndTime()  + " , duration = " + userSittingRec.getDuration();

            userSittingRec.setNeckNum(userSittingRec.getNeckNum()+cursor.getInt(2));
            userSittingRec.setBackNum(userSittingRec.getBackNum()+cursor.getInt(3));
            userSittingRec.setSHLDRNum(userSittingRec.getSHLDRNum()+cursor.getInt(4));
            userSittingRec.setLeftArmNum(userSittingRec.getLeftArmNum()+cursor.getInt(5));
            userSittingRec.setRightArmNum(userSittingRec.getRightArmNum()+cursor.getInt(6));
            userSittingRec.setSitWellNum(userSittingRec.getSitWellNum()+cursor.getInt(7));
            userSittingRec.setSitPoorNum(userSittingRec.getSitPoorNum()+cursor.getInt(8));

            oldRecAcc= cursor.getFloat(9);
            oldRecDur= cursor.getFloat(12);
            totalDur= oldRecDur+userSittingRec.getDuration();
            newAur= userSittingRec.getSitAccuracy()*userSittingRec.getDuration()/totalDur + oldRecAcc*oldRecDur/totalDur;
            String finalrec = "finalred : " + "recordID = " + cursor.getInt(0) + ", userID = " + cursor.getString(1) +  ", neckNum = " + userSittingRec.getNeckNum() + ", backCount = " + userSittingRec.getBackNum() + ", SHLDRCount = " + userSittingRec.getSHLDRNum() +
                    " , leftArmNum = " + userSittingRec.getLeftArmNum() + ", rightArmNum = " + userSittingRec.getRightArmNum() + ", sitWellNum = " + userSittingRec.getSitWellNum() + ", sitPoorNum = " + userSittingRec.getSitPoorNum() +
                    " , accuracy = " + newAur + " , startTime = " + cursor.getString(10) + " , endTime = " + getFullDateTime()  + " , duration = " + totalDur;
            Log.e(TAG, oldrec);
            Log.e(TAG, newrec);
            Log.e(TAG, finalrec);
            this.updateOneRow(cursor.getInt(0),cursor.getString(1),userSittingRec.getNeckNum(), userSittingRec.getBackNum(), userSittingRec.getSHLDRNum(),userSittingRec.getLeftArmNum(), userSittingRec.getRightArmNum(), userSittingRec.getSitWellNum(), userSittingRec.getSitPoorNum() ,newAur,cursor.getString(10), getFullDateTime(), totalDur);
        }
        this.updateUserProgress();
    }

    //need to modify since the new col dateSetString
    private void updateUserProgress(){
        try{
            Log.e(TAG," check user :" + userSittingRec.getUserID());
            boolean repeatedUser = checkRepeatedUser(userSittingRec.getUserID());

        if(!repeatedUser){ //insert new record
            Log.e(TAG,"checkRepeateduser==false" );
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBConstant.userID_col, userSittingRec.getUserID());
            values.put(DBConstant.progDaysLeft_col, default_days-1);//****
            dateSet.clear();
            Log.e(TAG2,"set.size() = "+ dateSet.size());
            for (String s : dateSet) {
                Log.e(TAG2, "new one , DATE ONLY " + s);
            }
            Log.e(TAG,"calProgStatus :" + calProgStatus(default_days-1));
            values.put(DBConstant.progStatus_col, calProgStatus(default_days-1));
            values.put(DBConstant.programRepeatedTimes_col,ProgramRepeatedTimes );
            values.put(DBConstant.datesString_col, getDateOnly()); //need to do checking
            db.insert(DBConstant.DB2_NAME, null, values);
            db.close();
            Log.e(TAG," check user inserted the new record" );
        }else{
            //check when to update the DaysLeft by using the date
            Log.e(TAG,"checkRepeateduser==true" );
            Cursor cursor = findRepeatedRow(userSittingRec.getUserID());
            cursor.moveToNext();
            Log.e(TAG, "cursor.getString(0) = "+cursor.getString(0));
            Log.e(TAG, "cursor.getInt(1) = "+cursor.getInt(1));
            Log.e(TAG, "cursor.getFloat(2) = "+cursor.getFloat(2));
            Log.e(TAG, "cursor.getInt(3) = "+cursor.getInt(3));
            Log.e(TAG, "cursor.getInt(3) = "+cursor.getString(4));

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            //reset the progDaysLeft column to default 63
            if(cursor.getInt(1)==0){ //check if progDaysLeft ==0
                values.put(DBConstant.progDaysLeft_col, default_days-1);
                values.put(DBConstant.programRepeatedTimes_col, cursor.getInt(3)+1);
                values.put(DBConstant.progStatus_col,  calProgStatus(default_days-1));
                values.put(DBConstant.datesString_col, ""); //reset the string to ""

                for (String s : dateSet) {
                    Log.e(TAG, "progDaysLeft ==0 REMOVED and added the new one, DATE ONLY" + s);
                }
            }else if(cursor.getInt(1)>0){ //when progDaysLeft column > 0
                String datesString = cursor.getString(4);
                String [] strArr = datesString.split(",");
                dateSet.clear();
                for(int i=0 ;i<strArr.length;i++){
                    dateSet.add(strArr[i]);
                }

                String nowDate = getDateOnly();
                Log.e(TAG2, ""+nowDate);
                Log.e(TAG2, "SET CONTAINS DATE "+ dateSet.contains(nowDate) + " DATE: " + nowDate);
                Log.e(TAG2,"set.size() = "+ dateSet.size()+ " set.isEmpty() = "+ dateSet.isEmpty());
                for(String s: dateSet)
                Log.e(TAG2, "set elements:"+s);

                //update datesString
                if (!dateSet.contains(nowDate)) { //newDate
                    Log.e(TAG2, "set does not contain the repeated element");
                    values.put(DBConstant.progDaysLeft_col, cursor.getInt(1)-1);
                    float a = (float)(cursor.getInt(1));
                    values.put(DBConstant.progStatus_col,  calProgStatus(a-1));
                    values.put(DBConstant.datesString_col, cursor.getString(4)+","+nowDate);
                    dateSet.add(nowDate);
                } else {//sameDate
                    Log.e(TAG2, "set contains the repeated element");
                    values.put(DBConstant.progDaysLeft_col, cursor.getInt(1));
                    float a = (float)(cursor.getInt(1));
                    values.put(DBConstant.progStatus_col,  calProgStatus(a) );
                    values.put(DBConstant.datesString_col, cursor.getString(4));
                }
                values.put(DBConstant.programRepeatedTimes_col, cursor.getInt(3));
                for (String s : dateSet) {
                    Log.e(TAG, "DATE ONLY" + s);
                }
            }
            db.update(DBConstant.DB2_NAME, values, DBConstant.userID_col+"=?", new String[]{userSittingRec.getUserID()});
            db.close();
            Log.e(TAG," check user updated the record" );

            for (String s : dateSet) {
                Log.e(TAG, "DATE ONLY" + s);
            }
        }
        }catch (Exception e){ Log.e(TAG, e.getMessage()); }
    }

    //***notes: userSittingRec Also needs to add repeatNum to return related sitting rec to plot the linechart and pie chart
    public void insertExerciseRec(String exerciseType){
        //check if the user record already exists
        //if yes retreive the rec and update
        // if no then create a new Rec
        //then update UserProgress record especially check the Stringdate in UserProgress
        //the problem is when to reset the relax count and strength count***
        //exercise rec
        setProgRepeatedTimeInExRec();
        boolean checkRepeatedEx = checkRepeatedExRec();  //sth wrong here****

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if(!checkRepeatedEx){// add new Exercise Record
            Log.e("!checkRepeatedEx start","add new Exercise Record");
            values.put(DBConstant.userID_col, userID);
            values.put(DBConstant.strengthExerciseCount_col, userExerciseRec.getStrengthExerciseCount());
            Log.e("userExerciseRec.getExerciseCount()) new ex rec", ""+userExerciseRec.getStrengthExerciseCount());
            values.put(DBConstant.relaxExerciseCount_col, userExerciseRec.getRelaxCount());
            values.put(DBConstant.programRepeatedTimes_col, userExerciseRec.getProgRepeatedTimes());
            db.insert(DBConstant.DB3_NAME, null, values);
            Log.e("!checkRepeatedEx End","add new Exercise Record");
        }else{ // update the existed exercise record
            Log.e("checkRepeatedEx",""+checkRepeatedEx);
            Log.e("checkRepeatedEx","update the existed Exercise Record");
            Cursor cursor = findRepeatedRowInEx(userExerciseRec.getUserID());//***
            Log.e("findRepeatedRowInEx cursor", ""+cursor.getCount());

            cursor.moveToNext();
            Log.e("exercise cursor content",""+cursor.getInt(0)+", "+cursor.getString(1) +", "+ cursor.getInt(2)+", "+ cursor.getInt(3)+", "+ cursor.getInt(4));
            if(exerciseType.equals("strength")){
                values.put(DBConstant.strengthExerciseCount_col, cursor.getInt(2)+1);
                values.put(DBConstant.relaxExerciseCount_col, cursor.getInt(3));
                Log.e("strength userExerciseRec.getExerciseCount())", ""+cursor.getInt(2)+1);
            }else if(exerciseType.equals("relax")){
                values.put(DBConstant.strengthExerciseCount_col, cursor.getInt(2));
                values.put(DBConstant.relaxExerciseCount_col, cursor.getInt(3)+1);
                Log.e("relax userExerciseRec.getExerciseCount())", ""+cursor.getInt(2));
            }
            values.put(DBConstant.programRepeatedTimes_col, userExerciseRec.getProgRepeatedTimes());

            Log.e("test1",""+userExerciseRec.getUserID());
            Log.e("test2",""+userExerciseRec.getStrengthExerciseCount());
            Log.e("test3",""+userExerciseRec.getRelaxCount());
            db.update(DBConstant.DB3_NAME, values, DBConstant.exRecID_col+"=?", new String[]{""+cursor.getInt(0)});//***
        }
        this.updateUserProgress();
    }

    private Cursor findRepeatedRowInEx(String userID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from "+ DBConstant.DB3_NAME + " uxr," + DBConstant.DB2_NAME + " up" +" WHERE uxr.userID ="+ "'" + userID + "'" + " AND "
                + "uxr.userID=up.userID"+" AND " + "uxr.ProgramRepeatedTimes=up.ProgramRepeatedTimes" , null);
        Log.e("end of findRepeatedRowInEx()", ""+"c count = " + c.getCount());
        return c;
    }

    private boolean checkRepeatedExRec(){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("Select * from " + DBConstant.DB3_NAME + " WHERE userID =" + "'" + userID + "'", null);
            if(c.getCount()==0)
                return false;
            if (c.getCount()==1)
                return true;
        }catch (Exception e){
            return false;
        }
        return false;
    }


    private void setProgRepeatedTimeInExRec(){
        boolean progTimeCreated = checkProgRepeatedTimeExist();
        Log.e("progTimeCreated = ",""+progTimeCreated);
        if(!progTimeCreated){
            userExerciseRec.setProgRepeatedTimes(0);
            Log.e("!progTimeCreated",""+userExerciseRec.getProgRepeatedTimes());
        }else{
            Cursor c = getProgRepeatedTimeCursor();
            Log.e("c.getCount()",""+c.getCount());
            c.moveToNext();
            if(c.getCount()==1){
                Log.e("c.getCount()",""+c.getCount());
                userExerciseRec.setProgRepeatedTimes(c.getInt(0));
                Log.e("progTimeCreated",""+userExerciseRec.getProgRepeatedTimes());
            }
        }
        Log.e("End of setProgRepeatedTimeInExRec","");
    }


    private void setProgRepeatedTimeInSitRec(){
        boolean progTimeCreated = checkProgRepeatedTimeExist();
        if(!progTimeCreated)
            userSittingRec.setProgramRepeatedTimes(0);
        if(progTimeCreated){
            Cursor c = getProgRepeatedTimeCursor();
            c.moveToNext();
            if(c.getCount()==1){
                userSittingRec.setProgramRepeatedTimes(c.getInt(0));
            }
        }
    }

    public Cursor checkRepeatedUserInProgress(){
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("Select * from "+ DBConstant.DB2_NAME + " WHERE userID = " + "'"+userID+"'", null);
    }

    private boolean checkProgRepeatedTimeExist(){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("Select " + DBConstant.programRepeatedTimes_col + " from " + DBConstant.DB2_NAME + " WHERE userID =" + "'" + userID + "'", null);
            db.close();
            Log.e("checkProgRepeatedTimeExist()", ""+c.getCount());
            if(c.getCount()==0)
                return false;
            if(c.getCount()==1)
                return true;

        }catch (Exception e){
            return false;
        }
        return false;
    }

    public Cursor getProgRepeatedTimeCursor(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select " + DBConstant.programRepeatedTimes_col + " from " + DBConstant.DB2_NAME + " WHERE userID =" + "'" + userID + "'", null);
        return c;
    }

    public Cursor getSelectedQuery(){
        Cursor cursor = getAllSittingData();
        int recCount  = cursor.getCount();
        if(recCount<7){
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " WHERE userID = '"+ userID +"'" + " ORDER BY " + DBConstant.recordID_col  +" DESC ", null);
            return cursor;
        }else if(recCount/7==1){//print 最近既7天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " WHERE userID = '"+ userID +"'" + " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 7", null);
            return cursor;
        }else if(recCount/7==2 ){ //print 最近既14天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " WHERE userID = '"+ userID +"'" + " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 14", null);
            return cursor;
        }else if(recCount/7>=3){//print 最近既21天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " WHERE userID = '"+ userID +"'"+ " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 21", null);
            return cursor;
        }
        return null;
    }

    public Cursor getSelectedQuerySitRecs(){
        Cursor cursor = getAllSittingData();
        int recCount = cursor.getCount();
        if(recCount<7){
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME +" ur," + " "+ DBConstant.DB2_NAME + " up " + " WHERE ur.userID = '"+ userID +"'" +
                    " AND " + "ur.userID=up.userID" + " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" +" ORDER BY " + DBConstant.recordID_col  +" DESC ", null);
            return cursor;
        }else if(recCount/7==1){//print 最近既7天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select * from " +" from " + DBConstant.DB_NAME +" ur," + " "+ DBConstant.DB2_NAME + " up " + " WHERE ur.userID = '"+ userID +"'" +
                    " AND " + "ur.userID=up.userID" + " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" + " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 7", null);
            return cursor;
        }else if(recCount/7==2 ){ //print 最近既14天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME +" ur," + " "+ DBConstant.DB2_NAME + " up " + " WHERE ur.userID = '"+ userID +"'" +
                    " AND " + "ur.userID=up.userID" + " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" + " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 14", null);
            return cursor;
        }else if(recCount/7>=3){//print 最近既21天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME +" ur," + " "+ DBConstant.DB2_NAME + " up " + " WHERE ur.userID = '"+ userID +"'"+
                    " AND " + "ur.userID=up.userID" + " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" + " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 21", null);
            return cursor;
        }

        return null;
    }

    public Cursor getSelectedQuerySitAccuracy(){
        Cursor cursor = getAllSittingData();
        int recCount = cursor.getCount();
        if(recCount<7){
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select "+ DBConstant.sit_accuracy_col +" from " + DBConstant.DB_NAME +" ur," + " "+ DBConstant.DB2_NAME + " up " + " WHERE ur.userID = '"+ userID +"'" +
                   " AND " + "ur.userID=up.userID" + " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" +" ORDER BY " + DBConstant.recordID_col  +" DESC ", null);
            return cursor;
        }else if(recCount/7==1){//print 最近既7天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select "+ DBConstant.sit_accuracy_col +" from " + DBConstant.DB_NAME +" ur," + " "+ DBConstant.DB2_NAME + " up " + " WHERE ur.userID = '"+ userID +"'" +
                    " AND " + "ur.userID=up.userID" + " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" + " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 7", null);
            return cursor;
        }else if(recCount/7==2 ){ //print 最近既14天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select "+ DBConstant.sit_accuracy_col +" from " + DBConstant.DB_NAME +" ur," + " "+ DBConstant.DB2_NAME + " up " + " WHERE ur.userID = '"+ userID +"'" +
                    " AND " + "ur.userID=up.userID" + " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" + " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 14", null);
            return cursor;
        }else if(recCount/7>=3){//print 最近既21天
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery("Select "+ DBConstant.sit_accuracy_col +" from " + DBConstant.DB_NAME +" ur," + " "+ DBConstant.DB2_NAME + " up " + " WHERE ur.userID = '"+ userID +"'"+
                    " AND " + "ur.userID=up.userID" + " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" + " ORDER BY " + DBConstant.recordID_col  +" DESC LIMIT 21", null);
            return cursor;
        }

        return null;
    }

    public Cursor getAllDates(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select "+ DBConstant.datesString_col + " from " + DBConstant.DB2_NAME + " WHERE userID =" + "'"+ userID + "'", null);
        return c;
    }

    public Cursor getTheLatestSittingRecDate(){
        String date = getDateOnly();
        SQLiteDatabase db = this.getReadableDatabase();
        //this is for demo purpose
        return db.rawQuery("Select " + DBConstant.endTime_col + " from " + DBConstant.DB_NAME + " ur," + DBConstant.DB2_NAME + " up " + " WHERE ur.ProgramRepeatedTimes = up.ProgramRepeatedTimes " +
                 " AND "+ DBConstant.endTime_col + " LIKE '20%'" + " ORDER BY " + DBConstant.recordID_col + " DESC LIMIT 1", null);
        //this is for real time
        /*return db.rawQuery("Select " + DBConstant.endTime_col + " from " + DBConstant.DB_NAME + " ur," + DBConstant.DB2_NAME + " up " + " WHERE ur.ProgramRepeatedTimes = up.ProgramRepeatedTimes " +
                 " AND "+ DBConstant.endTime_col + " ORDER BY " + DBConstant.recordID_col + " DESC LIMIT 1", null);*/
    }

    public Cursor getTheLatestSittingRecData(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("Select " + DBConstant.neckCount +"," + DBConstant.backCount + "," + DBConstant.SHLDRCount +"," + DBConstant.LT_ARM_Count + "," +DBConstant.RT_ARM_Count+ "," +DBConstant.sitWellCount+ "," +DBConstant.sitPoorCount
                +" from " + DBConstant.DB_NAME + " ur," + DBConstant.DB2_NAME + " up " + " WHERE ur.ProgramRepeatedTimes = up.ProgramRepeatedTimes " +
                " AND "+ DBConstant.endTime_col + " LIKE '20%'" + " ORDER BY " + DBConstant.recordID_col + " DESC LIMIT 1", null);
    }

    public Cursor getUserProgressStatus(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select "+DBConstant.progStatus_col+" from "+ DBConstant.DB2_NAME + " WHERE userID ="+ "'"+ userID +"'", null) ;
        return cursor;
    }

    public Cursor getUserProgress(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select "+DBConstant.progDaysLeft_col+" from "+ DBConstant.DB2_NAME + " WHERE userID ="+ "'"+ userID +"'", null) ;
        return cursor;
    }


    private float calProgStatus(float progDaysLeft) {
        Log.e(TAG, "numerator = " + (float)default_days+" - "+progDaysLeft);
            float numerator =(float)default_days-progDaysLeft;
            float denominator = (float)default_days;
        return numerator/denominator ;
    }


    public Cursor getSameDateRec(String date){ //search By startTime
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " where startTime LIKE '"+date+"%'", null);
        return cursor;
    }

    public void updateOneRow(int recordID, String userID, int neckNum, int backNum, int SHLDRNum, int leftArmNum, int rightArmNum, int sitWellNum, int sitPoorNum, float newAur, String startTime ,String endTime, float totalDur){

        String update_query = "UPDATE " + DBConstant.DB_NAME + " SET neckCount = " + neckNum + ", backCount = " + backNum + ", ShoulderCount = " + SHLDRNum
                + ", leftArmCount = " + leftArmNum + ", rightArmCount = " + rightArmNum + ", sitWellCount = " + sitWellNum + ", sitBadCount = " + sitPoorNum
                + ", sitAccuracy = " + newAur + ", startTime = " + startTime  +", endTime = " + endTime  + ", duration = " + totalDur + " WHERE recordID = " + recordID;

        Log.e(TAG, "updateOneRow scope "+update_query);
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.userID_col, userID);
        values.put(DBConstant.neckCount, neckNum);
        values.put(DBConstant.backCount, backNum);
        values.put(DBConstant.SHLDRCount, SHLDRNum);
        values.put(DBConstant.LT_ARM_Count, leftArmNum);
        values.put(DBConstant.RT_ARM_Count, rightArmNum);
        values.put(DBConstant.sitWellCount, sitWellNum);
        values.put(DBConstant.sitPoorCount, sitPoorNum);
        values.put(DBConstant.sit_accuracy_col, newAur);
        values.put(DBConstant.startTime_col, startTime);
        values.put(DBConstant.endTime_col, endTime);
        values.put(DBConstant.duration_col, totalDur);
        values.put(DBConstant.programRepeatedTimes_col, userSittingRec.getProgramRepeatedTimes());
        //db.update(DB_NAME, values, recordID_col+"="+recordID, null);
        db.update(DBConstant.DB_NAME, values, DBConstant.recordID_col+"= ?", new String[]{""+recordID});
        db.close();
    }

    private boolean checkRepeatedUser(String userID){
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            String select_query = "SELECT * from " + DBConstant.DB2_NAME + " WHERE " + DBConstant.userID_col
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
        return formatter1.format(new Date());
    }

    public String getDateOnly(){
        String nowDate= getFullDateTime();
        String [] arr = nowDate.split(" ");
        Log.e(TAG,"getDateOnly() = "+ arr[0]);
        return arr[0];
    }

    public void calAccuracy() {
        try {
            float numerator = (userSittingRec.getSitWellNum() + userSittingRec.getSitPoorNum()) * 5 - (userSittingRec.getNeckNum() + userSittingRec.getBackNum() + userSittingRec.getSHLDRNum() + userSittingRec.getLeftArmNum() + userSittingRec.getRightArmNum());
            float denominator = (userSittingRec.getSitWellNum() + userSittingRec.getSitPoorNum()) * 5;
            userSittingRec.setSitAccuracy(numerator / denominator *100);
            String calMsg = "numerator = " + numerator + ", denominator = " + denominator + ", sitAccuracy = " + userSittingRec.getSitAccuracy();
            Log.d(TAG, calMsg );
        }catch (Exception e){ e.printStackTrace();}
    }

    public void printDetails(){
        String str = " printDetails =  userID = " + userSittingRec.getUserID() + ", recordID = " +  userSittingRec.getRecordID() + ", neckNum = " + userSittingRec.getNeckNum() + ", backCount = " + userSittingRec.getBackNum() + ", SHLDRCount = " + userSittingRec.getSHLDRNum() +
                " , leftArmNum = " + userSittingRec.getLeftArmNum() + ", rightArmNum = " + userSittingRec.getRightArmNum() + ", sitWellNum = " + userSittingRec.getSitWellNum() + ", sitPoorNum = " + userSittingRec.getSitPoorNum() +
                " , duration = " + userSittingRec.getDuration() + " , accuracy = " + userSittingRec.getSitAccuracy() + " , startTime = " + userSittingRec.getStartTime() +" , endTime = " + userSittingRec.getEndTime() ;
        Log.d(TAG,str );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstant.DB_NAME);
        onCreate(db);
    }

    //***
    public Cursor getRecentUserExerciseData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from "+ DBConstant.DB3_NAME + " uxr," + DBConstant.DB2_NAME + " up" +" WHERE uxr.userID ="+ "'" + userID + "'" + " AND "
                + "uxr.userID=up.userID"+" AND " + "uxr.ProgramRepeatedTimes=up.ProgramRepeatedTimes" , null);
        Log.e("end of findRepeatedRowInEx()", ""+"c count = " + c.getCount());
        return c;
    }

    //called in GraphReportActivity to plot line graph
    public Cursor getAllSittingData() { //result ASC
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " WHERE userID = "+ "'" + userSittingRec.getUserID()+ "'", null);
        Cursor cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " ur," + DBConstant.DB2_NAME + " up" + " WHERE ur.userID = "+ "'" + userSittingRec.getUserID()+ "'" +
                " AND " + "ur.ProgramRepeatedTimes=up.ProgramRepeatedTimes" , null);
        return cursor;
    }

    //called in the ReportActivity , can be deleted
    public Cursor getAllSittingDataDESC(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " order by " + DBConstant.recordID_col + " DESC", null);
        return cursor;
    }

    public Cursor findRepeatedRow(String userID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DBConstant.DB2_NAME + " WHERE userID = " + "'"+ userID +"'", null);
        return cursor;
    }

    //need to modify
    public Cursor updateRepeatedRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DBConstant.DB_NAME + " WHERE userID = " + "'"+ userSittingRec.getUserID()  +"'", null);
        return cursor;
    }

    public Cursor getOneRowData(int recordID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+DBConstant.DB_NAME + " WHERE recordID = " + recordID, null);
        return cursor;
    }

    public boolean removeOneRow(int recordID){
        try{
            String delete_query = "DELETE FROM " + DBConstant.DB_NAME+ " WHERE recordID = " + recordID;
            SQLiteDatabase db= this.getWritableDatabase();
            db.execSQL(delete_query);
            db.close();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static UserSittingRecModel getUserSittingRec() {
        return userSittingRec;
    }

    public static UserExerciseRecModel getUserExerciseRec() {
        return userExerciseRec;
    }
}
