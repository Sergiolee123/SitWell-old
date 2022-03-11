package com.fyp.sitwell.muscleTraining;

import android.util.Log;

import com.google.mlkit.vision.pose.Pose;

import java.lang.reflect.InvocationTargetException;

public class MuscleTrainingFactory {
    public static MuscleTrainingInterface getMuscleTraining(Class<?> mClass, Pose pose){
        MuscleTrainingInterface t = null;
        try {
            t = (MuscleTrainingInterface) mClass.getConstructors()[0].newInstance(pose);
        }catch (IllegalAccessException | InstantiationException | InvocationTargetException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            Log.e("MTFactory", mClass.getName() + ": Constructors error");
        }
        return t;
    }
}
