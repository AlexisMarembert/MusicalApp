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

    public void goToMetronome(View v) {
        Intent myIntent = new Intent(this, MetronomeActivity.class);
        startActivity(myIntent);
    }

    public void goToIntrument(View v) {
        Intent myIntent = new Intent(this, InstrumentActivity.class);
        startActivity(myIntent);
    }
}
