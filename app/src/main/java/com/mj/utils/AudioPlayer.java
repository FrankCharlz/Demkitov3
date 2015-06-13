package com.mj.utils;


import android.media.MediaPlayer;

import com.mj.demkito.Song;

import java.io.IOException;

public class AudioPlayer {

    MediaPlayer mediaPlayer;
    Song song;

    public AudioPlayer(Song song) {
        mediaPlayer = new MediaPlayer();
        this.song = song;
        try {
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            M.logger("Media player is good");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setCleanPath() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getCleanPath());
            mediaPlayer.prepare();
            M.logger("Media player is good");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (mediaPlayer == null) return;
        mediaPlayer.start();
    }

    public void stop() {
        if (mediaPlayer == null) return;
        mediaPlayer.seekTo(0);
        mediaPlayer.pause();
    }

    public  void turnOff() {
        if (mediaPlayer == null) return;
        mediaPlayer.release();
        mediaPlayer  = null;
    }
}
