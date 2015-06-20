package com.mj.demkito;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mj.utils.AudioPlayer;
import com.mj.utils.ContentHelpers;
import com.mj.utils.M;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int RQ_CODE = 13;
    private TextView mainTextView;
    private Button button_remove, button_delete;
    private Typeface roboto;
    private boolean animation_started = false;
    private Song song;
    private ContentHelpers contentHelper;
    private AudioPlayer audioPlayer;
    private boolean audio_playing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_main);
        M.checkAndCreateFolders();

        //for file reading intent data
        contentHelper = new ContentHelpers(this);

        initViews();
        ActionBar bar = getSupportActionBar();
        bar.setTitle("Demkito");


        final Intent intent = getIntent();
        if (intent.getAction().contains("MAIN")) {
            //started from the menu...
            showInstructions();
        } else {
            //started by sharing
            processIntent(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/*");
                startActivityForResult(intent, RQ_CODE);
                break;

            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "You can download Demkito from:\n"
                        + "https://play.google.com/store/apps/details?id=com.mj.demkito");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

            case R.id.about:
                try {
                    //try since --market-- might not be installed
                    Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                    startActivity(i);
                } catch(Exception e) {e.printStackTrace();}
                break;

            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=com.mj.demkito") ;
                Intent playStore = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(playStore);
                break;

            default: break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RQ_CODE && data != null) {
                processIntent(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        roboto = Typeface.createFromAsset(getAssets(), "roboto.ttf");

        mainTextView = (TextView) findViewById(R.id.tv);
        mainTextView.setTypeface(roboto);

        button_remove = (Button) findViewById(R.id.button1);
        button_remove.setOnClickListener(new ButtonClicks());

        button_delete = (Button) findViewById(R.id.button2);
        button_delete.setOnClickListener(new ButtonClicks());

        button_remove.setTypeface(roboto);
        button_delete.setTypeface(roboto);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!animation_started) {
            //prevent starting animation twice.
            new BackgroundAnimation((ImageView) findViewById(R.id.imageView)).start();
            animation_started = true;
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showInstructions() {
        mainTextView.setTypeface(roboto);
        String html = "To use this app: \n" +
                "<br><br>Go to your <strong>audio player</strong> or <strong>file browser</strong>" +
                "<br><br>Select the song you want to edit and press <strong>share</strong>" +
                "<br><br>In the context menu select <strong>Demkito</strong>"+
                "<br><br>Or just press <strong>+</strong> above.";
        mainTextView.setText(Html.fromHtml(html));
    }

    class ButtonClicks implements OnClickListener{
        @Override
        public void onClick(final View v) {
            if (song == null) {
                M.toaster(getApplicationContext(), "No song selected/shared.");
            }
            else
            {
                switch (v.getId()) {
                    case R.id.button1:
                        demkitoSong();
                        break;

                    case R.id.button2:
                        previewSong();
                        break;
                    default:break;
                }
            }

        }
    }

    private void registerFileToMediaDb(File file) {
        MediaScannerConnection.scanFile(this,
                new String[] { file.getAbsolutePath()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        M.logger("Media scanner completed");
                    }
                });
    }


    private void demkitoSong() {
        if (song.isCleaned()) {
            M.toaster(this, "Ads already removed");
            return;//cease execution
        }
        if (song.isValid()) {
            File clean_file = new File(M.DEMKITO_FOLDER+""+song.getName());
            boolean success = song.removeAds(clean_file);
            if(success) {
                song.setCleaned();
                audioPlayer.setCleanPath();
                registerFileToMediaDb(clean_file);
                M.toaster(this,"Ad free song saved:\n"+clean_file.getAbsolutePath(),0);
            }
            else { M.toaster(this, "Failed to remove ads.");}
        } else {
            M.toaster(this , "The song can not be cut");
        }
    }

    private void previewSong() {
        if (song.isValid()) {
            if (audio_playing) {
                audioPlayer.stop();
                audio_playing = false;
                button_delete.setText("PREVIEW");
            }
            else {
                audioPlayer.play();
                audio_playing = true;
                button_delete.setText("STOP");
            }

        } else {
            M.toaster(this, "Selected file is *;&%$");
        }
    }

    @Override
    protected void onDestroy() {
        if (audioPlayer != null) audioPlayer.turnOff();
        super.onDestroy();
    }

    private void processIntent(final Intent intent) {
        String path = contentHelper.getFilePath(intent);
        if (path == null) {quitProcess(""); return;}

        if (!path.endsWith(".mp3") && !path.endsWith(".mp4") && !path.endsWith(".3ga")) {
            quitProcess("Format not supported");
            return;
        }

        song = new Song(this, path);
        song.solve();
        mainTextView.setText(song.toString());

        //turn off audio player..
        if (audioPlayer != null) audioPlayer.turnOff();
        //turn it on...
        audioPlayer = new AudioPlayer(song);
    }

    private void quitProcess(String str) {
        M.logger("Quited processing file..");
        M.toaster(this, "Failed to load file:\n"+str);
        mainTextView.setText("Failed to load file:\n"+str);
    }



}
