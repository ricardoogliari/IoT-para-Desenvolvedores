package com.example.ricardoogliari.alarmesmartphone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{

    private SurfaceHolder mHolder;
    private Camera mCamera;

    private final Camera.Parameters parameters;
    byte[] buffer;

    public interface ReadBuffer {
        public void newBitmap(Bitmap bitmap);
    }

    public ReadBuffer listener;

    public void setListener(ReadBuffer listener){
        this.listener = listener;
    }


    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        parameters = mCamera.getParameters();

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

            int width = this.getWidth();
            int height = this.getHeight();
            Camera.Size best = null;
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

            for (int i = 0; i < previewSizes.size(); i++) {
                Camera.Size size = previewSizes.get(i);
                if ((size.width <= width && size.height <= height) || (size.height <= width && size.width <= height))  {
                    if (best==null) {
                        best=size;
                    } else {
                        int resultArea=best.width*best.height;
                        int newArea=size.width*size.height;

                        if (newArea>resultArea) {
                            best=size;
                        }
                    }
                }
            }

            Camera.Size previewSize = null;
            if (best != null) {
                previewSize = best;
            } else {
                previewSize = previewSizes.get(0);
            }

            parameters.setPreviewSize(previewSize.width, previewSize.height);
            mCamera.setParameters(parameters);

            int bufferSize = previewSize.width * previewSize.height * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat())/8;
            buffer = new byte[bufferSize];
            mCamera.addCallbackBuffer(buffer);

            mCamera.setPreviewCallbackWithBuffer(this);
        } catch (IOException e) {
            Log.d("camerapreview", "Erro ao definir a pré-visualização da câmera: " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {  }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {  }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;

        YuvImage yuv = new YuvImage(bytes, parameters.getPreviewFormat(), width, height, null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);

        byte[] bytes2 = out.toByteArray();
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.length);

        Matrix matrix = new Matrix();
        matrix.postRotate(270);

        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                width, height, matrix, true);

        listener.newBitmap(rotatedBitmap);
        mCamera.addCallbackBuffer(buffer);

    }

}
