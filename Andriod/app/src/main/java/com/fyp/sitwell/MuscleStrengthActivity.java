package com.fyp.sitwell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.sitwell.mucleTraining.RepeatCounter;
import com.fyp.sitwell.mucleTraining.TrainingPostureAnalyer;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MuscleStrengthActivity extends AppCompatActivity {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};
    PoseDetector poseDetector;
    AccuratePoseDetectorOptions options;
    PreviewView previewView;
    ImageAnalysis imageAnalysis;
    RepeatCounter repeatCounter;
    TextToSpeech textToSpeech;
    Boolean started;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_muscle_strength);

        previewView = findViewById(R.id.viewBinder);
        textView = findViewById(R.id.text_instr_content);

        repeatCounter = new RepeatCounter();
        started = false;


        options =
                new AccuratePoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                        .build();

        poseDetector = PoseDetection.getClient(options);

        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if(i!=TextToSpeech.ERROR){
                textToSpeech.setLanguage(Locale.UK);
            }
        });

        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(640,480))
                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        ExecutorService analysisExecutor = Executors.newSingleThreadExecutor();;
        imageAnalysis.setAnalyzer(analysisExecutor, new MuscleStrengthActivity.PoseAnalyzer());

        preview.setSurfaceProvider(previewView.createSurfaceProvider());
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);

        AccuratePoseDetectorOptions options =
                new AccuratePoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                        .build();

        poseDetector = PoseDetection.getClient(options);

    }


    protected void getPose(Pose pose){



        String message = null;
        TrainingPostureAnalyer t = new TrainingPostureAnalyer(pose, "leftFoot");
        if(!t.isPrepare()){
            Log.e("muscle","isPrepare");
            message = "Please make sure your whole body is inside the phone camera";
            started = false;
        }else if(!t.isReady()){
            Log.e("muscle","isReady");
            message = "Please make sure you are in a correct posture";
            started = false;
        }else if(t.isReady() && !started){
            Log.e("muscle","started");
            textView.setText("");
            textToSpeech.speak( "You can start now, the app will count how many time you do"
                    ,TextToSpeech.QUEUE_ADD,null,null);
            started = true;
        }else if(t.isUp()){
            Log.e("muscle","isUp");
            repeatCounter.finishedHalf();
        }else if(t.isDown()){
            if(repeatCounter.addCounter()){
                textToSpeech.speak("You have complete " + repeatCounter.getCounter() + " times"
                        ,TextToSpeech.QUEUE_ADD,null,null);
            }
        }

        if(!textToSpeech.isSpeaking() && message != null){
            textToSpeech.speak(message,TextToSpeech.QUEUE_ADD,null,null);
            textView.setText(R.string.lying_lateral_leg_lift_instruction);
        }

        //textView.setText(t.debug());


    }


    private class PoseAnalyzer implements ImageAnalysis.Analyzer {

        @Override
        public void analyze(ImageProxy imageProxy) {
            @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = imageProxy.getImage();

            if (mediaImage != null) {
                InputImage image =
                        InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                // Pass image to an ML Kit Vision API
                // ...
                // Task completed successfully
                // ...
                // Task failed with an exception
                // ...
                Task<Pose> result =
                        poseDetector.process(image)
                                .addOnSuccessListener(
                                        (pose) -> {
                                            getPose(pose);
                                        }
                                )
                                .addOnFailureListener(
                                        Throwable::printStackTrace)
                                .addOnCompleteListener(
                                        (r) -> {
                                            imageProxy.close();
                                        }
                                );
            }
        }
    }


    @Override
    public void onStop() {

        super.onStop();
        imageAnalysis.clearAnalyzer();
        textToSpeech.shutdown();
        this.finish();

    }


    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}