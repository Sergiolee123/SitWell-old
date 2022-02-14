package com.fyp.sitwell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StaticImageActivity extends AppCompatActivity {
    private final String YOURIP = "nodejsssltest.herokuapp.com";
    //nodejsssltest.herokuapp.com:
    private final String SERVER_PATH = "wss://"+ YOURIP;
    private WebSocket webSocket;

    TextToSpeech textToSpeech;
    PoseDetector poseDetector;
    AccuratePoseDetectorOptions options;
    Boolean setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_image);


        setup = false;

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
        initiateSocketConnection();

    }

    protected boolean setup(SittingPostureAnalyzer s){

        String message = "";

        if(s.isNeckLateralBend()) {
            message += "Your Neck is not straight";
        }
        if(s.isShoulderAlignment()) {
            message += "Your shoulder is not align";
        }
        if(s.isLeftArmAbduction()) {
            message += "Your left arm is abduction";
        }
        if(s.isRightArmAdduction()) {
            message += "Your right arm is adduction";
        }

        if(!message.equals("")){
            textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
            return false;
        }else if(!s.setLeftShoulderZ() || !s.setRightShoulderZ()){
            return false;
        }

        return true;

    }

    protected void analyzePose(SittingPostureAnalyzer s){

        String message = "";

        if(s.isNeckLateralBend()) {
            message += "Your Neck is not straight";
        }
        if(s.isBackUpStraight()) {
            message += "Your Back is not straight";
        }
        if(s.isShoulderAlignment()) {
            message += "Your shoulder is not align";
        }
        if(s.isLeftArmAbduction()) {
            message += "Your left arm is abduction";
        }
        if(s.isRightArmAdduction()) {
            message += "Your right arm is adduction";
        }

        if(!message.equals("")){
            textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        }

    }


    protected void getPose(String encodedImage){

        encodedImage = encodedImage.substring(encodedImage.indexOf(",")  + 1);
        byte[] imageByte = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        Log.e("post","image");
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<Pose> result =
                poseDetector.process(image)
                        .addOnSuccessListener(
                                (pose) -> {
                                    Log.e("post","posta");
                                    SittingPostureAnalyzer s = new SittingPostureAnalyzer(pose,this);
                                    if(setup) {
                                        analyzePose(s);
                                    }else{
                                        setup = setup(s);
                                        if(setup){
                                            Log.e("SA","setup success");
                                        }
                                    }
                                }
                                )
                        .addOnFailureListener(
                                Throwable::printStackTrace)
                        .addOnCompleteListener(
                                (r) -> {

                                }
                        );
    }


    private void initiateSocketConnection() {

        Log.e("Init", "The initiateSocketConnection");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
        /*
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "Tom");
            jsonObject.put("message", "Hi");

            webSocket.send(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.e("SocketListener", "The SocketListener");
            runOnUiThread(() -> {
                Toast.makeText(StaticImageActivity.this, "Socket Connection Successful",
                        Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            //Log.e("Re",text);
            getPose(text);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            Log.e("onFailure","PK");
            t.printStackTrace();
            initiateSocketConnection();
        }

    }

    @Override
    public void onStop() {

        super.onStop();

    }
}