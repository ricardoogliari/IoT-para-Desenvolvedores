package com.example.ricardoogliari.alarmesmartphone;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CameraPreview.ReadBuffer {

    private Camera mCamera;
    private CameraPreview mPreview;

    private FirebaseVisionFaceDetectorOptions options;
    public boolean working;

    private Camera getCameraInstance()
    {
        Camera camera = null;
        try {
            camera = Camera.open(1);
        } catch (Exception e){}

        return camera;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setContourMode(FirebaseVisionFaceDetectorOptions.NO_CONTOURS)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.NO_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();

        FirebaseApp.initializeApp(this);

        mCamera = getCameraInstance();

        Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        Camera.getCameraInfo(1, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        result = (info.orientation + degrees) % 360;
        result = (360 - result) % 360;
        mCamera.setDisplayOrientation(result);

        mPreview = new CameraPreview(this, mCamera);
        mPreview.setListener(this);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }

    @Override
    public void newBitmap(Bitmap bitmap) {
        if (!working) {
            working = true;
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionFaceDetector detector = FirebaseVision.getInstance().getVisionFaceDetector(options);

            detector.detectInImage(image)
                    .addOnSuccessListener(
                            new OnSuccessListener<List<FirebaseVisionFace>>() {
                                @Override
                                public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                                    for (FirebaseVisionFace face : firebaseVisionFaces) {
                                        if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                            float smileProb = face.getSmilingProbability();
                                            if (smileProb > 0.85){
                                                retrofit2.Call<ReturnParser> repos = LifeCycle.service.stopAlarm();
                                                repos.enqueue(new Callback<ReturnParser>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<ReturnParser> call, Response<ReturnParser> response) {
                                                        Log.e("resposta", "sucesso? " + response.isSuccessful());
                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<ReturnParser> call, Throwable t) {
                                                        Log.e("resposta", "erro: " + t.getLocalizedMessage());
                                                    }
                                                });

                                            }
                                        }
                                    }
                                    working = false;
                                }
                            }
                    )
                    .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              working = false;
                          }
                      }
                    );
        }

    }

}
