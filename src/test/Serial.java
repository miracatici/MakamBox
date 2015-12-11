package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Serial {
	private HashMap<String, float[]> results;
	int noteContain = 4 ;
	String type = "n";
	public Serial(){
		results = new HashMap<String, float[]>();
	}
	public void process(){
		File folder = new File("/Users/miracatici/Documents/workspace/MakamDroid/assets/audio/n/4/");
		File[] files = folder.listFiles();
		System.out.println(files.length);
		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName();
			String name2 = name.replace(".mp3", "");
			float[] tempRes = new float[noteContain];	
			String[] nameSplit = name2.split("_");
			for (int j = 0; j < noteContain; j++) {
				tempRes[j] = Float.valueOf(nameSplit[j+2]);
			}
			results.put(name, tempRes);
		}
		serialize(type+"_"+String.valueOf(noteContain),results);
	}
	public void serialize(String name, Object obj){
		System.out.println("Start");	
		FileOutputStream fileOut;
		ObjectOutputStream out;
		try {
			fileOut = new FileOutputStream(name+".ser");
			out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
			System.out.println("Done");	
		} catch (FileNotFoundException e) {
			System.out.println("Error1");	
		} catch (IOException e) {
			System.out.println("Error2");	
		}
	}
	@SuppressWarnings("unchecked")
	public static HashMap<String, float[]> deserialize(String path){
		FileInputStream fileIn;
        ObjectInputStream in;
        HashMap<String, float[]> newCulture = null;
		try {	        
			fileIn = new FileInputStream(path);
	        in = new ObjectInputStream(fileIn);
	        newCulture = (HashMap<String, float[]> ) in.readObject();
	        in.close();
	        fileIn.close();
			JOptionPane.showMessageDialog(null, "Object data file is read");
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null,"Object data isn't read");
		}
		return newCulture;
	}
	public static void main (String[] args){
		new Serial().process();
	}
}
