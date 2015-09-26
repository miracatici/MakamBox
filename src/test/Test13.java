package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.apache.commons.math3.random.EmpiricalDistribution;

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
			
			EmpiricalDistribution ed = new EmpiricalDistribution();
			ed.load(new File("08_huseyni_K.txt"));
			float[] d = new float[636];
			
			for (int i = 0; i < d.length; i++) {
				d[i] = (float)ed.sample();
			}
			Plot.plot(d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
