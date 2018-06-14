package android.project.ue.musicalapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MetronomeActivity extends Activity {

    private Button metroButton;
    private Timer metroTimer;
    private int waitMetronome;
    private boolean isRed = true;
    private EditText eText;
    long startTime;
    long EndTime;
    private NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        metroButton = findViewById(R.id.metronomeButton);
        metroTimer  = new Timer();

        np = findViewById(R.id.selectMetronomeInterval);
        np.setMinValue(200);
        np.setMaxValue(20000);
        np.setOnValueChangedListener(onValueChangeListener);
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

        metroTimer.scheduleAtFixedRate(new TimerTask() {
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

    /**
     * Listener on Number Picker
     */
    NumberPicker.OnValueChangeListener onValueChangeListener =
            new NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker nb, int i, int i1) {
                    Toast.makeText(MetronomeActivity.this,"selected number "+nb.getValue(), Toast.LENGTH_SHORT);
                }
            };
}
