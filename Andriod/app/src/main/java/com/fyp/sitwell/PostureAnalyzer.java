package com.fyp.sitwell;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.List;

public class PostureAnalyzer {

    private Pose pose;
    private Context context;
    private Ringtone r;

    public PostureAnalyzer (Pose pose, Context context, Ringtone r){
        this.pose = pose;
        this.context = context;
        this.r = r;
        getPose();

    }

    protected synchronized void play(){
        if(!r.isPlaying()){
            r.play();
        }
    }

    protected synchronized void stopPlay(){
        if(r.isPlaying()){
            r.stop();
        }
    }

    protected double calAngle(PoseLandmark p1,PoseLandmark p2){
        return Math.abs(Math.toDegrees(Math.atan2(p1.getPosition().y-p2.getPosition().y,
                p1.getPosition().x-p2.getPosition().x)));
    }

    protected void getPose(){

        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        String message, message1;
        double shoulder;
        try {
            message = "Left x: " + leftShoulder.getPosition().x+" y: "+ leftShoulder.getPosition().y;
            message1 = "right x: " + rightShoulder.getPosition().x+" y: "+ rightShoulder.getPosition().y;
            shoulder = calAngle(leftShoulder,rightShoulder);
            Log.e("PA",message);
            Log.e("PA",message1);
            Log.e("PA",shoulder+"");
            if(shoulder>5){
                Toast.makeText(context, "You are sitting incorrectly", Toast.LENGTH_LONG).show();
                try {
                    play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                stopPlay();
            }
        } catch (NullPointerException p){
            Toast.makeText(context, "No posture detected", Toast.LENGTH_SHORT).show();
            stopPlay();
        }
        


    }

}
