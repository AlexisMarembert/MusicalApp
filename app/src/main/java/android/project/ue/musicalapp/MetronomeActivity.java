package android.project.ue.musicalapp;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
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
    private NumberPicker npMetric;
    private int minValues = 30;
    private int maxValues = 241;
    private String [] values = new String [maxValues - minValues];
    private String [] metrics = {"1/4" , "2/4" , "3/4" , "4/4"} ;
    private int metricCpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metricCpt = 0;
        NumberPicker.OnScrollListener scrollListener = new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                Toast.makeText(MetronomeActivity.this,"selected number "+scrollState, Toast.LENGTH_SHORT);

            }
        };
       // np.setOnScrollListener(scrollListener);
        //npMetric.setOnScrollListener((scrollListener));
        setContentView(R.layout.activity_metronome);
        metroButton = findViewById(R.id.metronomeButton);
        metroTimer  = new Timer();
        initMetronomeInterval();
        initMetronomeMetric() ;
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
    }

    public void initMetronomeMetric () {

        npMetric= findViewById(R.id.selectMetronomeMetric);
        npMetric.setDisplayedValues(metrics);
        npMetric.setMinValue(0);
        npMetric.setMaxValue(metrics.length - 1);
    }

    public static int getMetricValue(String stringValue){
        if(stringValue.equals("1/4")) return 1 ;
        if(stringValue.equals("2/4")) return 2 ;
        if(stringValue.equals("3/4")) return 3 ;
        if(stringValue.equals("4/4")) return 4 ;
        return -1 ;
    }

    //Play a Strong beat or a Weak depending on the given metric
    private int playTone(int counter ,int metric ,ToneGenerator toneGen){
        if(counter % metric == 0) {
            toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
            return 1 ;
        }
        else {
            toneGen.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 150);
            return 2 ;
        }
    }

    public void changeColor(int typeOfTone){
        if(typeOfTone==0) {
            metroButton.setBackgroundResource(R.drawable.button_metronome_off);
            System.out.println("Grey") ;
        }
        if(typeOfTone==1) {
            metroButton.setBackgroundResource(R.drawable.button_metronome_strong);
            System.out.println("Red") ;
        }

        if(typeOfTone== 2) {
            metroButton.setBackgroundResource(R.drawable.button_metronome_weak);
            System.out.println("Green") ;
        }

    }


    /**
     * Method called when button "configMetronome" is pressed
     * Then update integer "waitMetronome" and call method "startMetronome"
     */
    public void configMetronome(View v) {
        metroTimer.cancel() ;
        metroTimer  = new Timer();

        final ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        double waitMetronome = 60000 / Double.parseDouble(values[np.getValue()]);
        final int metricValue = getMetricValue(metrics[npMetric.getValue()]) ;

        if (waitMetronome > 0.0) {
            metroTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int type = playTone(metricCpt, metricValue, toneGen);
                            changeColor(type);
                            changeColor(0);
                        }
                    });

                    metricCpt = ++metricCpt;
                    if(metricCpt>metricValue) metricCpt=1 ;
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
