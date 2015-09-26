package test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import applied.SelectCulture;
import datas.Culture;

public class Test4 {
	
	public static void main (String[] args) throws Exception{
		JFrame fr = new JFrame();
		JTextArea pane = new JTextArea();
		fr.setPreferredSize(new Dimension(150,150));
		fr.setLayout(new BorderLayout());
		fr.setVisible(true);
		fr.setTitle("deneme");
		fr.getContentPane().add(pane);
		fr.pack();
				
		Culture cult = SelectCulture.getCulture();
		cult.getMakamsData();
		
//		Wavefile af;
//		PitchDetection pi;
//		Histogram hist;
//		MakamClassifier m;
//		File f;
		
//		f = new File("test2/single.txt");
//		af = new Wavefile(f);
//		pi = new PitchDetection(af);
//		hist = new Histogram(pi);
//		m = new MakamClassifier(hist);
//		m.measure();
//		pane.append(m.getName()+"=="+m.getMakamName()+"\n");
	}
}
