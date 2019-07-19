package com.example.ricardoogliari.alarmefirebase;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements GpioCallback {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ECHO_PIN_NAME = "BCM20";
    private static final String TRIGGER_PIN_NAME = "BCM21";

    private Gpio mEcho;
    private Gpio mTrigger;

    private long time1, time2;

    private Handler ultrasonicTriggerHandler;
    private MediaPlayer mediaPlayer;

    private Runnable triggerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                readDistanceAsnyc();
                ultrasonicTriggerHandler.postDelayed(triggerRunnable, 1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManager service = PeripheralManager.getInstance();

        try {
            mEcho = service.openGpio(ECHO_PIN_NAME);
            mEcho.setDirection(Gpio.DIRECTION_IN);
            mEcho.setEdgeTriggerType(Gpio.EDGE_BOTH);
            mEcho.setActiveType(Gpio.ACTIVE_HIGH);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mTrigger = service.openGpio(TRIGGER_PIN_NAME);

            mTrigger.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ultrasonicTriggerHandler = new Handler();
        ultrasonicTriggerHandler.post(triggerRunnable);

        try {
            mEcho.registerGpioCallback(null, this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onGpioEdge(Gpio gpio) {
        try {

            if (gpio.getValue() == false){
                time2 = System.nanoTime();
                long pulseWidth = time2 - time1;
                double distance = (pulseWidth / 1000000000.0) * 340.0 / 2.0 * 100.0;

                if (distance < 50){
                    controlSound(true);
                }

            } else {
                time1 = System.nanoTime();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        controlSound(false);
    };

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void controlSound(boolean play){
        if (play){
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return;
            }
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sirene);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

    }

    protected void readDistanceAsnyc() throws IOException, InterruptedException {
        mTrigger.setValue(false);
        Thread.sleep(0,2000);

        mTrigger.setValue(true);
        Thread.sleep(0,10000); //10 microsec

        mTrigger.setValue(false);
    }


}
