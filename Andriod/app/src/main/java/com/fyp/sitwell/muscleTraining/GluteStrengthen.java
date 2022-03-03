package com.fyp.sitwell.muscleTraining;

import android.graphics.PointF;
import android.util.Log;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;

public class GluteStrengthen implements MuscleTrainingInterface {
    private Pose pose;
    private String side;
    private String hipsAngle, HipKeenAngle, wiseEar;

    public GluteStrengthen(Pose pose){
        this.pose = pose;
        this.side = "right";
    }


    private double getHipKeenAngle(){
        /*
        if(side.equals("rightFoot")){
            return getAngle(pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE));
        }else {
            return getAngle(pose.getPoseLandmark(PoseLandmark.RIGHT_HIP),
                    pose.getPoseLandmark(PoseLandmark.LEFT_HIP),
                    pose.getPoseLandmark(PoseLandmark.LEFT_KNEE));
        }
        */
        if(side.equals("rightFoot")){
            return Math.abs(angleOfTwoPoint(pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)));
        }else {
            return Math.abs(angleOfTwoPoint(pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE),
                    pose.getPoseLandmark(PoseLandmark.LEFT_HIP)));
        }

    }

    private double angleOfTwoPoint(PoseLandmark p1,PoseLandmark p2){
        return Math.toDegrees(Math.atan2(p1.getPosition().y-p2.getPosition().y,
                p1.getPosition().x-p2.getPosition().x));
    }

    public double lengthOfTwoPoint(PointF p1, PointF p2){
        return Math.hypot(Math.abs(p2.y - p1.y), Math.abs(p2.x - p1.x));
    }

    public Boolean isPrepare(){
        ArrayList<Float> inFrameLikelihoods = new ArrayList<>();
        for(PoseLandmark p : pose.getAllPoseLandmarks()){
            inFrameLikelihoods.add(p.getInFrameLikelihood());
        }
        if(inFrameLikelihoods.size() < 32)
            return false;
        for(Float f : inFrameLikelihoods){
            if(f < 0.5){
                return false;
            }
        }

        return true;
    }

    public Boolean isReady(){
        PointF wrist = null, ear = null;

        if(side.equals("rightFoot")){
            wrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST).getPosition();
            ear = pose.getPoseLandmark(PoseLandmark.LEFT_EAR).getPosition();
        }else if(side.equals("leftFoot")){
            wrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST).getPosition();
            ear = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR).getPosition();
        }

        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        try {
            wiseEar = lengthOfTwoPoint(wrist, ear) + "";
        }catch (Exception e){
            return false;
        }
        double hipsAngle = Math.abs(angleOfTwoPoint(leftHip,rightHip)) - 90;
        Log.e("LLLF","wrist " + lengthOfTwoPoint(wrist, ear));

        this.HipKeenAngle =getHipKeenAngle()+"";
        this.hipsAngle = hipsAngle+"";
        Log.e("LLLF","Ankle " + HipKeenAngle+" Hip"+ hipsAngle);

        return lengthOfTwoPoint(wrist, ear) < 250 && hipsAngle < 30 && hipsAngle > -30;
    }

    public Boolean isHalf(){
        try{
            double angle = getHipKeenAngle();
            if(angle > 22){
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isFinished(){
        try{
            double angle = getHipKeenAngle();
            if(angle < 10){
                return true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }
}
