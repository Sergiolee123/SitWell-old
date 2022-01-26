package com.fyp.sitwell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StaticImageActivity extends AppCompatActivity {
    private final String YOURIP = "";
    private final String SERVER_PATH = "ws://"+ YOURIP;
    private WebSocket webSocket;


    PoseDetector poseDetector;
    AccuratePoseDetectorOptions options;
    PreviewView mPreviewView;
    Button captureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_image);
        initiateSocketConnection();

        mPreviewView = findViewById(R.id.viewFinder);
        captureImage = findViewById(R.id.camera_capture_button);

        options =
                new AccuratePoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
                        .build();

        poseDetector = PoseDetection.getClient(options);

        initiateSocketConnection();

    }

    protected void getPose(String encodedImage){
        encodedImage = encodedImage.substring(encodedImage.indexOf(",")  + 1);
        byte[] imageByte = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<Pose> result =
                poseDetector.process(image)
                        .addOnSuccessListener(
                                PostureAnalyzer::new)
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
        JSONObject jsonObject = new JSONObject();
        /*
        try {
            jsonObject.put("name", "Tom");
            jsonObject.put("message", "Hi");

            webSocket.send(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
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
            Log.e("Re",text);
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