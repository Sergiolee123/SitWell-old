package com.fyp.sitwell.muscleTraining;

import com.google.mlkit.vision.pose.Pose;

import java.util.concurrent.atomic.AtomicInteger;

public class WristMuscleRelax implements MuscleTrainingInterface{

    private Pose pose;
    private static String[] sides;
    private static AtomicInteger sidesIndex;
    private String side;

    static {
        sidesIndex = new AtomicInteger(0);
        sides = new String[]{"right foot","left foot"};
    }

    public WristMuscleRelax(Pose pose){
        this.pose = pose;
        this.side = sides[sidesIndex.get()];
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
