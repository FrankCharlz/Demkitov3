package com.mj.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ContentHelpers {


    /***
     * I will implement this using outputstream from the uri but not very soon
     */

    private Context context;
    private String fileName;

    public Context getContext() {
        return context;
    }

    public String getFileName() {
        return fileName;
    }

    public ContentHelpers(Context context) {
        this.context = context;
    }

    public boolean deleteFile() {
        return false;
    }

    public String getFilePath(Intent intent) {
        String result = getFilePathStreamWay(intent);
        if (result == null) result = getFilePathUriWay(intent);
        return result;
    }

    private String getFilePathStreamWay(Intent intent) {
        M.logger("STREAM WAY");
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri == null) return  null;

        String res = null;
        M.logger("uri string: "+uri.toString());
        res = uri.getPath();
        return res;
    }

    private String getFilePathUriWay(Intent intent) {
        M.logger("URI WAY");
        Uri uri = intent.getData();

        if (uri == null ) return null;

        String mime_type = context.getContentResolver().getType(uri);
        if (mime_type == null) return null;

        if(!mime_type.toLowerCase().contains("audio/mpeg"))  return null;//its is not mp3

        String fname = null, temp_name = null;
        M.logger("uri string: "+uri.toString());


        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        fname = cursor.getString(nameIndex);
        cursor.close();

        //dangerous file names...
        if (fname.length() < 4) return null;
        this.fileName = fname.trim()+".mp3";

        //failed to get path, so make tempo with name.
        try {
            File temp = File.createTempFile(fname,".mp3", context.getCacheDir());
            BufferedInputStream bis = new BufferedInputStream(context.getContentResolver().openInputStream(uri));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(temp));
            M.kopiInputToOutPutStreams(bis, bos);
            temp_name = temp.getAbsolutePath();
            M.logger("Temp path : "+temp_name);
            M.logger("fname : "+fname);
            return temp_name;
        } catch (IOException e) {
            M.logger("Failed to make temp file.");
            e.printStackTrace();
        }
        return  null;
    }



}
