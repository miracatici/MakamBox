package test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import backEnd.MakamBox;
import backEnd.PitchDetection;
import utilities.Plot;

public class Test13 {
	public static void main(String[] args){
		try {
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
			
			MakamBox mb = new MakamBox("test1.wav", null);
			PitchDetection pd = mb.getPitchDetection();
			float[] pr = pd.getPitchResult();
			Plot.plot(pr);
			float[][] prc = pd.chunkPitchTrack(pr);
			Plot.addBar(pd.getChunkPosition());
			Plot.plot(prc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
