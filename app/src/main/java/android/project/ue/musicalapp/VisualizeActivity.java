package android.project.ue.musicalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VisualizeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
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
     * Method called when button "goToMain" is pressed
     * Then start activity "MainActivity"
     * @param v
     */
    public void goToMain(View v) {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
