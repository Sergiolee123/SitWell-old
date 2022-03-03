package com.fyp.sitwell.muscleTraining;

import android.content.Context;
import android.util.Log;

import com.google.mlkit.vision.pose.Pose;

import java.lang.reflect.InvocationTargetException;

public class MuscleTrainingFactory {

    public static Object getMuscleTraining(Class<?extends MuscleTrainingInterface> mClass, Pose pose){
        Object object = null;
        try {
            object = mClass.getConstructors()[0].newInstance(pose);
        }catch (IllegalAccessException | InstantiationException | InvocationTargetException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            Log.e("MTFactory", mClass.getName() + ": Constructors error");
        }
        return object;
    }

}
