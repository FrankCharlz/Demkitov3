package com.mj.demkito;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("About Demkito");
        bar.setDisplayHomeAsUpEnabled(true);

        TextView bodyTv = (TextView) findViewById(R.id.body);
        String emojis = new String(Character.toChars(0x1f60a));
        String str = "Caused by: java.lang.NullPointerException\nTry again next time ... "
                    +"Hah haaha hhaa \nCrazy me!"+emojis;
        bodyTv.setText(str);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }
}
