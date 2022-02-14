package com.fyp.sitwell.mucleTraining;

import android.graphics.PointF;
import android.util.Log;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

public class TrainingPostureAnalyer {
    private Pose pose;
    private String side;

    public TrainingPostureAnalyer(Pose pose, String side){
        this.pose = pose;
        this.side = side;
    }

    protected double angleOfThreePoint(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result =
                Math.toDegrees(
                        Math.atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - Math.atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }

    public double lengthOfTwoPoint(PointF p1, PointF p2){
        return Math.hypot(Math.abs(p2.y - p1.y), Math.abs(p2.x - p1.x));
    }

    public Boolean isPrepare(){
        try{
            for(PoseLandmark p : pose.getAllPoseLandmarks()){
                if(p.getInFrameLikelihood()<0.7){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
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
            e.printStackTrace();
            return false;
        }
        Log.e("LLLF",lengthOfTwoPoint(wrist, ear)+"");
        return lengthOfTwoPoint(wrist, ear) < 30;
    }

    public Boolean isUp(){
        try{
            lengthOfTwoPoint(pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE).getPosition(),
                    pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE).getPosition());
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



}
