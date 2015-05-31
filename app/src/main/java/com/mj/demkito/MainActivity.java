package com.mj.demkito;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mj.cheapgoogle.CheapMP3;
import com.mj.demkito.R;

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
            showInstruction();
        } else {
            //started by sharing
            String out = "";
            out += "\n"+ intent.getAction();
            out += "\n"+ intent.getType();
            out += "\n"+ intent.getDataString();
            M.logger(out);
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

    private void showInstruction() {
        setContentView(R.layout.activity_no_song);
        //final TextView p = (TextView) findViewById(R.id.instructions);
        //p.setTypeface(roboto);
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

    private void deleteOriginalSong() {

    }


    private void processIntent(final Intent intent) {
        Uri uri = (Uri)intent.getParcelableExtra(Intent.EXTRA_STREAM);
        M.logger(uri.toString());

        processUri(uri);//results to Song static subclass

        if (!Song.isMp3)
            M.toaster(context, "Failed to load your file:\nOnly mp3 is supported");
        else
        {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    analySeFileSafe();
                }
            });

        }

    }


    private void analySeFileSafe() {
        try {
            analyseFile(Song.path);
        } catch (FileNotFoundException e) {
            M.logger(e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            M.logger(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void processUri(Uri uri) {
        //results to song static class
        if (uri != null) {
            Song.path = uri.getPath();
            Song.name = uri.getLastPathSegment(); //getting the saved name

            String[] chunks = Song.path.split("\\.");
            if (chunks[chunks.length-1].equalsIgnoreCase("mp3"))
                Song.isMp3 = true;
            else
                Song.isMp3 = false;

        } else {
            M.logger("Uri is null");
        }

    }


    private void analyseFile(String path) throws FileNotFoundException, IOException {
        File file = new File(Song.path);
        //File tempFile;
        if (file.canRead() && file.exists() && file.isFile()) {
            M.logger("The file is readable");
            //tempFile = File.createTempFile("demkitoed", null, getApplicationContext().getCacheDir());

            cmp = new CheapMP3();

            //reading file
            cmp.ReadFile(file);
            Song.bitrate = cmp.getAvgBitrateKbps();
            Song.size = cmp.getFileSizeBytes();
            Song.sample_rate = cmp.getSampleRate();
            Song.sample_per_frame = cmp.getSamplesPerFrame();
            Song.num_frames = cmp.getNumFrames();
            Song.frameVolumes = cmp.getFrameGains();

            //solving file for cutting point
            Song.solveFile();

            M.logger(Song.userString());
            tv.setText(Song.userString());

            button_remove.setVisibility(View.VISIBLE);

        }

        else
            M.logger("The file is not readable");

    }

    private void demkitoSong(){
        try {
            File songx = new File(M.DEMKITO_FOLDER+""+Song.name);
            cmp.WriteFile(songx, Song.the_cut_frame, Song.num_frames - Song.the_cut_frame);
            button_remove.setVisibility(View.GONE);
            button_delete.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            M.toaster(context, e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


}
