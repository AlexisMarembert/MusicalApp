package android.project.ue.musicalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MetronomeActivity extends Activity {

    private Button metroButton;
    private Timer metroTimer;
    private double waitMetronome;
    private boolean isRed = true;
    private NumberPicker np;
    private String [] values = new String []{
            "40", "42", "44", "46", "48","50", "52", "54", "56",
            "58", "60", "63", "66", "69", "72", "76", "80", "84",
            "88", "92", "96", "100", "104", "108", "112", "116",
            "120", "126", "132", "138", "144", "152", "160",
            "168", "176", "184", "192", "200", "208"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        metroButton = findViewById(R.id.metronomeButton);
        metroTimer  = new Timer();
        initMetronomeInterval();
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
     *  Method : initialise metronome interval list
     */
    public void initMetronomeInterval () {
        np = findViewById(R.id.selectMetronomeInterval);
        np.setDisplayedValues(values);
        np.setMinValue(0);
        np.setMaxValue(values.length-1);
        np.setOnValueChangedListener(onValueChangeListener);
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
    /**
     * Method called when button "configMetronome" is pressed
     * Then update integer "waitMetronome" and call method "startMetronome"
     * @param v
     */
    public void configMetronome(View v) {
        metroTimer.cancel();
        metroTimer  = new Timer();
        waitMetronome = 60000 / Double.parseDouble(values[np.getValue()]);

        if (waitMetronome > 0.0) {
            metroTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (isRed) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                metroButton.setBackgroundResource(R.drawable.button_metronome_on);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                metroButton.setBackgroundResource(R.drawable.button_metronome_off);
                            }
                        });
                    }
                    isRed = !isRed;
                }
            }, new Date(), (int)waitMetronome);
        } else {
            metroTimer.cancel();
        }
    }

    /**
     * Method : reset metronome configuration
     * @param view
     */
    public void resetConfigMetronome(View view) {
        metroTimer.cancel();
        metroButton.setBackgroundResource(R.drawable.button_metronome_off);
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
