package com.example.ricardoogliari.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Pwm;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity {

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManager manager = PeripheralManager.getInstance();

        /**
         * Código que lista as portas GPIO e PWM, mostrado na Listagem 9.5
         */
        List<String> gpioList = manager.getGpioList();

        if (gpioList.isEmpty()) {
            Log.i(TAG, "Nenhuma porta GPIO disponível neste dispositivo.");
        } else {
            Log.i(TAG, "Lista das portas disponíveis: " + gpioList);
        }
        List<String> pwmList = manager.getPwmList();
        if (pwmList.isEmpty()) {
            Log.i(TAG, "Nenhuma porta PWM disponível neste dispositivo.");
        } else {
            Log.i(TAG, "Lista das portas disponíveis: " + pwmList);
        }

        /**
         * Código que pisca o LED, mostrado na Listagem 9.6
         */
        try {
            Gpio gpio = manager.openGpio("BCM13");
            gpio.setDirection(Gpio.DIRECTION_IN);
            while (true) {
                gpio.setValue(true);
                Thread.sleep(500);
                gpio.setValue(false);
                Thread.sleep(500);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){

        }

        /**
         * Código que usa o LED como um PWM, mostrado na Listagem 9.7
         */
        try {
            Pwm pwm = manager.openPwm("PWM1");
            pwm.setPwmFrequencyHz(120);
            pwm.setEnabled(true);
            while (true) {
                for (int i = 0; i < 101; i++){
                    pwm.setPwmDutyCycle(i);
                    Thread.sleep(50);
                }
                for (int i = 100; i > 0; i--){
                    pwm.setPwmDutyCycle(i);
                    Thread.sleep(50);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
