package com.fyp.sitwell.muscleTraining;

import android.graphics.PointF;

import com.google.mlkit.vision.pose.PoseLandmark;

public class NeckMuscleRelax implements MuscleTrainingInterface{


    private double angleOfTwoPoint(PoseLandmark p1, PoseLandmark p2){
        return Math.toDegrees(Math.atan2(p1.getPosition().y-p2.getPosition().y,
                p1.getPosition().x-p2.getPosition().x));
    }

    public double lengthOfTwoPoint(PointF p1, PointF p2){
        return Math.hypot(Math.abs(p2.y - p1.y), Math.abs(p2.x - p1.x));
    }

    @Override
    public Boolean isPrepare() {

        return null;
    }

    @Override
    public Boolean isReady() {
        return null;
    }

    @Override
    public Boolean isHalf() {
        return null;
    }

    @Override
    public Boolean isFinished() {
        return null;
    }

    @Override
    public Boolean isNextSide(int count) {
        return null;
    }

    @Override
    public Boolean isEnd() {
        return null;
    }

    @Override
    public String getInstruction() {
        return null;
    }
}
