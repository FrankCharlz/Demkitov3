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
        overridePendingTransition(R.anim.slide_in, android.R.anim.slide_out_right);

        ActionBar bar = getSupportActionBar();
        bar.setTitle("About Demkito");
        bar.setDisplayHomeAsUpEnabled(true);

        TextView bodyTv = (TextView) findViewById(R.id.body);
        String emoji1 = new String(Character.toChars(0x1f602));
        String emoji2 = new String(Character.toChars(0x1f61c));
        String emoji3 = new String(Character.toChars(0x1f628));
        String str = "Caused by: java.lang.NullPointerException\nTry again next time ... "
                    +"Hah haaha hhaa \nCrazy me! "+emoji1+emoji2+emoji1+emoji2+emoji1+emoji2+emoji3;
        bodyTv.setText(str);


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
