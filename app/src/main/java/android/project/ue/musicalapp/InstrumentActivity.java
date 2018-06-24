package android.project.ue.musicalapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//https://www.tutorialspoint.com/android/android_audio_capture.htm

public class InstrumentActivity extends Activity {

    private Button buttonRecord;
    private Button buttonStopRecord;

    private int permStorage;
    private int permAudio;

    private AudioRecord audioRecord;
    private int bufferSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument);

        //permissions
        if(ContextCompat.checkSelfPermission(InstrumentActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(InstrumentActivity.this, new String[]{RECORD_AUDIO}, 0);
        } else {
            // TODO all ok do job
        }

        // set buttons
        buttonRecord = findViewById(R.id.buttonRecord);
        buttonStopRecord = findViewById(R.id.buttonStopRecord);
        buttonStopRecord.setEnabled(false);

        // listener record
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });

        // listener stop record
        buttonStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Method start record
     */
    public void startRecord() {
        // audio record configuration
        int audioSrc = MediaRecorder.AudioSource.MIC;
        int configChannel = AudioFormat.CHANNEL_IN_MONO;
        int audioEncode = AudioFormat.ENCODING_PCM_16BIT;

        // get sample rate valide
        int sampleRate = 0;
        for (int rate : new int[]{8000, 11025, 16000, 22050, 44100}) {
            int bufferSize = AudioRecord.getMinBufferSize(rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            if (bufferSize > 0) {
                sampleRate = rate;
            }
        }

        // next config
        bufferSize = 1024;
        audioRecord = new AudioRecord(audioSrc, sampleRate, configChannel, audioEncode, bufferSize);
        audioRecord.startRecording();

        // set enabled buttons
        buttonRecord.setEnabled(false);
        buttonStopRecord.setEnabled(true);
    }

    /**
     * Method stop record
     */
    public void stopRecord() {
        audioRecord.stop();
        audioRecord.release();

        // set enabled buttons
        buttonRecord.setEnabled(true);
        buttonStopRecord.setEnabled(false);
    }

    /**
     * Method called when button "goToMain" is pressed
     * Then start activity "MainActivity"
     * @param v
     */
    public void goToMain(View v) {
        finish();
    }
}
