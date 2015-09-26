package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import backEnd.TemplateCreate;

public class Test12 {
	public static void main(String[] args){
		new Test12().start();
	}
	public void start(){
		framepack();
		process();
	}
	public void framepack(){
		JFrame fr = new JFrame();
		fr.setPreferredSize(new Dimension(450,450));
		fr.setLayout(new BorderLayout());
		fr.setVisible(true);
		fr.setTitle("deneme");
		JTextArea pane = new JTextArea();
		pane.setAutoscrolls(true);
		pane.setPreferredSize(new Dimension(70,140));
		fr.getContentPane().add(pane);
		fr.pack();
		
	}
	public void process(){
		String path = "/Users/mirac/Documents/workspace/MakamToolBox/testDatasets/turkTestData/testDar";
		File dir = new File(path);
		File theo = new File("/Users/mirac/Documents/workspace/MakamToolBox/makams/arels.txt");
		TemplateCreate temp = new TemplateCreate(dir,theo);
		temp.createTemplates();		
	}
}
