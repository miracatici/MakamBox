package datas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import applied.SelectCulture;
import backEnd.MakamBox;
import utilities.Plot;

public class TuningSystemCreate {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		JFrame fr = new JFrame();
		fr.setPreferredSize(new Dimension(100,25));
		fr.setLayout(new BorderLayout());
		fr.setVisible(true);
		fr.setTitle("deneme");
		JTextArea pane = new JTextArea();
		pane.setAutoscrolls(true);
		pane.setPreferredSize(new Dimension(70,140));
		fr.getContentPane().add(pane);
		fr.pack();
		
		SelectCulture.setCulture(Culture.deserialize("settings/TurkishExtended.ser"));

		TuningSystem karadeniz_41 = new TuningSystem(new File("makams/theoreticalData/Karadeniz_41.txt"));
		TuningSystem yarman_21 = new TuningSystem(new File("makams/theoreticalData/Yarman_21.txt"));
		TuningSystem yarman_24 = new TuningSystem(new File("makams/theoreticalData/Yarman_24.txt"));
		TuningSystem yarman_79 = new TuningSystem(new File("makams/theoreticalData/Yarman_79.txt"));
		TuningSystem yavuzoglu_48 = new TuningSystem(new File("makams/theoreticalData/Yavuzoglu_48.txt"));
		TuningSystem arel = new TuningSystem(new File("makams/theoreticalData/AEUIntervals2.txt"));
		
		karadeniz_41.writeSystemToFile("karadeniz.txt");
		yarman_21.writeSystemToFile("yarman21.txt");
		yarman_24.writeSystemToFile("yarman24.txt");
		yarman_79.writeSystemToFile("yarman79.txt");
		yavuzoglu_48.writeSystemToFile("yavuzoglu48.txt");
		arel.writeSystemToFile("arel.txt");

		TreeMap<String,TuningSystem> systems = new TreeMap<String,TuningSystem>();
		
		systems.put("Arel_Ezgi_Uzdilek", arel);
		systems.put("Karadeniz_41", karadeniz_41);
		systems.put("Yarman_21", yarman_21);
		systems.put("Yarman_24", yarman_24);
		systems.put("Yarman_79", yarman_79);
		systems.put("Yavuzoglu_48", yavuzoglu_48);
		
		MakamBox rec=null;
		try {
			rec = new MakamBox("04Track4_huseyni.wav",null);
			rec.createMakam("Huseyni");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		FileOutputStream fileOut;
		ObjectOutputStream out;
		String name = "TuningSystems";
		try {
			fileOut = new FileOutputStream(name+".ser");
			out = new ObjectOutputStream(fileOut);
			out.writeObject(systems);
			out.close();
			fileOut.close();
			JOptionPane.showMessageDialog(null, "Tuning System data file is created");
			System.out.println("Serialized data is saved in /"+name+".ser");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileInputStream fileIn;
		ObjectInputStream in;

		TreeMap<String,TuningSystem> newSystems = null;
		try{
			fileIn = new FileInputStream("TuningSystems.ser");
	        in = new ObjectInputStream(fileIn);
	        newSystems = (TreeMap<String,TuningSystem>) in.readObject();
	        in.close();
	        fileIn.close();
			JOptionPane.showMessageDialog(null, "Tuning Systems file is read");
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null,"Please select a file");
		}
		TuningSystem t = newSystems.get("Arel_Ezgi_Uzdilek");
		TreeMap<String, Makam> a = t.getMakamsMap();
		Makam b = null;
		try {
			b = a.get(rec.getMakamName());
			System.out.println(b.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		float[] temp = b.getIntervalArray();
		Plot.plot(temp);

	}
	
}
