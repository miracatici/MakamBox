package test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import backEnd.Histogram;
import backEnd.PitchDetection;
import backEnd.Wavefile;
import utilities.AudioUtilities;
import utilities.Plot;

public class AndroHist {
	public static void main(String[] args){
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
		try {
			PitchDetection pd = new PitchDetection(new Wavefile("test.wav"));
			System.out.println(1);
			Histogram h = new Histogram(pd);
			System.out.println(2);
			float[] pe = h.getPeaksCent();
			for (int i = 0; i < pe.length; i++) {
				System.out.println(AudioUtilities.centToHertz(pe[i]));
			}
			Plot.plot(pd.getPitchResult());
			System.out.println(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
