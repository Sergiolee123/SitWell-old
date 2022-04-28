package com.fyp.sitwell.muscleTraining;

public interface MuscleTrainingInterface {
    Boolean isPrepare();
    Boolean isReady();
    Boolean isHalf();
    Boolean isFinished();
    Boolean isNextSide(int count);
    Boolean isEnd();
    String getInstruction();


}
