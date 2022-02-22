package com.fyp.sitwell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class HabitCorrectionActivity extends FragmentActivity{
    private final String YOURIP = "nodejsssltest.herokuapp.com/";
    private final String SERVER_PATH = "wss://"+ YOURIP;
    private WebSocket webSocket;

    private TextToSpeech textToSpeech;
    private PoseDetector poseDetector;
    private AccuratePoseDetectorOptions options;
    private Boolean setup, connected;
    private FragmentManager fragmentManager;
    private ConnectWebcamFragment connectWebcamFragment;
    private SetupPostureFragment setupPostureFragment;
    private HabitCorrectionFragment habitCorrectionFragment;
    private ScheduledExecutorService scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_correction);

        connectWebcamFragment = new ConnectWebcamFragment();
        setupPostureFragment = new SetupPostureFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.habit_fragment, connectWebcamFragment, "ConnectWebcam")
                .commit();

        scheduler = Executors.newScheduledThreadPool(1);
        setup = false;
        connected = false;

        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if(i!=TextToSpeech.ERROR){
                textToSpeech.setLanguage(Locale.UK);
            }
        });

        options =
                new AccuratePoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                        .build();

        poseDetector = PoseDetection.getClient(options);
        initiateSocketConnection("onCreate");

    }

    protected boolean isSetup(SittingPostureAnalyzer s){

        String message = "";

        if(s.isNeckLateralBend()) {
            message += "Your Neck is not straight ";
        }
        if(s.isShoulderAlignment()) {
            message += "Your shoulder is not align ";
        }
        if(s.isLeftArmCorrect()) {
            message += "Your left arm is in bad position ";
        }
        if(s.isRightArmCorrect()) {
            message += "Your right arm is in bad position ";
        }

        if(!message.equals("")){
            message += "Please make sure you are in a correct posture";
            textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
            return false;
        }

        return s.setLeftShoulderZ() && s.setRightShoulderZ();

    }

    protected void analyzePose(SittingPostureAnalyzer s){

        String message = "";

        if(!s.isPrepare()) {
            habitCorrectionFragment.setTextView("Your body are not captured by the Webcam");
            return;
        }

        if(s.isNeckLateralBend()) {
            message += "Your Neck is not straight@";
        }
        if(s.isBackUpStraight()) {
            message += "Your Back is not straight@";
        }
        if(s.isShoulderAlignment()) {
            message += "Your shoulder is not align@";
        }
        if(s.isLeftArmCorrect()) {
            message += "Your left arm is in bad position@";
        }
        if(s.isRightArmCorrect()) {
            message += "Your right arm is in bad position@";
        }

        if(message.equals("")){
            habitCorrectionFragment.clearAll();
        }else{
            habitCorrectionFragment.setTextView(message.replace("@", "\n"));
            habitCorrectionFragment.showCorrectPose();
            textToSpeech.speak(message.replace("@", ",")
                            + " Please refer to your correct posture" ,
                    TextToSpeech.QUEUE_FLUSH,null,null);
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
                            SittingPostureAnalyzer s = new SittingPostureAnalyzer(pose,this);
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
                            Log.e("post","posta");
                            SittingPostureAnalyzer s = new SittingPostureAnalyzer(pose,this);
                            setup = isSetup(s);
                            if(setup){
                                habitCorrectionFragment = new HabitCorrectionFragment(bitmap);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.habit_fragment, habitCorrectionFragment, "habit")
                                        .commit();

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



    private void initiateSocketConnection(String text) {

        Log.e("Init", "The initiateSocketConnection " + text);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    private void keepAlive(){

        scheduler.scheduleAtFixedRate(() -> {
                    webSocket.send("ping");
                    Log.e("live","ping");
                }
                , 0,15, TimeUnit.SECONDS);

    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.e("SocketListener", "The SocketListener");
            runOnUiThread(HabitCorrectionActivity.this::keepAlive);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.e("Re",text);
            if(text.equals("ping")){
                return;
            }
            if(!connected){
                connected = isConnected(text);
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
                    setupPostureFragment.setImage(decodeBase64(text));
                });
                return;
            }
            runOnUiThread(() -> {
                getPose(text);
            });
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.e("onClose", "Close: " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Log.e("onFailure","connect fail");
            t.printStackTrace();
            initiateSocketConnection("onFailure");
        }

    }


    @Override
    public void onStop() {

        super.onStop();
        scheduler.shutdownNow();
        webSocket.close(1000,"User left");
        webSocket.cancel();
        textToSpeech.shutdown();
        this.finish();
    }
}