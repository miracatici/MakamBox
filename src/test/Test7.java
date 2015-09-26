package test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import utilities.AudioUtilities;


public class Test7 {
	float[] temp;
	JFrame fr;
	JTextArea pane;
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
		float x =  AudioUtilities.hertzToCent(440);
		System.out.println(x);
		float y = AudioUtilities.centToHertz(x);
		System.out.println(y);
		
	}
	public static void main(String[] args) throws Exception{
		new Test7().start();
	}
}
