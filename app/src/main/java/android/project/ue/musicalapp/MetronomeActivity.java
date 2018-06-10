package android.project.ue.musicalapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MetronomeActivity extends Activity {

    private Button metroButton;
    private Timer metroTimer;
    private int waitMetronome;
    private boolean isRed = true;
    private EditText eText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        metroButton = findViewById(R.id.metronomeButton);
        metroTimer  = new Timer();
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
     * Method called when button "configMetronome" is pressed
     * Then update integer "waitMetronome" and call method "startMetronome"
     * @param v
     */
    public void configMetronome(View v) {
        metroTimer.cancel();
        metroTimer  = new Timer();
        eText = (EditText) findViewById(R.id.metronomeInterval);
        waitMetronome = Integer.parseInt(eText.getText().toString());

        metroTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isRed) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            metroButton.setBackgroundColor(Color.RED);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            metroButton.setBackgroundColor(Color.GREEN);
                        }
                    });
                }
                isRed = !isRed;
            }
        }, new Date(), waitMetronome);
    }

    /**
     * Method called when button "goToMain" is pressed
     * Then start activity "MainActivity"
     * @param v
     */
    public void goToMain(View v) {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
