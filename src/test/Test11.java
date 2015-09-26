package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import javazoom.jl.converter.Converter;
import utilities.AudioUtilities;
import backEnd.MakamBox;
import backEnd.WaveWriter;
import backEnd.Wavefile;

public class Test11 {
	public Converter converter;
	public File temp,mpeg;
	public MakamBox box;
	private JFrame fr;
	private JTextArea pane;
	public Test11(){
		framepack();
		File saba = new File("/Users/mirac/Documents/workspace/MakamToolBox/testDatasets/turkTestData/testData/01_saba_taksim.wav");
		Wavefile wave = null;
		try {
			wave = new Wavefile(saba);
			System.out.println(wave.getFormat());
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] raw = wave.getRawData();
		byte[] raw2 = AudioUtilities.convert(raw, raw.length, true, false, (int)wave.getSampleRate(), (int)wave.getSampleRate());
		
		AudioFormat af = new AudioFormat((int)wave.getSampleRate(), wave.getBitRate(), 1, true, false);
		try {
			Wavefile yeni = new Wavefile(af, raw2, "yeni.wav");
			WaveWriter write = new WaveWriter(yeni);
			write.toString();
			System.out.println(yeni.getFormat());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void framepack(){
		fr = new JFrame();
		fr.setPreferredSize(new Dimension(450,450));
		fr.setLayout(new BorderLayout());
		fr.setVisible(true);
		fr.setTitle("deneme");
		pane = new JTextArea();
		pane.setPreferredSize(new Dimension(70,140));
		fr.getContentPane().add(pane);
		fr.pack();
		
	}
	public static void main(String[] args){
		new Test11();
	}
}
