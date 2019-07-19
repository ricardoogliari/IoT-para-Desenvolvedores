package com.example.ricardoogliari.th;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

import java.io.IOException;
import java.util.Locale;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private UartDevice device;
    private TextToSpeech ttsEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ttsEngine = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale localeBR = new Locale("pt","br");
                    ttsEngine.setLanguage(localeBR);
                    ttsEngine.setPitch(1f);
                    ttsEngine.setSpeechRate(1f);
                }
            }
        });

        PeripheralManager manager = PeripheralManager.getInstance();
        try {
            device = manager.openUartDevice("UART0");

            device.setBaudrate(9600);
            device.setDataSize(8);
            device.setParity(UartDevice.PARITY_NONE);
            device.setStopBits(1);

            device.registerUartDeviceCallback(new UartDeviceCallback() {
                @Override
                public boolean onUartDeviceDataAvailable(UartDevice uartDevice) {
                    try {
                        byte[] buffer = new byte[8];
                        while (uartDevice.read(buffer, buffer.length) > 0) {
                            String data = new String(buffer);
                            if (data.startsWith("$")){
                                speak(data.substring(1, data.length() - 2));
                            }
                        }
                    } catch (IOException e){
                        Log.e(TAG, "Erro ao ler os dados recebidos: " + e);
                    }

                    return true;
                }
            });
        } catch (IOException ex) {
            Log.e(TAG, "Erro criando variável device: " + ex);
        }

    }

    public void speak(String text){
        ttsEngine.speak(text, TextToSpeech.QUEUE_ADD, null, "UTTERANCE_ID");
    }

    public void temperature(View view) {
        sendToUartGPIO("T");
    }

    public void humidity(View view) {
        sendToUartGPIO("H");
    }

    public void sendToUartGPIO(String send){
        try {
            device.write(send.getBytes(), send.getBytes().length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
