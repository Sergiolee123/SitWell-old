package com.fyp.sitwell;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.Ringtone;
import android.util.Log;
import android.widget.Toast;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;

import java.util.List;

public class SittingPostureAnalyzer {

    private Pose pose;
    private Context context;
    private static double leftShoulderZ, rightShoulderZ;

    public SittingPostureAnalyzer(Pose pose, Context context){
        this.pose = pose;
        this.context = context;
    }

    public boolean setLeftShoulderZ() {
        try {
            leftShoulderZ =
                    pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER).getPosition3D().getZ();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean setRightShoulderZ() {
        try {
            rightShoulderZ =
                    pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER).getPosition3D().getZ();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    protected PointF midPoint(PointF p1, PointF p2){
        return new PointF((p1.x+p2.x)/2, (p1.y+p2.y)/2);
    }

    protected double angleOfTwoPoint(PoseLandmark p1,PoseLandmark p2){
        return Math.abs(Math.toDegrees(Math.atan2(p1.getPosition().y-p2.getPosition().y,
                p1.getPosition().x-p2.getPosition().x)));
    }

    protected double angleOfTwoPoint(PointF p1,PointF p2){
        return Math.abs(Math.toDegrees(Math.atan2(p1.y-p2.y,
                p1.x-p2.x)));
    }

    protected Boolean isShoulderAlignment(){
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        try {
            String message = "Left x: " + leftShoulder.getPosition().x+" y: "+ leftShoulder.getPosition().y;
            String message1 = "right x: " + rightShoulder.getPosition().x+" y: "+ rightShoulder.getPosition().y;
            double angle = angleOfTwoPoint(leftShoulder,rightShoulder);
            Log.e("PA",message);
            Log.e("PA",message1);
            Log.e("PA",angle+"");
            if(angle>5){
                return true;
            }
        } catch (NullPointerException p){
            Toast.makeText(context, "No posture detected", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    protected Boolean isRightArmCorrect(){
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
        try {
            String message = "Left x: " + rightShoulder.getPosition().x+" y: "+ rightShoulder.getPosition().y;
            String message1 = "right x: " + rightElbow.getPosition().x+" y: "+ rightElbow.getPosition().y;
            double angle = Math.abs(angleOfTwoPoint(rightShoulder,rightElbow) - 90);
            Log.e("PA",message);
            Log.e("PA",message1);
            Log.e("PA",angle+"");
            if(angle>15){
                return true;
            }
        } catch (NullPointerException p){
            Toast.makeText(context, "No posture detected", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    protected Boolean isLeftArmCorrect(){
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        try {
            String message = "Left x: " + leftShoulder.getPosition().x+" y: "+ leftShoulder.getPosition().y;
            String message1 = "right x: " + leftElbow.getPosition().x+" y: "+ leftElbow.getPosition().y;
            double angle = Math.abs(angleOfTwoPoint(leftShoulder,leftElbow) - 90);
            Log.e("PA",message);
            Log.e("PA",message1);
            Log.e("PA",angle+"");
            if(angle>15){
                return true;
            }
        } catch (NullPointerException p){
            Toast.makeText(context, "No posture detected", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    protected Boolean isNeckLateralBend(){
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark nose = pose.getPoseLandmark(PoseLandmark.NOSE);
        PoseLandmark leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
        PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);

        try {
            PointF midPoint1, midPoint2;
            if(nose.getInFrameLikelihood()>0.7){ //if the nose is high confidence
                midPoint1 = new PointF(nose.getPosition().x, nose.getPosition().y);
            }else{
                midPoint1 = midPoint(leftEye.getPosition(),rightEye.getPosition());
            }
            midPoint2 = midPoint(leftShoulder.getPosition(), rightShoulder.getPosition());

            String message = "Left x: " + midPoint1.x+" y: "+ midPoint1.y;
            String message1 = "right x: " + midPoint2.x+" y: "+ midPoint2.y;
            double angle = Math.abs(angleOfTwoPoint(midPoint1,midPoint2)-90);
            Log.e("PA",message);
            Log.e("PA",message1);
            Log.e("PA",angle+"");
            if(angle>10){
                return true;
            }
        } catch (NullPointerException p){
            Toast.makeText(context, "No posture detected", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    protected Boolean isBackUpStraight(){
        Float leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER).getPosition3D().getZ();
        Float rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER).getPosition3D().getZ();
        try {
            String message = "Left z: " + leftShoulder;
            String message1 = "right z: " + rightShoulder;

            Log.e("PA",message);
            Log.e("PA",message1);
            Log.e("PA","OLeft" + leftShoulderZ + "    ORight" + rightShoulderZ);

            if((leftShoulder > leftShoulderZ+150 || leftShoulder < leftShoulderZ-150)
                    || (rightShoulder > rightShoulderZ+150 || rightShoulder < rightShoulderZ-150)){
                return true;
            }
        } catch (NullPointerException p){
            Toast.makeText(context, "No posture detected", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
