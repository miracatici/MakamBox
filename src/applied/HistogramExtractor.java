/** This file is part of MakamBox.

    MakamBox is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MakamBox is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MakamBox.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * MakamBox is an implementation of MakamToolBox Turkish Makam music analysis tool which is developed by Baris Bozkurt
 * This is the project of music tuition system that is a tool that helps users to improve their skills to perform music. 
 * This thesis aims at developing a computer based interactive tuition system specifically for Turkish music. 
 * 
 * Designed and implemented by @author Bilge Mirac Atici
 * Supervised by @supervisor Baris Bozkurt
 * Bahcesehir University, 2014-2015
 */


package applied;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import backEnd.TemplateCreate;

public class HistogramExtractor {

	public static JProgressBar progressBar;
	public static JLabel lblProgress;
	private JFrame frmMakamTemplateHistogram;
	private JTextField theoryPath;
	private JTextField songPath;
	private JButton btnTheoryLoad;
	private JButton btnFolderLoad;
	private JButton btnStartExtraction;
	private JLabel lblFolder;
	private JLabel lblInterval;
	private JFileChooser chooser;
	private File dir = new File("/Users/mirac/Documents/workspace/MakamToolBox/testDatasets/turkTestData");
	private JButton btnExit;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					HistogramExtractor window = new HistogramExtractor();
					window.frmMakamTemplateHistogram.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HistogramExtractor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMakamTemplateHistogram = new JFrame();
		frmMakamTemplateHistogram.getContentPane().setBackground(new Color(255, 248, 220));
		frmMakamTemplateHistogram.getContentPane().setLayout(null);
		
		lblInterval = new JLabel("Theoretical Makam Intervals");
		lblInterval.setBounds(6, 16, 177, 16);
		frmMakamTemplateHistogram.getContentPane().add(lblInterval);
		
		theoryPath = new JTextField();
		theoryPath.setBounds(6, 40, 177, 28);
		frmMakamTemplateHistogram.getContentPane().add(theoryPath);
		theoryPath.setColumns(10);
		
		btnTheoryLoad = new JButton("Load");
		btnTheoryLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				select(".txt",false);
				if(!(chooser.getSelectedFile()==null)){
					theoryPath.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnTheoryLoad.setBounds(183, 40, 75, 29);
		frmMakamTemplateHistogram.getContentPane().add(btnTheoryLoad);
		
		lblFolder = new JLabel("Audio File Folder");
		lblFolder.setBounds(6, 80, 106, 16);
		frmMakamTemplateHistogram.getContentPane().add(lblFolder);
		
		songPath = new JTextField();
		songPath.setColumns(10);
		songPath.setBounds(6, 108, 177, 28);
		frmMakamTemplateHistogram.getContentPane().add(songPath);
		
		btnFolderLoad = new JButton("Load");
		btnFolderLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				select("",true);
				if(!(chooser.getSelectedFile()==null)){
					songPath.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		btnFolderLoad.setBounds(183, 108, 75, 29);
		frmMakamTemplateHistogram.getContentPane().add(btnFolderLoad);
		
		btnStartExtraction = new JButton("Start Extraction");
		btnStartExtraction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					TemplateCreate creator = new TemplateCreate(new File(songPath.getText()),new File(theoryPath.getText()));						
					creator.createTemplates();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnStartExtraction.setBounds(270, 40, 129, 29);
		frmMakamTemplateHistogram.getContentPane().add(btnStartExtraction);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(6, 148, 252, 20);
		frmMakamTemplateHistogram.getContentPane().add(progressBar);
		
		lblProgress = new JLabel("");
		lblProgress.setBounds(270, 148, 129, 16);
		frmMakamTemplateHistogram.getContentPane().add(lblProgress);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(303, 75, 63, 29);
		frmMakamTemplateHistogram.getContentPane().add(btnExit);
		frmMakamTemplateHistogram.setTitle("Makam Template Histogram Extraction Tool v1.0");
		frmMakamTemplateHistogram.setResizable(false);
		frmMakamTemplateHistogram.setBounds(100, 100, 409, 210);
		frmMakamTemplateHistogram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private void select(String extension,boolean isFolder){ 
		chooser = new JFileChooser();
		chooser.setDialogTitle("Select an Data File");
		if(isFolder){
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else {			
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);			
		}
		chooser.setCurrentDirectory(dir);
		
		int x=chooser.showOpenDialog(null);
		if(x==JFileChooser.APPROVE_OPTION){
			while(!(chooser.getSelectedFile().getName().endsWith(extension))){
				JOptionPane.showMessageDialog(null,"Please select a supported file");
				x=chooser.showOpenDialog(null);
				if(x==JFileChooser.CANCEL_OPTION){
					break;
				}
			}
		}
		dir = chooser.getCurrentDirectory();
	}
}
