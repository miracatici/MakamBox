package datas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.JOptionPane;


public class CultureCreate {
	
	// Notalari oku NoteMap'e yaz
	// Ahenkleri oku AhenkMap'e yaz
	// Makamlari oku MakamMap'e yaz
	// Hepsini Culture objectine yaz
	// Culture objectini serial et
	private TreeMap<String,Note> notesMap ;
	private TreeMap<String,Makam> makamsMap ;
	private TreeMap<String,Ahenk> ahenksMap ;
	private TreeMap<String, float[]> intervalMap;
	
	public static void main (String[] arg){
		CultureCreate cc = new CultureCreate();
		cc.readIntervals("makams/turkishFull/arels.txt");
		cc.loadNote(new File("makams/turkishFull/turkNoteYeni.txt"));
		cc.loadAhenk(new File("makams/turkishFull/turkAhenkYeni.txt"));
		cc.loadMakam(new File("makams/turkishFull/turkMakamYeni.txt"));
		
		Culture cul = new Culture("TurkishCultureFull",cc.notesMap,cc.makamsMap,cc.ahenksMap);
		cul.serialize("TurkishCultureFull");
	}
	
	public void loadNote(File noteFile){
		if(noteFile.exists()){
			notesMap = new TreeMap<String,Note>();
			BufferedReader buffReader = null;
			String line;
			try {
				buffReader = new BufferedReader (new FileReader(noteFile));
				System.out.println("Buffered");
				int count = 0;
				while((line = buffReader.readLine())!=null){
					String[] temp = line.split("\\t");
					notesMap.put(temp[0], new Note(temp[0],Float.valueOf(temp[1])));
					count++;
					System.out.println(count);
				}
				buffReader.close();
				System.out.println("Buffered Done, Closed");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}

	public void loadMakam(File makamFile){
		if(makamFile.exists()){
			makamsMap = new TreeMap<String,Makam>();
			BufferedReader buffReader = null;
			String line;
			try {
				buffReader = new BufferedReader (new FileReader(makamFile));
				System.out.println("Buffered");
				int count = 0;
				while((line = buffReader.readLine())!=null){
					String[] temp = line.split("\\t");
					BufferedReader buffReader2 = new BufferedReader (new FileReader(temp[2]));
					String line2;
					float[] template = new float[3272];
					int i=0;
					while((line2 = buffReader2.readLine())!=null){
						template[i] = Float.valueOf(line2);
						i++;
					}
					buffReader2.close();
					makamsMap.put(temp[0], new Makam(temp[0],temp[1],temp[2],template,intervalMap.get(temp[0].toLowerCase())));
					count ++;
					System.out.println(count);
				}
				buffReader.close();
				System.out.println("Buffered Done, Closed");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}
	public void loadAhenk(File ahenkFile){
		if(ahenkFile.exists()){
			ahenksMap = new TreeMap<String,Ahenk>();
			BufferedReader buffReader = null;
			String line;
			try {
				buffReader = new BufferedReader (new FileReader(ahenkFile));
				System.out.println("Buffered");
				int count = 0;
				while((line = buffReader.readLine())!=null){
					String[] temp = line.split("\\t");
					ahenksMap.put(temp[0], new Ahenk(temp[0],temp[1],Float.valueOf(temp[2])));
					count++;
					System.out.println(count);
				}
				buffReader.close();
				System.out.println("Buffered Done, Closed");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}
	public void readIntervals(String intervalFile){
		intervalMap = new TreeMap<String,float[]>();
		try {
			String line;
			BufferedReader buffReader = new BufferedReader (new FileReader(intervalFile));
			System.out.println("Buffered");
			int count = 0;
			while((line = buffReader.readLine())!=null){
				String[] temp = line.split("\\t");
				float[] interval = new float[temp.length-1];
				for (int i = 1; i < temp.length; i++) {
					 interval[i-1] = Float.valueOf(temp[i]); 
				}
				intervalMap.put(temp[0], interval);
				count++;
				System.out.println(count);
			}
			buffReader.close();
			System.out.println("Buffered Close,Done");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
