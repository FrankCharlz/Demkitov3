package com.mj.demkito;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private TextView heading_tv;
    private Typeface roboto;
    private TextView body_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);
        overridePendingTransition(R.anim.slide_in, android.R.anim.slide_out_right);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("About Demkito");
        bar.setDisplayHomeAsUpEnabled(true);

        initViews();

        String emoji1 = new String(Character.toChars(0x1f602));
        String emoji2 = new String(Character.toChars(0x1f660));
        String emoji3 = new String(Character.toChars(0x1f628));
        String str = "All cleaned files are saved to [/sdcard/Demkito]\nEnjoy "+emoji1+emoji2+emoji1+emoji2+emoji1+emoji2+emoji3;
        body_tv.setText(str);


    }

    private void initViews() {
        roboto = Typeface.createFromAsset(getAssets(), "roboto.ttf");
        heading_tv = (TextView) findViewById(R.id.heading);
        heading_tv.setTypeface(roboto);

        body_tv = (TextView) findViewById(R.id.body);
        body_tv.setTypeface(roboto);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
