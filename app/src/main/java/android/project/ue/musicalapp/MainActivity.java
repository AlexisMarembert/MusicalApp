package android.project.ue.musicalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
     * Method called when button "goToMetronome" is called
     * Then start activity "MetronomeActivity"
     * @param v
     */
    public void goToMetronome(View v) {
        Intent myIntent = new Intent(this, MetronomeActivity.class);
        startActivity(myIntent);
    }

    /**
     * Method called when button "goToInstrument" is called
     * Then start activity "InstrumentActivity"
     * @param v
     */
    public void goToInstrument(View v) {
        Intent myIntent = new Intent(this, InstrumentActivity.class);
        startActivity(myIntent);
    }

    /**
     * Method called when button "goToVisualize" is called
     * Then start activity "VisualizeActivity"
     * @param v
     */
    public void goToVisualize(View v) {
        Intent myIntent = new Intent(this, VisualizeActivity.class);
        startActivity(myIntent);
    }

    /**
     * Method called when button "exit" is called
     * Then close application
     * @param v
     */
    public void exit(View v) {
        finishActivity(0);
        moveTaskToBack(true);
    }
}
