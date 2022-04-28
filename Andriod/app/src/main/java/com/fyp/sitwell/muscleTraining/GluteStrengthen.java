package com.fyp.sitwell.muscleTraining;

import android.graphics.PointF;
import android.util.Log;

import com.fyp.sitwell.R;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GluteStrengthen implements MuscleTrainingInterface {
    private Pose pose;
    private static String[] sides;
    private static AtomicInteger sidesIndex;
    private String side;

    static {
        sidesIndex = new AtomicInteger(0);
        sides = new String[]{"right foot","left foot"};
    }

    public GluteStrengthen(Pose pose){
        this.pose = pose;
        this.side = sides[sidesIndex.get()];
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
        if(sidesIndex.get() == 0){
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
        //Make sure every key points can be relied
        for(Float f : inFrameLikelihoods){
            if(f < 0.5){
                return false;
            }
        }

        return true;
    }

    public Boolean isReady(){
        PointF wrist = null, ear = null;

        if(sidesIndex.get() == 0){
            wrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST).getPosition();
            ear = pose.getPoseLandmark(PoseLandmark.LEFT_EAR).getPosition();
        }else if(sidesIndex.get() == 1){
            wrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST).getPosition();
            ear = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR).getPosition();
        }

        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        double hipsAngle, length;
        try{
            hipsAngle = Math.abs(angleOfTwoPoint(leftHip,rightHip)) - 90;
            length = lengthOfTwoPoint(wrist, ear);
        }catch (NullPointerException e){
            return false;
        }
        Log.e("LLLF","wrist " + length);
        Log.e("LLLF", "angle" + hipsAngle);
        return length < 250 && hipsAngle < 30 && hipsAngle > -30;
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

    public Boolean isNextSide(int count){
        if(count >= 10){
            sidesIndex.incrementAndGet();
            return true;
        }
        return false;
    }

    public Boolean isEnd(){
        return sidesIndex.get() > sides.length;
    }

    public String getInstruction() {
        return "Requirement for this training:\n" +
                "A place to lie down Lie on one side, keeping your lower leg slightly bent on the ground.\n" +
                "Engage your core by drawing your belly button in toward your spine.\n" +
                "Raise your top leg without moving the rest of your body. Hold for 2 seconds at the top. Repeat 10 times.";
    }
}
