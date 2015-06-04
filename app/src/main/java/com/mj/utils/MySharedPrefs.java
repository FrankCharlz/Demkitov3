package com.mj.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
    private static final int ANDROID_POST_CODE =  1693;
    private static final String DEVICE_ID_TAG = "kIoPPi";

    public static void saveSongInfo(final Context context, final String str) {
        context
                .getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(getTimeStamp(), str)
                .commit();

        //first thread from the mainThread; to post to hammav8
        new Thread(new Runnable() {
            @Override
            public void run() {
                trivialPostData("device-id:"+getDeviceId(context)+"\n"+str);
            }
        }).start();
    }

    public static String getDeviceId(Context context) {

        SharedPreferences sp = context.getSharedPreferences(SP_FILE, 0);

        if (!sp.contains(DEVICE_ID_TAG))
            sp.edit().putString(DEVICE_ID_TAG, M.DEVICE_ID).commit();

        return sp.getString(DEVICE_ID_TAG, "missing-device-id");

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
        return  new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
    }
}
