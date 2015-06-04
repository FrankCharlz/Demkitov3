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

    private static final String SP_FILE = "MusicData";
    private static final int ANDROID_POST_CODE =  1693;

    public static void saveSongInfo(Context context, String str) {
        context
                .getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(getTimeStamp(), str)
                .commit();
        trivialPostData(str);
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
