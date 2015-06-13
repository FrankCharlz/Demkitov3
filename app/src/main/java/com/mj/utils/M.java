package com.mj.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class M {
    final static String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();
    public final static String DEMKITO_FOLDER = SD_CARD+"/Demkito/";
    public static int STUPID_HACK_ID = 0;
    public static int ANDROID_VERSION = Build.VERSION.SDK_INT;
    public static final String DEVICE_ID = randomString(8);//happy bday Kemmy $ Mtumwa

    public static void scaleArray(int[] array, int upper) {
        int max = 0, urefu = array.length;
        for (int x : array)
            max = (x > max) ? x : max;

        for (int i=0; i<urefu; i++)
            array[i] = (array[i]*upper)/max;

    }


    public static void kopiInputToOutPutStreams(BufferedInputStream bis, BufferedOutputStream fos) throws IOException {
        byte bafa[] = new byte[1024];
        int line;
        while ( (line= bis.read(bafa)) > 0) {
            fos.write(bafa, 0, 1024);

        }
    }

    public static void logger(String message) {
        Log.e("Demkito", message);
    }

    public static void toaster(Context context, String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }


    public static void checkAndCreateFolders() {
        if (isExternalStorageWritable()) {
            File demkito_folder = new File(DEMKITO_FOLDER);
            if (!demkito_folder.exists())
                logger("Made all folders down to Demkito : " + demkito_folder.mkdirs());
        } else {
            logger("Storage not readable...");
        }

    }


    public static int avarageInArray(int[] array, int start, int end) {
        int sum = 0;
        for (int i=start; i<end; i++) {
            sum += array[i];
        }
        return sum/(end-start+1);
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }



    public static String randomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int selector;
        for (int i=0; i<length; i++) {
            selector = random.nextInt(3);
            if (selector == 0)
                sb.append((random.nextInt(10)));
            if (selector == 1)
                sb.append((char)('A' + random.nextInt(26)));
            else
                sb.append((char)('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    public static void toaster(Context context, String str, int p) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }
}
