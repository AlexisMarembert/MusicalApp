package android.project.ue.musicalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MetronomeActivity extends Activity {

    private Button metroButton;
    private Button okButton;
    private Button resetButton;
    private Button addPrefButton;
    private Spinner spin;
    private Timer metroTimer;
    private boolean isRed = true;
    private NumberPicker np;
    private NumberPicker npMetric;
    private int minValues = 30;
    private int maxValues = 241;
    private String [] values = new String [maxValues - minValues];
    private String [] metrics = {"1/4" , "2/4" , "3/4" , "4/4"} ;
    private int metricCpt;
    private ArrayAdapter<String> arrayPref;
    private ArrayList<String> aList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metricCpt = 0;

        setContentView(R.layout.activity_metronome);
        metroButton = findViewById(R.id.metronomeButton);
        okButton = findViewById(R.id.configMetronome);
        resetButton = findViewById(R.id.resetConfigMetronome);
        addPrefButton = findViewById(R.id.buttonAddPreference);

        okButton.setEnabled(true);
        resetButton.setEnabled(false);

        metroTimer  = new Timer();

        initMetronomeInterval();
        initMetronomeMetric() ;
        initSpinList();

        addPrefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupAddPreference(v);
            }
        });
    }

    /**
     * Method : initialize spinner list
     */
    public void initSpinList() {
        spin = findViewById(R.id.spinnerChoosePreference);
        aList = new ArrayList<String>(Arrays.asList(new String[]{""}));
        arrayPref = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aList);
        spin.setAdapter(arrayPref);
    }

    /**
     * Method : update spinner list
     * @param prefToAdd
     */
    public void updateSpinList(String prefToAdd) {
        System.out.println("begin : "+prefToAdd);
        aList.add(prefToAdd);
        arrayPref = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aList);
        System.out.println("before : "+prefToAdd);
        spin.setAdapter(arrayPref);
        System.out.println("after : "+prefToAdd);
    }

    /**
     * Method : set metronome interval values
     */
    public void setValues() {
        int j = values.length;
        for (int i = 0; i < j; i++) {
            values[i] = Integer.toString(i + minValues);
        }
    }

    /**
     * Method : initialise metronome interval list
     */
    public void initMetronomeInterval () {
        setValues();
        np = findViewById(R.id.selectMetronomeInterval);
        np.setDisplayedValues(values);
        np.setMinValue(0);
        np.setMaxValue(values.length - 1);
    }

    /**
     * Method : initialize metronome metric
     */
    public void initMetronomeMetric () {
        npMetric= findViewById(R.id.selectMetronomeMetric);
        npMetric.setDisplayedValues(metrics);
        npMetric.setMinValue(0);
        npMetric.setMaxValue(metrics.length - 1);
    }

    /**
     * Method : get metric value
     * @param stringValue
     * @return case or error
     */
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
           } else {
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
        okButton.setEnabled(false);
        resetButton.setEnabled(true);

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
        okButton.setEnabled(true);
        resetButton.setEnabled(false);
        metroTimer.cancel();
        metroButton.setBackgroundResource(R.drawable.button_metronome_off);
    }

    /**
     * Method : show popup to add preference
     * @param v
     */
    private void showPopupAddPreference(View v){
        //
        LayoutInflater layoutInflater = LayoutInflater.from(MetronomeActivity.this);
        View showView = layoutInflater.inflate(R.layout.activity_addpreference, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MetronomeActivity.this);
        alertDialogBuilder.setView(showView);

        final EditText writePreference = showView.findViewById(R.id.writePreferenceText);

        // if clicked on "OK"
        alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.out.println("text preference : "+writePreference.getText());
                System.out.println("rythm value : "+values[np.getValue()]);
                System.out.println("metric value : "+getMetricValue(metrics[npMetric.getValue()]));
                // add in spinner list
                updateSpinList(String.valueOf(writePreference.getText()));

                SharedPreferences.Editor editPref = getSharedPreferences("myPreference", MODE_PRIVATE).edit();
                editPref.putString("idName", String.valueOf(writePreference.getText()));
                editPref.putInt("idRythm", Integer.parseInt(values[np.getValue()]));
                editPref.putInt("idMetric", getMetricValue(metrics[npMetric.getValue()]));
                editPref.apply();
                Toast.makeText(MetronomeActivity.this,writePreference.getText()+" : SAVED",Toast.LENGTH_LONG).show();
            }
        // if clicked on "cancel"
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /**
     * Method called when button "goToMain" is pressed
     * Then start activity "MainActivity"
     * @param v
     */
    public void goToMain(View v) {
        resetConfigMetronome(v);
        finish();
    }

}
