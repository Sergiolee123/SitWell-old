package com.fyp.sitwell.muscleTraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Size;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.sitwell.R;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MuscleRelaxTrainingActivity extends AppCompatActivity {

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
    static Class<?> mClass;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_muscle_training);

        mClass = (Class<?>) getIntent().getSerializableExtra("class");

        if(mClass == null){
            this.finish();
        }

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
                    //
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

        ExecutorService analysisExecutor = new ThreadPoolExecutor(1,10
                ,0, TimeUnit.SECONDS, new SynchronousQueue<>()
                , Executors.defaultThreadFactory(),new ThreadPoolExecutor.CallerRunsPolicy());
        imageAnalysis.setAnalyzer(analysisExecutor, new MuscleRelaxTrainingActivity.PoseAnalyzer());

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
        MuscleTrainingInterface t = MuscleTrainingFactory.getMuscleTraining(mClass, pose);;

        if(t == null){
            throw new NullPointerException();
        }

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
        }else if(t.isHalf()){
            Log.e("muscle","isUp");
            repeatCounter.finishedHalf();
        }else if(t.isFinished()){
            if(repeatCounter.addCounter()){
                textToSpeech.speak("You have complete " + repeatCounter.getCounter() + " times"
                        ,TextToSpeech.QUEUE_ADD,null,null);
            }
        }
        if(t.isNextSide(repeatCounter.getCounter())){
            textToSpeech.speak("You have finished one side Please change to another side",
                    TextToSpeech.QUEUE_ADD, null, null);
            repeatCounter.setZero();
        }

        if(t.isEnd()){
            textToSpeech.speak(("Good job You have finished this training")
                    ,TextToSpeech.QUEUE_FLUSH, null, null);
            this.finish();
        }

        if(!textToSpeech.isSpeaking() && message != null){
            textToSpeech.speak(message,TextToSpeech.QUEUE_ADD,null,null);
            textView.setText(t.getInstruction());
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