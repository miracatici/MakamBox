package test;

import java.io.File;
import java.io.FilenameFilter;

import backEnd.PitchDetection;

public class MultiNoteSeq {

	public static void main (String[] args){
 		File[] files = new File("/Users/miracatici/Documents/workspace/MakamBox/notes/4luler").listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".wav")){
					return true;
				} else {
					return false;
				}
			}
 			
 		});
 		
 		System.out.println(files.length);
 		PitchDetection pd;
 		for (int i = 0; i < files.length; i++) {
			try {
				pd = new PitchDetection(files[i]);
				float[] res = pd.getChunkResults(4);
				String name = new String("n_4_"+Math.round(res[0]) + "_" + Math.round(res[1])+ "_" + Math.round(res[2])+ "_" + Math.round(res[3])+".wav");
				files[i].renameTo(new File(name));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
