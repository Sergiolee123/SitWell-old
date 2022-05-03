package com.fyp.sitwell.habitCorrection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.fyp.sitwell.DBHandler;
import com.fyp.sitwell.LineChartReportActivity;
import com.fyp.sitwell.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;

import io.socket.client.Socket;


public class HabitCorrectionActivity extends FragmentActivity{

    private PoseDetector poseDetector;
    private Boolean setup, connected;
    private FragmentManager fragmentManager;
    private ConnectWebcamFragment connectWebcamFragment;
    private SetupPostureFragment setupPostureFragment;
    private HabitCorrectionFragment habitCorrectionFragment;
    private Socket socket;
    private Button endBtn;
    private DBHandler dbHandler;
    private String uid;
    private int alertTime, alertCounter;
    long startTime, endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_correction);
        endBtn= findViewById(R.id.endBtn);
        connectWebcamFragment = new ConnectWebcamFragment();
        setupPostureFragment = new SetupPostureFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.habit_fragment, connectWebcamFragment, "ConnectWebcam")
                .commit();

        setup = false;
        connected = false;
        alertTime = HabitLocalStorage.getId(this);
        alertCounter = 0;

        try {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){
            e.printStackTrace();
            this.finish();
        }

        AccuratePoseDetectorOptions options = new AccuratePoseDetectorOptions.Builder()
                .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                .build();

        poseDetector = PoseDetection.getClient(options);

        URI uri = URI.create("https://nodesitwell.herokuapp.com");
        socket = IO.socket(uri);

        initiateSocketConnection();
        dbHandler = new DBHandler(this);

        endBtn.setOnClickListener((view -> {
            EndBtnClick();
        }));
    }
    //need to add End detection button

    protected boolean isSetup(SittingPostureClassification s){

        String message = "";

        if(!s.isPrepare()) {
            Log.e("isPrepare", s.isPrepare()+"");
            setupPostureFragment.setText("Your body are not captured by the Webcam");
            return false;
        }

        if(s.isNeckLateralBend()) {
            message += "Your Neck is not straight\n";
        }
        if(s.isNotShoulderAlignment()) {
            message += "Your shoulder is not align\n";
        }
        if(s.isNotLeftArmCorrect()) {
            message += "Your left arm is in bad position\n";
        }
        if(s.isNotRightArmCorrect()) {
            message += "Your right arm is in bad position\n";
        }

        if(!message.equals("")){
            message += "Please make sure you are in a correct posture";
            setupPostureFragment.setText(message);
            return false;
        }

        return s.setLeftShoulderZ() && s.setRightShoulderZ();

    }

    protected void analyzePose(SittingPostureClassification s){

        String message = "";


        if(!s.isPrepare()) {
            habitCorrectionFragment.setTextView("Your body are not captured by the Webcam");
            return;
        }

        if(s.isNeckLateralBend()) {
            message += "Your Neck is not straight\n";
            dbHandler.getUserSittingRec().setNeckNum(dbHandler.getUserSittingRec().getNeckNum()+1);
        }

        if(s.isNotBackUpStraight()) {
            message += "Your Back is not straight\n";
            dbHandler.getUserSittingRec().setBackNum(dbHandler.getUserSittingRec().getBackNum()+1);
        }
        if(s.isNotShoulderAlignment()) {
            message += "Your shoulder is not align\n";
            dbHandler.getUserSittingRec().setSHLDRNum(dbHandler.getUserSittingRec().getSHLDRNum()+1);
        }
        if(s.isNotLeftArmCorrect()) {
            message += "Your left arm is in bad position\n";
            dbHandler.getUserSittingRec().setLeftArmNum(dbHandler.getUserSittingRec().getLeftArmNum()+1);
        }
        if(s.isNotRightArmCorrect()) {
            message += "Your right arm is in bad position\n";
            dbHandler.getUserSittingRec().setRightArmNum(dbHandler.getUserSittingRec().getRightArmNum()+1);
        }
        if(message.equals("")){
            habitCorrectionFragment.clearAll();
            dbHandler.getUserSittingRec().setSitWellNum(dbHandler.getUserSittingRec().getSitWellNum()+1);
        } else if(alertCounter < alertTime){
            alertCounter++;
        } else{
            habitCorrectionFragment.setTextView(message);
            habitCorrectionFragment.showCorrectPose();
            socket.emit("notification", uid, message + " Please refer to your correct posture");
            dbHandler.getUserSittingRec().setSitPoorNum(dbHandler.getUserSittingRec().getSitPoorNum()+1);
            alertCounter=0;
        }

    }

    public void setCorrectSitting(View v) {
        if(setupPostureFragment.getImage() != null)
            getPose(setupPostureFragment.getImage());
    }

    private boolean isConnected(String encodedImage){
        encodedImage = encodedImage.substring(encodedImage.indexOf(",")  + 1);
        byte[] imageByte = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        return bitmap != null;
    }

    private Bitmap decodeBase64(String encodedImage){
        encodedImage = encodedImage.substring(encodedImage.indexOf(",")  + 1);
        byte[] imageByte = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
    }

    private void getPose(String encodedImage){

        encodedImage = encodedImage.substring(encodedImage.indexOf(",")  + 1);
        byte[] imageByte = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        habitCorrectionFragment.setImageView(bitmap);
        Log.e("post","image");
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        poseDetector.process(image)
                .addOnSuccessListener(
                        (pose) -> {
                            Log.e("post","posta");
                            SittingPostureClassification s = new SittingPostureClassification(pose,this);
                            analyzePose(s);

                        })
                .addOnFailureListener(
                        Throwable::printStackTrace)
                .addOnCompleteListener(
                        (r) -> {}
                        );
    }

    protected void getPose(Bitmap bitmap){
        Log.e("post","image");
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        poseDetector.process(image)
                .addOnSuccessListener(
                        (pose) -> {
                            Log.e("post","posts");
                            SittingPostureClassification s = new SittingPostureClassification(pose,this);
                            setup = isSetup(s);
                            if(setup){
                                habitCorrectionFragment = new HabitCorrectionFragment(bitmap);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.habit_fragment, habitCorrectionFragment, "habit")
                                        .commit();
                                endBtn.setVisibility(View.VISIBLE);
                                startTime = System.currentTimeMillis();
                                dbHandler.getUserSittingRec().setStartTime(dateStr());//***

                                Log.d("calTime", "startTime = " + startTime);

                            }else{
                                Toast.makeText(this,
                                        "You are not in a correct posture, please refer to the instruction",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                .addOnFailureListener(
                        Throwable::printStackTrace)
                .addOnCompleteListener(
                        (r) -> {}
                );
    }



    private void initiateSocketConnection() {
        Log.e("Init", "The initiateSocketConnection");
        /*OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
         */

        socket.on("full", (args) -> {
            runOnUiThread(() -> {
                Toast.makeText(HabitCorrectionActivity.this, "You already have other " +
                                "device connected to the server.",
                        Toast.LENGTH_LONG).show();
            });
        });

        socket.on("pm", (args) -> {
            String text1 = (String) args[0];
            //Log.e("", (String) args[0]);
            if(!connected){
                connected = isConnected(text1);
                if(connected){
                    runOnUiThread(() -> {

                        connectWebcamFragment.connectSuccess();

                        new Handler().postDelayed(() -> {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.habit_fragment, setupPostureFragment, "setup")
                                    .commit();
                        }, 1000);
                    });
                }
                return;
            }

            if(!setup){
                runOnUiThread(() -> {
                    setupPostureFragment.setImage(decodeBase64(text1));
                });
                return;
            }
            runOnUiThread(() -> {
                getPose(text1);
            });

        });

        socket.connect();

        socket.emit("join", uid);
    }

    private void EndBtnClick() {

        String uid = null;
        try {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){
        }

        dbHandler.userId=uid;
        dbHandler.getUserSittingRec().setUserID(uid);
        dbHandler.getUserSittingRec().setEndTime(dateStr());
        endTime = System.currentTimeMillis();
        dbHandler.getUserSittingRec().setDuration((TimeUnit.MILLISECONDS.toSeconds(endTime-startTime)));
        dbHandler.calAccuracy();
        dbHandler.addNewRecord();
        dbHandler.getUserSittingRec().resetAllCol();
        dbHandler.insertRandomRecord();
        dbHandler.printDetails();


        Intent intent = new Intent( getApplicationContext(), LineChartReportActivity.class);
        startActivity(intent);
    }

    private String dateStr(){
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        String nowDate= formatter1.format(new Date());
        Log.d("Time", nowDate);
        return nowDate;
    }

    @Override
    public void onStop() {

        super.onStop();
        /*
        scheduler.shutdownNow();
        webSocket.close(1000,"User left");
        webSocket.cancel();

         */
        this.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        socket.disconnect();
        socket.off();
    }


}