package com.mj.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MySharedPrefs {

    private static final String SP_FILE = "MusicData";

    public static void saveSongInfo(Context context, String str) {
        context
                .getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(getTimeStamp(), str)
                .commit();
    }

    public static String getTimeStamp() {
        return  new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
    }
}
