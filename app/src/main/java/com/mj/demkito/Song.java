package com.mj.demkito;

import com.mj.cheapgoogle.CheapMP3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Song  extends CheapMP3 {

    private String name;
    private String path;

    private final File songfile;
    private int[] frame_volumes;

    //range of five seconds ndo ads zao zilipo
    static int cut_0 = (int)(4000/26) ;
    static int cut_1 = (int)(10000/26) ;

    //in case ur fucked just cut here...
    static int the_cut_frame = (int)(7000/26);

    private int min_volume;
    private boolean isSolved = false;

    public Song(String path) {
        this.songfile = new File(path);
        this.name = getFileName(path);
        this.path = path;
    }

    private String getFileName(String path) {
       int f = path.lastIndexOf("/");
       return (f > 0) ? path.substring(f, path.length()) : "Failed to get file name";

    }

    public void solve() {
        if (!(songfile.canRead() && songfile.exists() && songfile.isFile())){
            M.logger("File is in accessible");
            return;
        }

        try {
            //cheapmp3 to read file..
            super.ReadFile(songfile);

            //array of frame volumes
            frame_volumes = getFrameGains();

            M.logger("Number of frames inspected for volume : "+ (cut_1 - cut_0));
            //initialize to default volume of cut place

            min_volume = frame_volumes[the_cut_frame];
            for (int x = cut_0; x < cut_1; x++) {
                if (frame_volumes[x] < min_volume) {
                    min_volume = frame_volumes[x];
                    the_cut_frame = x;
                }
            }

            M.logger("Min volume : "+min_volume+" The cutting frame : "+the_cut_frame);
            isSolved = true;
        } catch (IOException e) {
            M.logger("File is unreadable by Google :"+e.getLocalizedMessage());
            e.printStackTrace();
        }

    }


    public boolean removeAds(File cleanFile) {
        try {
            super.WriteFile(cleanFile, the_cut_frame, getNumFrames() - the_cut_frame);
            return true;
        } catch (IOException e) {
            M.logger("Failed to write new file :" +e.getLocalizedMessage());
            e.printStackTrace();
        }

        return false;

    }




    public String toString() {
        if (!isSolved) {
            return "File not read yet.";
        }

        String r = "\n"+
                "name : "+name + "\n" +
                "path : "+path + "\n" +
                "size in kb : "+getFileSizeBytes()/1024 + "\n" +
                "bitrate : "+getAvgBitrateKbps() + "\n" +
                "sample rate : "+getSampleRate()+ "\n" +
                "cutting point: "+the_cut_frame+ "\n"+
                hashCode()+"\n"+
                "---------------";
        return  r;
    }

    public ArrayList<String> getSongInfo() {
        ArrayList<String> res = new ArrayList<String>();
        res.add("Name: "+name);
        res.add("Size: "+getFileSizeBytes() / (float)(1024 * 1024));
        res.add("Path: "+path);
        return res;
    }


    public boolean isValid() {
        return isSolved;
    }

    public String getName() {
        return name;
    }
}
