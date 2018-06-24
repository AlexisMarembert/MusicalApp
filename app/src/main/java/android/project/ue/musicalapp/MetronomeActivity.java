package android.project.ue.musicalapp;

import android.app.Activity;
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
    private boolean isRed = true;
    private NumberPicker np;
    private int minValues = 30;
    private int maxValues = 240;
    String [] values = new String [maxValues - minValues];

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
     *
     */
    public void setValues() {
        int j = values.length;
        for (int i = 0; i < j; i++) {
            values[i] = Integer.toString(i + minValues);
        }
    }

    /**
     *  Method : initialise metronome interval list
     */
    public void initMetronomeInterval () {
        setValues();
        np = findViewById(R.id.selectMetronomeInterval);
        np.setDisplayedValues(values);
        np.setMinValue(0);
        np.setMaxValue(values.length - 1);
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
        double waitMetronome = 60000 / Double.parseDouble(values[np.getValue()]);

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
            }, new Date(), (int) waitMetronome);
        } else {
            metroTimer.cancel();
        }
    }

    /**
     * Method : reset metronome configuration
     * @param v
     */
    public void resetConfigMetronome(View v) {
        metroTimer.cancel();
        metroButton.setBackgroundResource(R.drawable.button_metronome_off);
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
