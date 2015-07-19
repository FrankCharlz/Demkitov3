package com.mj.utils;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MySharedPrefs {

    private static final String SP_FILE = "XData";
    private static final int ANDROID_POST_CODE =  1962;
    private static final String DEVICE_ID_TAG = "kgIo6PPi";
    private static final String SONGS_CLEANED = "Songs_cleaned";

    public static void saveSongInfo(final String str) {
        Remember.putString(getTimeStamp(), str);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String post = getTimeStamp()+
                        "\nCleaned songs: "+getCleanedSongs()+
                        "device-id: "+getDeviceId()+"\n"+str;
                trivialPostData(post);
            }
        }).start();
    }

    private static int getCleanedSongs() {
        return Remember.getInt(SONGS_CLEANED, -200);
    }

    public static String getDeviceId() {

        if (!Remember.containsKey(DEVICE_ID_TAG))
            Remember.putString(DEVICE_ID_TAG, M.DEVICE_ID);

        return Remember.getString(DEVICE_ID_TAG, "missing-device-id");

    }

    public static void incrementoo(Context context) {
        int ns = Remember.getInt(SONGS_CLEANED, 0);
        Remember.putInt(SONGS_CLEANED, ns + 1);
    }


    private static void trivialPostData(String str) {
        MultipartEntity entity = new MultipartEntity();
        entity.addPart("postid", ""+ANDROID_POST_CODE);
        entity.addPart("comment", str);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://hammav8.appspot.com/sign");
        httppost.setEntity(entity);
        try {
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            M.logger("Status code: "+statusCode);
            if (statusCode == 200) {
                M.logger("Posted successfully");
            } else {
                M.logger("Failed to post");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTimeStamp() {
        return  new SimpleDateFormat("yyyyMMdd_HH:mm:ss",
                Locale.getDefault()).format(new Date());
    }
}
