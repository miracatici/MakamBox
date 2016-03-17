package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

public class Freks {

	public static void main(String[] args) {
		try {
			File folder = new File("/Users/miracatici/Documents/workspace/appClips/mix/4luler");
			File[] files = folder.listFiles(new FilenameFilter(){
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
			BufferedReader text = new BufferedReader(new FileReader("freks4.txt"));
			for (int i = 0; i < files.length; i++) {
				String[] line = text.readLine().split("\t");
				String name = "n_4_"+line[0]+"_"+line[1]+"_"+line[2]+"_"+line[3]+".wav";
				System.out.println(name);
				files[i].renameTo(new File(name));
			}
			text.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
