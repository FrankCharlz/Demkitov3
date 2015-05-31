package com.mj.demkito;

public class Song {

	//for uri and file
	static String path;
	static String pathx;
	static String name;
	static String namex;
	static boolean isMp3 = false;
	static boolean hasAds = false;

	//for demkitoling
	static int bitrate, size, sample_rate, sample_per_frame, num_frames;
	static int[] frameVolumes;
	static int cut_0 = (int)(4000/26) ; 
	static int cut_1 = (int)(10000/26) ; //range of five seconds
	static int the_cut_frame = (int)(7000/26); //in case ur fucked just cut here...
	static int min_volume, beforeCutPoint, afterCutPoint; //talkin abt volumes

	private Song() {
		//private constructor disable instanciation
	}

	public static void solveFile() {
		//getting the volumes of the chopped five seconds
		//and taking the smallest volume as the cutting point...
		M.logger("Number of frames inspected for volume : "+ (cut_1 - cut_0));
		min_volume = frameVolumes[the_cut_frame]; //initialize to default volume of cut place
		for (int x = cut_0; x < cut_1; x++) {
			if (frameVolumes[x] < min_volume) {
				min_volume = frameVolumes[x];
				the_cut_frame = x;
			}
		}
		M.logger("Min volume : "+min_volume+" The cut frame : "+the_cut_frame);

		beforeCutPoint = M.avarageInArray(frameVolumes, the_cut_frame-3, the_cut_frame) - min_volume;
		afterCutPoint = M.avarageInArray(frameVolumes, the_cut_frame+1, the_cut_frame+3) - min_volume;

		hasAds =  (beforeCutPoint > 50);
			
			

	}

	public static int[] getSongGraph(int bars) {
		int number_of_frames = frameVolumes.length;
		int leap = number_of_frames/bars;
		int result[] = new int[bars];

		number_of_frames -= number_of_frames%bars;

		int i,k=0;
		for (i=0; i<number_of_frames; i+=leap) {
			result[k++] = frameVolumes[i];
		}

		return result;

	}


    public static String userString() {
        String r = "\n"+
                "name : "+name + "\n" +
                "path : "+path + "\n" +
                "size in kb : "+size/1024 + "\n" +
                "has Ads : "+hasAds+ "\n" +
                "bitrate : "+bitrate + "\n" +
                "sample rate : "+sample_rate + "\n" +
                "cutting point: "+the_cut_frame + "\n"+
                "vol before : "+beforeCutPoint + "\n"+
                "vol after : "+afterCutPoint + "\n"+
                "---------------";
        return  r;
    }
}