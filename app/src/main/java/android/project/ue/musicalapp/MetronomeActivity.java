package android.project.ue.musicalapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MetronomeActivity extends Activity {

    private Button metroButton;
    private Button okButton;
    private Button resetButton;
    private Button addPrefButton;
    private Button removePrefButton;
    private Button selectPrefButton;
    private Spinner spin;
    private Timer metroTimer;
    private NumberPicker np;
    private NumberPicker npMetric;
    private int minValues = 30;
    private int maxValues = 241;
    private String [] values = new String [maxValues - minValues];
    private String [] metrics = {"1/4" , "2/4" , "3/4" , "4/4"} ;
    private int metricCpt;
    private ArrayAdapter<String> arrayPref;
    private List<String> aList;
    private EditText writePreference;
    private AlertDialog.Builder alertDialogBuilder;
    private LayoutInflater layoutInflater;
    private View showView;
    private String pathToPrefs = "/data/data/android.project.ue.musicalapp/shared_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metricCpt = 0;

        setContentView(R.layout.activity_metronome);
        metroButton = findViewById(R.id.metronomeButton);
        okButton = findViewById(R.id.configMetronome);
        resetButton = findViewById(R.id.resetConfigMetronome);
        addPrefButton = findViewById(R.id.buttonAddPreference);
        removePrefButton = findViewById(R.id.buttonRemovePreference);
        selectPrefButton = findViewById(R.id.buttonSelectPreference);
        spin = findViewById(R.id.spinnerChoosePreference);

        addPrefButton.setEnabled(true);
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

    @Override
    protected void onPause(){
        super.onPause();
        resetButton.performClick();
    };

    @Override
    protected void onResume(){
        super.onResume();
        //okButton.performClick();
    };

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

    /**
     * Method : play a Strong beat or a Weak depending on the given metric
     * @param counter
     * @param metric
     * @param toneGen
     * @return
     */
    private int playTone(int counter ,int metric ,ToneGenerator toneGen){
           if(counter % metric == 0) {
               toneGen.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
               return 1 ;
           } else {
               toneGen.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 150);
               return 2 ;
           }
    }

    /**
     * Method : change BIP button color
     * @param typeOfTone
     */
    public void changeColor(int typeOfTone){
        if(typeOfTone==0) {
            metroButton.setBackgroundResource(R.drawable.button_metronome_off);
        }
        if(typeOfTone==1) {
            metroButton.setBackgroundResource(R.drawable.button_metronome_strong);
        }
        if(typeOfTone== 2) {
            metroButton.setBackgroundResource(R.drawable.button_metronome_weak);
        }
    }

    /**
     * Method called when button "configMetronome" is pressed
     * Then update integer "waitMetronome" and call method "startMetronome"
     * @param v
     */
    public void configMetronome(View v) {
        okButton.setEnabled(false);
        resetButton.setEnabled(true);

        metroTimer.cancel() ;
        metroTimer  = new Timer();

        final ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        double waitMetronome = 30000 / Double.parseDouble(values[np.getValue()]);
        final int metricValue = getMetricValue(metrics[npMetric.getValue()]);

        if (waitMetronome > 0.0) {
            metroTimer.scheduleAtFixedRate(new TimerTask() {
                boolean alt = true ;
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(alt) {
                                int type = playTone(metricCpt, metricValue, toneGen);
                                changeColor(type);
                                metricCpt = ++metricCpt;
                            }
                            else {
                                changeColor(0);
                            }
                            alt = ! alt ;
                            if(metricCpt>metricValue) metricCpt=1 ;
                        }
                    });
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
     * Method : initialize spinner list
     */
    public void initSpinList() {
        File[] files = new File(pathToPrefs).listFiles();
        aList = new ArrayList<String>();

        // list all files in shared_prefs folder
        for (File f : files) {
            aList.add(f.getName().toString().substring(0,f.getName().length() - 4));
        }

        arrayPref = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aList);
        spin.setAdapter(arrayPref);

        if (spin.getSelectedItem() == null) {
            removePrefButton.setEnabled(false);
            selectPrefButton.setEnabled(false);
        } else {
            removePrefButton.setEnabled(true);
            selectPrefButton.setEnabled(true);
        }
    }

    /**
     * Method : update spinner list
     * @param prefToAdd
     */
    public void updateSpinList(String prefToAdd) {
        aList.add(prefToAdd);
        arrayPref = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aList);
        spin.setAdapter(arrayPref);
    }

    /**
     * Method : select preference from spinner list
     * @param v
     */
    public void selectPreference(View v) {
        np.setValue(getApplicationContext().getSharedPreferences(spin.getSelectedItem().toString(), MODE_PRIVATE).getInt("idRythm", 0) - minValues);
        npMetric.setValue(getApplicationContext().getSharedPreferences(spin.getSelectedItem().toString(), MODE_PRIVATE).getInt("idMetric", 0));

        resetConfigMetronome(v);
        configMetronome(v);
    }

    /**
     * Method : show popup to add preference
     * @param v
     */
    private void showPopupAddPreference(View v){
        layoutInflater = LayoutInflater.from(MetronomeActivity.this);
        showView = layoutInflater.inflate(R.layout.activity_addpreference, null);

        alertDialogBuilder = new AlertDialog.Builder(MetronomeActivity.this);
        alertDialogBuilder.setView(showView);

        writePreference = showView.findViewById(R.id.writePreferenceText);

        // if clicked on "OK"
        alertDialogBuilder.setCancelable(false).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // add in spinner list
                updateSpinList(String.valueOf(writePreference.getText()));

                // add preference
                getApplicationContext().getSharedPreferences(String.valueOf(writePreference.getText()), MODE_PRIVATE)
                        .edit()
                        .putInt("idRythm", Integer.parseInt(values[np.getValue()]))
                        .putInt("idMetric", npMetric.getValue())
                        .apply();
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

        removePrefButton.setEnabled(true);
        selectPrefButton.setEnabled(true);
    }

    /**
     * Method : Remove preference
     * @param v
     */
    public void removePreference(View v) {
        // remove preference
        File fileToRemove = new File(pathToPrefs+"/"+spin.getSelectedItem().toString()+".xml");
        fileToRemove.delete();
        Toast.makeText(MetronomeActivity.this,spin.getSelectedItem().toString()+" : REMOVED",Toast.LENGTH_LONG).show();

        // update spin list
        initSpinList();
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
