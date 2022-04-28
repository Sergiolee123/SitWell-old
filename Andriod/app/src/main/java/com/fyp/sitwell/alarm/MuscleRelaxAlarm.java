package com.fyp.sitwell.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class MuscleRelaxAlarm {

    public final static String TIMER_ACTION_REPEATING = "repeat_relax";


    public static void setAlarm(Context context, long time){
        //Alarm id for each alarm
        int alarmId = LocalStorage.getId(context);
        Log.e("setAlarm", alarmId+"");
        LocalStorage.setId(context, alarmId);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(TIMER_ACTION_REPEATING);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.e("Alarm", time+"");
        Log.e("Alarm", "Set Alarm" + new Date(time));
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, sender);
    }

    public static void cancelAlarm(Context context){
        int alarmId = LocalStorage.getId(context);
        Log.e("clearAlarm", alarmId+"");
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.setAction(TIMER_ACTION_REPEATING);
        for(int i = alarmId; i>= 0; i--){
            PendingIntent sender = PendingIntent.getBroadcast(context, i, myIntent,0);
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(sender);
        }
        LocalStorage.resetId(context);
    }


}
