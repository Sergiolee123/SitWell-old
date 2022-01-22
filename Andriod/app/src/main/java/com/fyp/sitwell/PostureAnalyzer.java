package com.fyp.sitwell;

import android.util.Log;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.List;

public class PostureAnalyzer {

    private Pose pose;

    public PostureAnalyzer (Pose pose){
        this.pose = pose;
        getPose();

    }

    protected void getPose(){

        List<PoseLandmark> allPoseLandmarks = pose.getAllPoseLandmarks();
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        String message;
        if(leftShoulder != null){
            message = "x: " + leftShoulder.getPosition().x+" y: "+ leftShoulder.getPosition().y;
        }else {
            message = "empty";
        }
        Log.e("PA",message);

    }

}
