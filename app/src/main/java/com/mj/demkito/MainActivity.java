package com.mj.demkito;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mj.cheapgoogle.CheapMP3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private CheapMP3 cmp;
    protected MediaPlayer mp;
    private SurfaceView surfaceView;
    private Context context;
    private Button button_remove, button_delete;
    private Typeface roboto;
    private boolean gootToAnimate = true;
    private String name;
    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = getApplicationContext();

        initViews();
        M.checkAndCreateFolders();

        final Intent intent = getIntent();
        M.logger("Action : " + intent.getAction());

        if (intent.getAction().toString().contains("MAIN")) {
            //started from the menu...
            //showInstructions();
        } else {
            //started by sharing
            processIntent(intent);
        }

    }

    private void initViews() {
        roboto = Typeface.createFromAsset(getAssets(), "roboto.ttf");

        tv = (TextView) findViewById(R.id.tv);
        tv.setTypeface(roboto);

        button_remove = (Button) findViewById(R.id.button1);
        button_remove.setOnClickListener(new ButtonClicks());

        button_delete = (Button) findViewById(R.id.button2);
        button_delete.setOnClickListener(new ButtonClicks());

        button_remove.setTypeface(roboto);
        button_delete.setTypeface(roboto);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (gootToAnimate) {
            new BackgroundAnimation((ImageView)findViewById(R.id.imageView)).start();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showInstructions() {
        setContentView(R.layout.activity_no_song);
        gootToAnimate = true;
        final TextView p = (TextView) findViewById(R.id.instructions);
        p.setTypeface(roboto);
        String html = "To use this app: \n" +
                "<br><br>Go to your <strong>audio player</strong> or <strong>file browser</strong>\n" +
                "<br><br>Select the song you want to edit and press <strong>share</strong> \n" +
                "<br><br>In the context menu select <strong>Demkito</strong>.";
        p.setText(Html.fromHtml(html));
    }

    class ButtonClicks implements OnClickListener{
        @Override
        public void onClick(View v)
        {
            switch (v.getId()) {
                case R.id.button1:
                    demkitoSong();
                    break;

                case R.id.button2:
                    deleteOriginalSong();
                    break;

                default:break;
            }

        }


    }

    private void demkitoSong() {
        if (song.isValid()) {
            File f = new File(M.DEMKITO_FOLDER+""+song.getName());
            boolean success = song.removeAds(null);
            //if(success)
        } else {
            M.toaster(this , "The song can not be cut");
        }
    }

    private void deleteOriginalSong() {

    }

    private String getFilePath(Intent intent) {
        String result = getFilePathStreamWay(intent);
        if (result == null) result = getFilePathUriWay(intent);
        return result;
    }

    private String getFilePathStreamWay(Intent intent) {
        Uri uri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri == null) return  null;

        M.logger("STREAM WAY: "+uri.toString());
        return uri.getPath();
    }

    private String getFilePathUriWay(Intent intent) {
        Uri uri = intent.getData();
        if (uri == null ) return null;

        M.logger("URI WAY: "+uri.toString());

        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    private void processIntent(final Intent intent) {
        String path = getFilePath(intent);
        if (path == null) {quitProcess("");}
        if (!path.endsWith(".mp3")) {quitProcess("Format not mp3");}

        song = new Song(path);
        song.solve();
        tv.setText(song.toString());

    }

    private void quitProcess(String str) {
        M.logger("Quited processing file..");
        M.toaster(this, "Failed to load file:\n"+str);
    }



}
