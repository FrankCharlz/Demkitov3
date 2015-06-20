package com.mj.demkito;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        setContentView(R.layout.about_layout);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("About Demkito");
        bar.setHomeButtonEnabled(true);

    }
}
