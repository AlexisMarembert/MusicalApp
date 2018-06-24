package android.project.ue.musicalapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

//https://www.tutorialspoint.com/android/android_audio_capture.htm

public class InstrumentActivity extends Activity {

    private Button buttonRecord;
    private Button buttonStopRecord;
    private Button buttonPlayLastRecord;
    private Button buttonStopListening;
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecord;
    private Random random;
    private String RandomAudioFileName = "azertyuiopqsdfghjklmwxcvbn";
    public static final int RequestPermissionCode = 1;
    private MediaPlayer mediaPlayer ;
    private static final int capacityAudioFile = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument);

        buttonRecord = findViewById(R.id.buttonRecord);
        buttonStopRecord = findViewById(R.id.buttonStopRecord);
        buttonStopListening = findViewById(R.id.buttonStopListening);
        buttonPlayLastRecord = findViewById(R.id.buttonPlayLastRecord);

        buttonStopRecord.setEnabled(false);
        buttonPlayLastRecord.setEnabled(false);
        buttonStopListening.setEnabled(false);

        random = new Random();

        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permStorage = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
                int permAudio = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);

                if(permStorage == PackageManager.PERMISSION_GRANTED && permAudio == PackageManager.PERMISSION_GRANTED) {

                    StringBuilder stringBuilder = new StringBuilder(capacityAudioFile);
                    int i = 0 ;
                    while(i < capacityAudioFile) {
                        stringBuilder.append(RandomAudioFileName.
                                charAt(random.nextInt(RandomAudioFileName.length())));

                        i++ ;
                    }

                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/"
                            + stringBuilder.toString()
                            + "AudioRecording.3gp";

                    mediaRecord=new MediaRecorder();
                    mediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecord.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecord.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    mediaRecord.setOutputFile(AudioSavePathInDevice);

                    try {
                        mediaRecord.prepare();
                        mediaRecord.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    buttonRecord.setEnabled(false);
                    buttonStopRecord.setEnabled(true);

                    Toast.makeText(InstrumentActivity.this,
                            "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
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
     * Method request permissions
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(InstrumentActivity.this,
                new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO},
                RequestPermissionCode);
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
