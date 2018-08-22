package com.shashank.autosmsreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Queue;

public class ReadMessage extends Activity implements TextToSpeech.OnInitListener {

    private TextView senderName;
    private TextView messageBody;
    private Button stopButton;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private TextToSpeech tts;
    int result;

    private static final int REQUEST_CODE = 100;

    String Sender="";
    String Message="";

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_popup);

        senderName=(TextView) findViewById(R.id.senderName);
        messageBody=(TextView) findViewById(R.id.messageBody);
        stopButton=(Button) findViewById(R.id.stopButton);
        settings= PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent=getIntent();
        Sender=intent.getStringExtra("senderName");
        Message=intent.getStringExtra("messageBody");
        senderName.setText("From: "+Sender);
        messageBody.setText(Message);


        Intent checkTTS=new Intent();
        checkTTS.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTS,REQUEST_CODE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tts!=null){
                    tts.stop();
                }
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            if(resultCode==TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                tts=new TextToSpeech(this,this);

            }
            else{
                Intent installTSS=new Intent();
                installTSS.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTSS);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS){

            result=tts.setLanguage(Locale.US);
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                }

                @Override
                public void onDone(final String utteranceId) {
                    ReadMessage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ReadMessage.this.finish();
                        }
                    });
                }
                @Override
                public void onError(String utteranceId) {

                }
            });
            TTS();
        }
        else{
            Toast.makeText(this,"Feature not supported in your device",Toast.LENGTH_LONG).show();
        }
    }

    public void TTS(){
        if(result== TextToSpeech.LANG_NOT_SUPPORTED||result==TextToSpeech.LANG_MISSING_DATA){
            Toast.makeText(this,"Language not available,Please download",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setAction("com.android.settings.TTS_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        else{
            if(settings.getBoolean("shake",true))
                mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

            setTTSParameters();
            String text="Message from: "+Sender+" Message is "+Message;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                tts.speak(text,TextToSpeech.QUEUE_ADD,null,TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);

            else
                tts.speak(text, TextToSpeech.QUEUE_ADD,null);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(settings.getBoolean("shake",true))
            mSensorManager.unregisterListener(mShakeDetector);
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
    }

    public void handleShakeEvent(int count){
        if(count>=2){
         //   Toast.makeText(getApplicationContext(),"Shake detected",Toast.LENGTH_LONG).show();
            if(tts!=null){
                tts.stop();
            }
            finish();
        }
    }

    public void setTTSParameters(){
       // settings= PreferenceManager.getDefaultSharedPreferences(this);
        int language= Integer.parseInt(settings.getString("language","1"));
        int pitch= Integer.parseInt(settings.getString("pitch","3"));
        int speed=Integer.parseInt(settings.getString("speed","3"));

        switch (language){
            case 1:
                tts.setLanguage(Locale.US);
                break;
            case 2:
                tts.setLanguage(Locale.UK);
                break;
            case 3:
                tts.setLanguage(Locale.GERMAN);
        }

        switch(pitch){
            case 1:
                tts.setPitch(0.1f);
                break;
            case 2:
                tts.setPitch(0.5f);
                break;
            case 3:
                tts.setPitch(1.2f);
                break;
            case 4:
                tts.setPitch(1.7f);
                break;
            case 5:
                tts.setPitch(2.0f);
        }

        switch(speed){
            case 1:
                tts.setSpeechRate(0.4f);
                break;
            case 2:
                tts.setSpeechRate(0.7f);
                break;
            case 3:
                tts.setSpeechRate(0.9f);
                break;
            case 4:
                tts.setSpeechRate(1.3f);
                break;
            case 5:
                tts.setSpeechRate(1.8f);
        }
    }
}
