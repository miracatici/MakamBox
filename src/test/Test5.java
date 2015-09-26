package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import backEnd.Histogram;
import backEnd.MakamBox;
import backEnd.MakamClassifier;
import backEnd.PitchDetection;
import backEnd.Wavefile;

public class Test5 {
	Wavefile af;
	PitchDetection pi;
	Histogram hist;
	MakamClassifier m;
	MakamBox box;
	JFrame fr;
	JTextArea pane;
	PrintWriter outFile;
	String res;
	
	public void start() throws Exception{
		framepack();
		process();	
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
	public void process() throws Exception{
		File folder = new File("/Users/mirac/Documents/workspace/MakamToolBox/testWavMat/");
		File[] files = folder.listFiles(new FileFilter(){
			@Override
			public boolean accept(File arg0) {
				if(arg0.getName().endsWith(".wav")){
					return true;
				}
				return false;
			}	
		});
		outFile = new PrintWriter(new FileWriter("totalFromWav.csv"));
		for(int i=0;i<files.length;i++){
			box = new MakamBox(files[i],null);
//			box.showHistWithTemp();
			res = box.getName()+"\t"+box.getMakamName();
//			pane.append("\n"+res);
			outFile.println(res);
		}
		outFile.close();
		System.out.println("Done");
	}
	public static void main(String[] args) throws Exception {
		new Test5().start();
		
	}
}
