package com.fyp.sitwell.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.fyp.sitwell.MainActivity;
import com.fyp.sitwell.R;
import com.fyp.sitwell.muscleTraining.MuscleRelaxActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_FLAG = 3;
    private static final String CHANNEL_ID = "sitwell";
    public final static String TIMER_ACTION_REPEATING = "repeat_relax";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmR", "Intent come");
        if(intent.getAction().equals(TIMER_ACTION_REPEATING)){
            Log.e("AlarmR", "Received");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID, "SitWell", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = context.getSystemService(NotificationManager.class);
                assert manager != null;
                manager.createNotificationChannel(channel);
            }
            Intent intent1 = new Intent(context, MuscleRelaxActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("SitWell！")
                    .setContentText("Please open the app to start your muscle relax")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(1, builder.build());
        }

    }
}