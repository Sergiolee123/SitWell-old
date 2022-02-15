package com.fyp.sitwell.mucleTraining;

import android.graphics.PointF;
import android.util.Log;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;

public class TrainingPostureAnalyer {
    private Pose pose;
    private String side;
    private String hipsAngle, ankleLength, wiseEar;

    public TrainingPostureAnalyer(Pose pose, String side){
        this.pose = pose;
        this.side = side;
    }

    protected double angleOfTwoPoint(PoseLandmark p1,PoseLandmark p2){
        return Math.abs(Math.toDegrees(Math.atan2(p1.getPosition().y-p2.getPosition().y,
                p1.getPosition().x-p2.getPosition().x)));
    }

    public double lengthOfTwoPoint(PointF p1, PointF p2){
        return Math.hypot(Math.abs(p2.y - p1.y), Math.abs(p2.x - p1.x));
    }

    public Boolean isPrepare(){
        ArrayList<Float> inFrameLikelihoods = new ArrayList<Float>();
        for(PoseLandmark p : pose.getAllPoseLandmarks()){
            inFrameLikelihoods.add(p.getInFrameLikelihood());
        }
        return !inFrameLikelihoods.isEmpty();
    }

    public Boolean isReady(){
        PointF wrist = null, ear = null;
        try{
        if(side.equals("rightFoot")){
            wrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST).getPosition();
            ear = pose.getPoseLandmark(PoseLandmark.LEFT_EAR).getPosition();
        }else if(side.equals("leftFoot")){
            wrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST).getPosition();
            ear = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR).getPosition();
        }

        }catch (Exception e){

        }

        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);

        wiseEar = lengthOfTwoPoint(wrist, ear)+"";

        double hipsAngle = Math.abs(angleOfTwoPoint(leftHip,rightHip) - 90);
        Log.e("LLLF","wrist " + lengthOfTwoPoint(wrist, ear));
        double length = lengthOfTwoPoint(pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE).getPosition(),
                pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE).getPosition());

        this.ankleLength =length+"";
                this.hipsAngle = hipsAngle+"";
        Log.e("LLLF","Ankle " + length+" Hip"+ hipsAngle);
        return lengthOfTwoPoint(wrist, ear) < 50 && hipsAngle < 20;
    }

    public Boolean isUp(){
        try{
            double length = lengthOfTwoPoint(pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE).getPosition(),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE).getPosition());
            if(length > 100){
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isDown(){
        try{
            double length = lengthOfTwoPoint(pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE).getPosition(),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE).getPosition());
            if(length < 60){
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public String debug(){
        return "Hips " + hipsAngle + "\n wiseEar " + wiseEar + "\n ankleLength" + ankleLength;
    }



}
