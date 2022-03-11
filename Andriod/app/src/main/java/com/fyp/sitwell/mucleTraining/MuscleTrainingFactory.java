package com.fyp.sitwell.mucleTraining;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

public class MuscleTrainingFactory {

    public static Object getMuscleTraining(Class<?extends MuscleTrainingInterface> mClass, Context context){
        Object object = null;
        try {
            object = mClass.getConstructors()[0].newInstance(context);
        }catch (IllegalAccessException | InstantiationException | InvocationTargetException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            Log.e("MTFactory", mClass.getName() + ": Constructors error");
        }
        return object;
    }

}
