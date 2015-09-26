package test;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import utilities.AudioUtilities;
import utilities.Plot;
import backEnd.MakamBox;
import backEnd.PitchDetection;
import backEnd.Wavefile;


public class Test6 {
	float[] a;
	JFrame fr;
	JTextArea pane;
	JButton but;
	String message;
	Wavefile af;
	PitchDetection pi;
	File f;
	int count=0;
	
	public void object() throws Exception{
		MakamBox box = new MakamBox("/Users/mirac/Desktop/app/turkTestData/04Track4_huseyni.wav",null);
		float[] a1 = box.getHistogramData();
		float[] b1 = AudioUtilities.smootize(a1, 3);
		Plot.plot(a1,b1);
	}
	public void framepack(){
		fr = new JFrame();
		fr.setPreferredSize(new Dimension(450,450));
		fr.setLayout(new FlowLayout());
		fr.setVisible(true);
		fr.setTitle("deneme");
		but = new JButton("Sine");
		pane = new JTextArea();
		pane.setPreferredSize(new Dimension(70,140));
		fr.getContentPane().add(pane);
		fr.getContentPane().add(but);
		fr.pack();	
		
	}
	public void process() throws Exception{
		framepack();
		object();
	}
	public static void main(String[] args) throws Exception{
		new Test6().process();
	}
}