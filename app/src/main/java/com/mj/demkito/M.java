package com.mj.demkito;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class M {
	final static String SD_CARD = Environment.getExternalStorageDirectory().getAbsolutePath();
	final static String DEMKITO_FOLDER = SD_CARD+"/Demkito/";
	public static int STUPID_HACK_ID = 0;
	public static int ANDROID_VERSION = Build.VERSION.SDK_INT;
	
	public static void scaleArray(int[] array, int upper) {
		int max = 0, urefu = array.length;
		for (int x : array)
			max = (x > max) ? x : max;
		
		for (int i=0; i<urefu; i++) 
			array[i] = (array[i]*upper)/max;			
			
	}

	private void kopiZeFaili(File fi, File fo) {
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(fi);
			fos = new FileOutputStream(fo);

			try {
				byte[] bafa = new byte[1024];
				int l;
				while ( (l= fis.read(bafa)) > 0) {
					fos.write(bafa, 0, 1024);

				}

				fis.close();
				fos.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	public static void logger(String message) {
		Log.e("DEMKITO", message);
	}
	
	public static  char  randomChar(Random rn) {
		if (rn.nextBoolean())
			return (char) (rn.nextInt(26) + 'a');
		else 
			return (char) (rn.nextInt(26) + 'A');
	}

	public static void toaster(Context context, String str){
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();	
	}


	public static void checkAndCreateFolders() {
		if (isExternalStorageWritable()) {
			File myvaste_desa_folder = new File(DEMKITO_FOLDER);
			if (!myvaste_desa_folder.exists()) 
				Log.e("1993", "Made all folders down to DESA : "+myvaste_desa_folder.mkdirs());
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

    public static void prepareBar(AppCompatActivity activity, int color) {
        ActionBar bar = activity.getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(color));
    }
}
