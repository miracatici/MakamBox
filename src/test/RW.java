package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import utilities.Plot;
import backEnd.MakamBox;

public class RW {
	private float[] data,data2;
	private BufferedReader reader;
	private BufferedWriter writer;
	private File temp;
	private MakamBox box;
	private JFrame fr;
	private JTextArea pane;
	
	public static void main(String[] args){
		new RW().process();
	}
	
	public void process(){
		framepack();
		try {
			box = new MakamBox("test.wav", null);
			data = box.getHistogramData();
			data2 = new float[data.length];
			System.out.println("Original data array : " + data.length);
			temp = File.createTempFile("temp", ".txt");
			temp.deleteOnExit();
			fileWrite(data,temp.getAbsolutePath());
			fileRead(data2,temp.getAbsolutePath());
			Plot.setNames("Mirac","Yapay");
			Plot.plot(data,data2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void fileWrite(float[] array, String path){
		try {
			writer = new BufferedWriter(new FileWriter(path));
			for (int i = 0; i < data.length; i++) {
				writer.write(new DecimalFormat("0.0000000000000000").format(array[i]));
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void fileRead(float[] array,String path){
		String line; int k=0;
		try {
			reader = new BufferedReader(new FileReader(path));
			while((line = reader.readLine())!=null){
				array[k] = Float.valueOf(line);
				++k;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		System.out.println("Read write data array : " + k);
	}
	public void framepack(){
		fr = new JFrame();
		fr.setPreferredSize(new Dimension(150,150));
		fr.setLayout(new BorderLayout());
		fr.setVisible(true);
		fr.setTitle("deneme");
		pane = new JTextArea();
		pane.setAutoscrolls(true);
		pane.setPreferredSize(new Dimension(70,140));
		fr.getContentPane().add(pane);
		fr.pack();
	}
}
