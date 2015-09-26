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
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import datas.Ahenk;
import datas.Culture;
import datas.Makam;
import datas.Note;

public class DataTool {

	private JFrame frmDatatool;
	private JTabbedPane tabbedPane;
	private JTextPane txtpnCulture;
	private JPanel makamPane,ahenkPane,culturePane,noteTrayPane;
	private JScrollPane guidePane,notesPane;
	private PrintWriter outFile;
	private TreeMap<String,Note> notesMap ;
	private TreeMap<String,Makam> makamsMap ;
	private TreeMap<String,Ahenk> ahenksMap ;
	private TreeMap<String,float[]> intervalMap;
	private Culture cultureData = new Culture();
	private JTextArea txtGuide;
	private JTextField[] noteNameList,noteRatioList,makamNameList,makamTonicList,
						 histNameList,ahenkNameList,ahenkBaseList,ahenkFreqList;
	private JFileChooser chooser;
	private File dir= new File("/Users/mirac/Desktop");
	
	private JTextField noteName1,noteRatio1,noteName2,noteRatio2,makamName1,
	tonic1,makamName2,tonic2,makamName3,tonic3,makamName4,tonic4,makamName5,
	tonic5,makamName6,tonic6,makamName7,tonic7, ahenkName6,ahenkBase6,ahenkFreq6,
	ahenkName7,ahenkBase7,ahenkFreq7,histPath1,histPath2,histPath3,histPath4,histPath5,
	histPath6,histPath7,txtCultureFileName,makamName9,tonic9,histPath9,makamName8,
	tonic8,histPath8,noteName8,noteName9,noteName10,noteName11,noteName12,noteName13,
	noteName14,noteRatio8,noteRatio9,noteRatio10,noteRatio11,noteRatio12,noteRatio13,
	noteRatio14,noteName16,noteName17,noteName18,noteRatio15,noteRatio16,noteRatio17,
	noteRatio18,noteName20,noteName21,noteName22,noteName23,noteName24,noteRatio19,
	noteRatio20,noteRatio21,noteRatio22,noteRatio23,noteRatio24,noteName25,
	noteName15,noteRatio25,noteName19,txtNoteFileName,txtAhenkFileName,txtMakamFileName,
	noteName3, noteRatio3,noteName4,noteRatio4,noteName5,noteRatio5,noteName6,noteRatio6,
	noteName7,noteRatio7,ahenkName1,ahenkBase1,ahenkFreq1,ahenkName2,ahenkBase2,ahenkFreq2,
	ahenkName3,ahenkBase3,ahenkFreq3,ahenkName4,ahenkBase4,ahenkFreq4,ahenkName5,ahenkBase5,
	ahenkFreq5;
	
	private JLabel noteLabel2,noteLabel3,noteLabel4,noteLabel5,noteLabel6,noteLabel7,
	lblMakamName,lblTonic,lblAhenkName,lblBaseNote,lblFrequency,lblRatios,lblNames,
	noteLabel1,lblHistogramPath,lblCultureName,noteLabel8,noteLabel9,noteLabel10,
	noteLabel11,noteLabel12,noteLabel13,noteLabel14,noteLabel15,noteLabel16,
	noteLabel17,noteLabel18,noteLabel19,noteLabel20,noteLabel21,noteLabel22,
	noteLabel23,noteLabel24,noteLabel25,lblAhenkFileName,lblMakamFileName,
	lblNoteFileName,lblMakamIntervalsName;
	
	private JButton notesSave,ahenkSave,makamSave,btnLoadNoteFile,btnLoadAhenkFile,
	btnLoadMakamFile,btnClearNote,btnClearAhenk,btnClearMakam,btnLoadCulture,btnLoad,
	btnLoad3,btnLoad4,btnLoad5,btnLoad6,btnLoad7,btnLoad8,btnLoad9,btnLoad2,btnSaveCulture,
	btnLoadIntervalsFile;
	private JTextField txtIntervalFile;
	
	private void select(String extension){ 
		chooser = new JFileChooser();
		chooser.setDialogTitle("Select an Data File");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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
	private void setNoteField(){
		noteNameList = new JTextField[]{noteName1,noteName2,noteName3,noteName4,
				noteName5,noteName6,noteName7,noteName8,noteName9,noteName10,noteName11,
				noteName12,noteName13,noteName14,noteName15,noteName16,noteName17,noteName18,
				noteName19,noteName20,noteName21,noteName22,noteName23,noteName24,noteName25};
		
		noteRatioList = new JTextField[]{noteRatio1,noteRatio2,noteRatio3,noteRatio4,
				noteRatio5,noteRatio6,noteRatio7,noteRatio8,noteRatio9,noteRatio10,noteRatio11,
				noteRatio12,noteRatio13,noteRatio14,noteRatio15,noteRatio16,noteRatio17,noteRatio18,
				noteRatio19,noteRatio20,noteRatio21,noteRatio22,noteRatio23,noteRatio24,noteRatio25};
	}
	private void setMakamField(){
		makamNameList = new JTextField[]{makamName1,makamName2,makamName3,makamName4,
				makamName5,makamName6,makamName7,makamName8,makamName9};
		makamTonicList = new JTextField[]{tonic1,tonic2,tonic3,tonic4,
				tonic5,tonic6,tonic7,tonic8,tonic9};
		histNameList = new JTextField[]{histPath1,histPath2,histPath3,histPath4,
				histPath5,histPath6,histPath7,histPath8,histPath9};
	}
	private void setAhenkField(){
		ahenkNameList = new JTextField[]{ahenkName1,ahenkName2,ahenkName3,ahenkName4,
				ahenkName5,ahenkName6,ahenkName7};
		
		ahenkBaseList = new JTextField[]{ahenkBase1,ahenkBase2,ahenkBase3,ahenkBase4,
				ahenkBase5,ahenkBase6,ahenkBase7};
		
		ahenkFreqList = new JTextField[]{ahenkFreq1,ahenkFreq2,ahenkFreq3,ahenkFreq4,
				ahenkFreq5,ahenkFreq6,ahenkFreq7};
	}
	private void saveNotes(){
		int reply = JOptionPane.showConfirmDialog(null, "Are you sure about values", "Is it correct?"
																		, JOptionPane.YES_NO_OPTION);
		if(reply == JOptionPane.YES_OPTION){
			notesMap = new  TreeMap<String, Note>();
		         
			String fileName;
			if(txtNoteFileName.getText().length()==0){
				fileName = "notes.txt";
			} else if(txtNoteFileName.getText().endsWith(".txt")){
				fileName = txtNoteFileName.getText();
			} else {
				fileName = txtNoteFileName.getText()+".txt";
			}
				
			try {
				outFile = new PrintWriter(new FileWriter(fileName));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			for(int i=0;i<noteNameList.length;i++){
				if(!(noteNameList[i].getText().length()==0)){				
					outFile.println(noteNameList[i].getText()+"\t"+noteRatioList[i].getText());
					notesMap.put(noteNameList[i].getText(), new Note(noteNameList[i].getText(),
																	Float.valueOf(noteRatioList[i].getText())));
				} 
			}
			outFile.close();
			notesMap = sortByValue(notesMap);
			cultureData.setNotesData(notesMap);
		}
	}
	private void saveMakams(){
		if(txtIntervalFile.getText().length()==0){
			JOptionPane.showMessageDialog(null,"A file that contains interval of makams must be selected."
					+ "\nIt's same with Theory File which is used in Histogram Extraction");
			return;
		} else {
			int reply = JOptionPane.showConfirmDialog(null, "Are you sure about values?", "Is it correct?"
																				, JOptionPane.YES_NO_OPTION);
			if(reply == JOptionPane.YES_OPTION){
				makamsMap = new TreeMap<String,Makam>();
				String fileName;
				if(txtMakamFileName.getText().length()==0){
					fileName = "makams.txt";
				} else if(txtMakamFileName.getText().endsWith(".txt")){
					fileName = txtMakamFileName.getText();
				} else {
					fileName = txtMakamFileName.getText()+".txt";
				}
				try {
					outFile = new PrintWriter(new FileWriter(fileName));
				} catch (IOException e) {
					e.printStackTrace();
				}
				readIntervals(txtIntervalFile.getText());
				for(int i=0;i<makamNameList.length;i++){
					if(!(makamNameList[i].getText().length()==0)){
						outFile.println(makamNameList[i].getText()+
									"\t"+makamTonicList[i].getText()+
									"\t"+histNameList[i].getText());
						Scanner infile = null;
						try {
							infile = new Scanner (new FileReader(histNameList[i].getText()));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						float[] template = new float[3272];
						for(int k=0;k<3272;k++){
							template[k] = Float.parseFloat(infile.nextLine());	
						}
						infile.close();
						makamsMap.put(makamNameList[i].getText(), new Makam(makamNameList[i].getText(),
																			makamTonicList[i].getText(),
																			histNameList[i].getText(),
																			template,
																			intervalMap.get(makamNameList[i].getText())));
					} 
				}
				outFile.close();
				cultureData.setMakamsData(makamsMap);
			}
		}
	}
	public void readIntervals(String intervalFile){
		intervalMap = new TreeMap<String,float[]>();
		try {
			String line;
			BufferedReader buffReader = new BufferedReader (new FileReader(intervalFile));
			while((line = buffReader.readLine())!=null){
				String[] temp = line.split("\\t");
				float[] interval = new float[temp.length-1];
				for (int i = 1; i < temp.length; i++) {
					 interval[i-1] = Float.valueOf(temp[i]); 
				}
				intervalMap.put(temp[0], interval);
			}
			buffReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void saveAhenks(){
		int reply = JOptionPane.showConfirmDialog(null, "Are you sure about values", "Is it correct?", 
																			JOptionPane.YES_NO_OPTION);
		if(reply == JOptionPane.YES_OPTION){
			ahenksMap = new TreeMap<String,Ahenk>();
			String fileName;
			if(txtAhenkFileName.getText().length()==0){
				fileName = "ahenks.txt";
			} else if(txtAhenkFileName.getText().endsWith(".txt")){
				fileName = txtAhenkFileName.getText();
			} else {
				fileName = txtAhenkFileName.getText()+".txt";
			}
			try {
				outFile = new PrintWriter(new FileWriter(fileName));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			for(int i=0;i<ahenkNameList.length;i++){
				if(!(ahenkNameList[i].getText().length()==0)){
					outFile.println(ahenkNameList[i].getText()+"\t"+ahenkBaseList[i].getText()
								+"\t"+ahenkFreqList[i].getText());
					ahenksMap.put(ahenkNameList[i].getText(), new Ahenk(ahenkNameList[i].getText(),
																		ahenkBaseList[i].getText(),
																		Float.valueOf(ahenkFreqList[i].getText())));
				}
			}
			outFile.close();
			cultureData.setAhenksData(ahenksMap);
		}
	}
	private void loadNote(){
		select(".txt");
		if(!(chooser.getSelectedFile()==null)){
			btnClearNote.doClick();
			Scanner infile = null;
			try {
				infile = new Scanner (new FileReader(chooser.getSelectedFile()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String[] line;
			txtNoteFileName.setText(chooser.getSelectedFile().getName());
			for(int k=0;infile.hasNextLine();k++){
				line = infile.nextLine().split("\t");
				noteNameList[k].setText(line[0]);
				noteRatioList[k].setText(line[1]);
			}
			infile.close();
		} else{
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}
	private void loadMakam(){
		select(".txt");
		if(!(chooser.getSelectedFile()==null)){
			btnClearMakam.doClick();
			Scanner infile = null;
			try {
				infile = new Scanner (new FileReader(chooser.getSelectedFile()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String[] line;
			txtMakamFileName.setText(chooser.getSelectedFile().getName());
			for(int k=0;infile.hasNextLine();k++){
				line = infile.nextLine().split("\t");
				makamNameList[k].setText(line[0]);
				makamTonicList[k].setText(line[1]);
				histNameList[k].setText(line[2]);
			}
			infile.close();
		} else {
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}
	private void loadAhenk(){
		select(".txt");
		if(!(chooser.getSelectedFile()==null)){
			btnClearAhenk.doClick();
			Scanner infile = null;
			try {
				infile = new Scanner (new FileReader(chooser.getSelectedFile()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String[] line;
			txtAhenkFileName.setText(chooser.getSelectedFile().getName());
			for(int k=0;infile.hasNextLine();k++){
				line = infile.nextLine().split("\t");
				ahenkNameList[k].setText(line[0]);
				ahenkBaseList[k].setText(line[1]);
				ahenkFreqList[k].setText(line[2]);
			}
			infile.close();
		} else {
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}
	private void loadCulture(){
		select(".ser");
		if(!(chooser.getSelectedFile()==null)){
			btnClearNote.doClick();
			btnClearAhenk.doClick();
			btnClearMakam.doClick();
			Culture selCulture = Culture.deserialize(chooser.getSelectedFile().getAbsolutePath());
			txtCultureFileName.setText(chooser.getSelectedFile().getName());
			if(!(selCulture.getNotesData()==null)){
				// Get a set of the entries for Notes
				Set<Map.Entry<String,Note>> set = selCulture.getNotesData().entrySet();
				// Get an iterator
				Iterator<Entry<String, Note>> it = set.iterator();
				// Display elements
				for(int i=0;it.hasNext();i++) {
					Map.Entry<String,Note> me = it.next();
					noteNameList[i].setText(me.getKey());
					noteRatioList[i].setText(String.valueOf(me.getValue().ratio));
				}
			} 
			if(!(selCulture.getAhenksData()==null)){
				// Get a set of the entries for Ahenks
				Set<Map.Entry<String,Ahenk>> set2 = selCulture.getAhenksData().entrySet();
				// Get an iterator
				Iterator<Entry<String, Ahenk>> it2 = set2.iterator();
				// Display elements
				for(int k=0;it2.hasNext();k++) {
					Map.Entry<String,Ahenk> me = it2.next();
					ahenkNameList[k].setText(me.getKey());
					ahenkBaseList[k].setText(me.getValue().baseNote);
					ahenkFreqList[k].setText(String.valueOf(me.getValue().baseFreq));
				}
			} 
			if(!(selCulture.getMakamsData()==null)){
				// Get a set of the entries for Makams
				Set<Map.Entry<String,Makam>> set3 = selCulture.getMakamsData().entrySet();
				// Get an iterator
				Iterator<Entry<String, Makam>> it3 = set3.iterator();
				// Display elements
				for(int j=0;it3.hasNext();j++) {
					Map.Entry<String,Makam> me = it3.next();
					makamNameList[j].setText(me.getKey());
					makamTonicList[j].setText(me.getValue().getTonicNote());
					histNameList[j].setText(me.getValue().getHistogramPath());
				}
			} 
		} else {
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}
	private void loadIntervals(){
		select(".txt");
		if(!(chooser.getSelectedFile()==null)){
			txtIntervalFile.setText(chooser.getSelectedFile().getAbsolutePath());
		} else {
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DataTool window = new DataTool();
					window.frmDatatool.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public DataTool() {
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDatatool = new JFrame();
		frmDatatool.setTitle("DataTool v1.0 Culture-Specific Setting Creator");
		frmDatatool.setResizable(false);
		frmDatatool.setBounds(100, 100, 495, 404);
		frmDatatool.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmDatatool.getContentPane().setLayout(null);
		
		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setAutoscrolls(true);
		tabbedPane.setBounds(0, 0, 498, 390);
		frmDatatool.getContentPane().add(tabbedPane);
		
		txtGuide = new JTextArea("\t       Welcome DataTool 1.0 \n\nThis software is a tool that can help user to input culture-specific settings for  MakamBox, culture-specific music analysis software.\n\nThis process contains 3 parts. First part is Note Settings. In this part, user will specify the name of notes and frequency ratios with respect to the first note. This information serves as a theoretical reference which helps initialization of intonation analysis. For example, frequency ratio of A5 with reference to A4 is 2. \n\nFor the 12-tet tuning system, the note list can be specified as:\n\nC - 1.00   \t\nC# - 1.059 \nD - 1.122  (ratio wrt first note)\nD# - 1.189 (ratio wrt first note)\n.\n.\n.\nC - 2.00   (Octave)\n\nSecond part is the Ahenk Settings. Ahenk refers to key transposition (Ex: Bb key used for some woodwind instruments). User will specify the ahenk name, reference note and frequency of the reference note for each ahenk. This data will be used for key transposition (when pitch shifting needs to be applied).\n\nThird part is the Makam Settings. For each makam, a template histogram file is needed, which is computed/learned from recordings using another tool. The user needs to specify the text file path containing the histogram. Also, each makam has a tonic note, \"karar\". This note may be called as \"ending note\" or \"starting note\". With this note, MakamBox compute shifting amount to transpose to user-specified ahenk.\n\nAt the end of the process, user needs to save all settings with given name to a Java SER file. It'll be used to set culture specific features of the MakamBox. The name of this file must have contain only letters and no spaces. The DataTool saves the file to folder where DataTool is located.\n");
		txtGuide.setOpaque(false);
		txtGuide.setDragEnabled(false);
		txtGuide.setEditable(false);
		txtGuide.setBorder(null);
		txtGuide.setBounds(6, 6, 427, 320);
		txtGuide.setLineWrap(true);
		txtGuide.setWrapStyleWord(true);

		noteTrayPane = new JPanel();
		noteTrayPane.setPreferredSize(new Dimension(352, 880));
		noteTrayPane.setLayout(null);
		noteTrayPane.setOpaque(false);
		noteTrayPane.setBorder(null);

		guidePane = new JScrollPane(txtGuide);
		notesPane = new JScrollPane(noteTrayPane);
		makamPane = new JPanel();
		ahenkPane = new JPanel();
		culturePane = new JPanel();
	
		guidePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		guidePane.getViewport().setOpaque(false);
		guidePane.setBorder(null);
		guidePane.setOpaque(false);

		notesPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		notesPane.getViewport().setOpaque(false);
		notesPane.setBorder(null);
		notesPane.setOpaque(false);
		
		ahenkPane.setLayout(null);
		ahenkPane.setBorder(null);
		ahenkPane.setOpaque(false);
		makamPane.setLayout(null);
		makamPane.setBorder(null);
		makamPane.setOpaque(false);
		culturePane.setLayout(null);
		culturePane.setOpaque(false);
		culturePane.setBorder(null);
		
		tabbedPane.addTab("Guide", null, guidePane, null);
		tabbedPane.addTab("Notes", null, notesPane, null);
		
		tabbedPane.addTab("Ahenks", null, ahenkPane, null);
		tabbedPane.addTab("Makams", null, makamPane, null);
		tabbedPane.addTab("Save Culture-Specific Settings", null, culturePane, null);
		
		lblNames = new JLabel("Names");
		lblNames.setBounds(79, 6, 61, 16);
		lblRatios = new JLabel("Ratios");
		lblRatios.setBounds(158, 6, 61, 16);
		
		noteLabel1 = new JLabel("Note 1");
		noteLabel1.setBounds(6, 35, 61, 16);
		
		noteName1 = new JTextField();
		noteName1.setDragEnabled(false);
		noteName1.setBounds(79, 29, 67, 28);
		
		noteRatio1 = new JTextField();
		noteRatio1.setDragEnabled(false);
		noteRatio1.setColumns(10);
		noteRatio1.setBounds(158, 29, 67, 28);
		
		noteLabel2 = new JLabel("Note 2");
		noteLabel2.setBounds(6, 69, 61, 16);
		
		noteName2 = new JTextField();
		noteName2.setDragEnabled(false);
		noteName2.setColumns(10);
		noteName2.setBounds(79, 63, 67, 28);
		
		noteRatio2 = new JTextField();
		noteRatio2.setDragEnabled(false);
		noteRatio2.setColumns(10);
		noteRatio2.setBounds(158, 63, 67, 28);
		
		noteLabel3 = new JLabel("Note 3");
		noteLabel3.setBounds(6, 103, 61, 16);
		
		noteName3 = new JTextField();
		noteName3.setDragEnabled(false);
		noteName3.setColumns(10);
		noteName3.setBounds(79, 97, 67, 28);
		
		noteRatio3 = new JTextField();
		noteRatio3.setDragEnabled(false);
		noteRatio3.setColumns(10);
		noteRatio3.setBounds(158, 97, 67, 28);
		
		noteLabel4 = new JLabel("Note 4");
		noteLabel4.setBounds(6, 137, 61, 16);
		
		noteName4 = new JTextField();
		noteName4.setDragEnabled(false);
		noteName4.setColumns(10);
		noteName4.setBounds(79, 131, 67, 28);
		
		noteRatio4 = new JTextField();
		noteRatio4.setDragEnabled(false);
		noteRatio4.setColumns(10);
		noteRatio4.setBounds(158, 131, 67, 28);
		
		noteLabel5 = new JLabel("Note 5");
		noteLabel5.setBounds(6, 171, 61, 16);
		
		noteName5 = new JTextField();
		noteName5.setDragEnabled(false);
		noteName5.setColumns(10);
		noteName5.setBounds(79, 165, 67, 28);
		
		noteRatio5 = new JTextField();
		noteRatio5.setDragEnabled(false);
		noteRatio5.setColumns(10);
		noteRatio5.setBounds(158, 165, 67, 28);
		
		noteLabel6 = new JLabel("Note 6");
		noteLabel6.setBounds(6, 205, 61, 16);
		
		noteName6 = new JTextField();
		noteName6.setDragEnabled(false);
		noteName6.setColumns(10);
		noteName6.setBounds(79, 199, 67, 28);
		
		noteRatio6 = new JTextField();
		noteRatio6.setDragEnabled(false);
		noteRatio6.setColumns(10);
		noteRatio6.setBounds(158, 199, 67, 28);
		
		noteLabel7 = new JLabel("Note 7");
		noteLabel7.setBounds(6, 239, 61, 16);
		
		noteName7 = new JTextField();
		noteName7.setDragEnabled(false);
		noteName7.setColumns(10);
		noteName7.setBounds(79, 233, 67, 28);
		
		noteRatio7 = new JTextField();
		noteRatio7.setDragEnabled(false);
		noteRatio7.setColumns(10);
		noteRatio7.setBounds(158, 233, 67, 28);
		
		noteTrayPane.add(lblNames);
		noteTrayPane.add(lblRatios);
		noteTrayPane.add(noteName1);
		noteTrayPane.add(noteName2);
		noteTrayPane.add(noteName3);
		noteTrayPane.add(noteName4);
		noteTrayPane.add(noteName5);
		noteTrayPane.add(noteName6);
		noteTrayPane.add(noteName7);
		noteTrayPane.add(noteRatio1);
		noteTrayPane.add(noteRatio2);
		noteTrayPane.add(noteRatio3);
		noteTrayPane.add(noteRatio4);
		noteTrayPane.add(noteRatio5);
		noteTrayPane.add(noteRatio6);
		noteTrayPane.add(noteRatio7);
		noteTrayPane.add(noteLabel1);
		noteTrayPane.add(noteLabel2);
		noteTrayPane.add(noteLabel3);
		noteTrayPane.add(noteLabel4);
		noteTrayPane.add(noteLabel5);
		noteTrayPane.add(noteLabel6);
		noteTrayPane.add(noteLabel7);
		
		notesSave = new JButton("Save to File");
		notesSave.setBounds(228, 132, 103, 29);
		notesSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveNotes();
			}
		});
		noteTrayPane.add(notesSave);
		
		noteName8 = new JTextField();
		noteName8.setDragEnabled(false);
		noteName8.setBounds(79, 268, 67, 28);
		noteTrayPane.add(noteName8);
		
		noteName9 = new JTextField();
		noteName9.setColumns(10);
		noteName9.setBounds(79, 302, 67, 28);
		noteTrayPane.add(noteName9);
		
		noteName10 = new JTextField();
		noteName10.setColumns(10);
		noteName10.setBounds(79, 336, 67, 28);
		noteTrayPane.add(noteName10);
		
		noteName11 = new JTextField();
		noteName11.setColumns(10);
		noteName11.setBounds(79, 370, 67, 28);
		noteTrayPane.add(noteName11);
		
		noteName12 = new JTextField();
		noteName12.setColumns(10);
		noteName12.setBounds(79, 404, 67, 28);
		noteTrayPane.add(noteName12);
		
		noteName13 = new JTextField();
		noteName13.setColumns(10);
		noteName13.setBounds(79, 438, 67, 28);
		noteTrayPane.add(noteName13);
		
		noteName14 = new JTextField();
		noteName14.setColumns(10);
		noteName14.setBounds(79, 472, 67, 28);
		noteTrayPane.add(noteName14);
		
		noteRatio8 = new JTextField();
		noteRatio8.setDragEnabled(false);
		noteRatio8.setColumns(10);
		noteRatio8.setBounds(158, 268, 67, 28);
		noteTrayPane.add(noteRatio8);
		
		noteRatio9 = new JTextField();
		noteRatio9.setColumns(10);
		noteRatio9.setBounds(158, 302, 67, 28);
		noteTrayPane.add(noteRatio9);
		
		noteRatio10 = new JTextField();
		noteRatio10.setColumns(10);
		noteRatio10.setBounds(158, 336, 67, 28);
		noteTrayPane.add(noteRatio10);
		
		noteRatio11 = new JTextField();
		noteRatio11.setColumns(10);
		noteRatio11.setBounds(158, 370, 67, 28);
		noteTrayPane.add(noteRatio11);
		
		noteRatio12 = new JTextField();
		noteRatio12.setColumns(10);
		noteRatio12.setBounds(158, 404, 67, 28);
		noteTrayPane.add(noteRatio12);
		
		noteRatio13 = new JTextField();
		noteRatio13.setColumns(10);
		noteRatio13.setBounds(158, 438, 67, 28);
		noteTrayPane.add(noteRatio13);
		
		noteRatio14 = new JTextField();
		noteRatio14.setColumns(10);
		noteRatio14.setBounds(158, 472, 67, 28);
		noteTrayPane.add(noteRatio14);
		
		noteLabel8 = new JLabel("Note 8");
		noteLabel8.setBounds(6, 274, 61, 16);
		noteTrayPane.add(noteLabel8);
		
		noteLabel9 = new JLabel("Note 9");
		noteLabel9.setBounds(6, 308, 61, 16);
		noteTrayPane.add(noteLabel9);
		
		noteLabel10 = new JLabel("Note 10");
		noteLabel10.setBounds(6, 342, 61, 16);
		noteTrayPane.add(noteLabel10);
		
		noteLabel11 = new JLabel("Note 11");
		noteLabel11.setBounds(6, 376, 61, 16);
		noteTrayPane.add(noteLabel11);
		
		noteLabel12 = new JLabel("Note 12");
		noteLabel12.setBounds(6, 410, 61, 16);
		noteTrayPane.add(noteLabel12);
		
		noteLabel13 = new JLabel("Note 13");
		noteLabel13.setBounds(6, 444, 61, 16);
		noteTrayPane.add(noteLabel13);
		
		noteLabel14 = new JLabel("Note 14");
		noteLabel14.setBounds(6, 478, 61, 16);
		noteTrayPane.add(noteLabel14);
		
		noteName15 = new JTextField();
		noteName15.setDragEnabled(false);
		noteName15.setColumns(10);
		noteName15.setBounds(79, 506, 67, 28);
		noteTrayPane.add(noteName15);
		
		noteName16 = new JTextField();
		noteName16.setDragEnabled(false);
		noteName16.setColumns(10);
		noteName16.setBounds(79, 540, 67, 28);
		noteTrayPane.add(noteName16);
		
		noteName17 = new JTextField();
		noteName17.setDragEnabled(false);
		noteName17.setColumns(10);
		noteName17.setBounds(79, 574, 67, 28);
		noteTrayPane.add(noteName17);
		
		noteName18 = new JTextField();
		noteName18.setDragEnabled(false);
		noteName18.setColumns(10);
		noteName18.setBounds(79, 608, 67, 28);
		noteTrayPane.add(noteName18);
		
		noteRatio15 = new JTextField();
		noteRatio15.setDragEnabled(false);
		noteRatio15.setColumns(10);
		noteRatio15.setBounds(158, 506, 67, 28);
		noteTrayPane.add(noteRatio15);
		
		noteRatio16 = new JTextField();
		noteRatio16.setDragEnabled(false);
		noteRatio16.setColumns(10);
		noteRatio16.setBounds(158, 540, 67, 28);
		noteTrayPane.add(noteRatio16);
		
		noteRatio17 = new JTextField();
		noteRatio17.setDragEnabled(false);
		noteRatio17.setColumns(10);
		noteRatio17.setBounds(158, 574, 67, 28);
		noteTrayPane.add(noteRatio17);
		
		noteRatio18 = new JTextField();
		noteRatio18.setDragEnabled(false);
		noteRatio18.setColumns(10);
		noteRatio18.setBounds(158, 608, 67, 28);
		noteTrayPane.add(noteRatio18);
		
		noteLabel15 = new JLabel("Note 15");
		noteLabel15.setBounds(6, 512, 61, 16);
		noteTrayPane.add(noteLabel15);
		
		noteLabel16 = new JLabel("Note 16");
		noteLabel16.setBounds(6, 546, 61, 16);
		noteTrayPane.add(noteLabel16);
		
		noteLabel17 = new JLabel("Note 17");
		noteLabel17.setBounds(6, 580, 61, 16);
		noteTrayPane.add(noteLabel17);
		
		noteLabel18 = new JLabel("Note 18");
		noteLabel18.setBounds(6, 614, 61, 16);
		noteTrayPane.add(noteLabel18);
		
		noteName19 = new JTextField();
		noteName19.setDragEnabled(false);
		noteName19.setBounds(79, 643, 67, 28);
		noteTrayPane.add(noteName19);
		
		noteName20 = new JTextField();
		noteName20.setDragEnabled(false);
		noteName20.setColumns(10);
		noteName20.setBounds(79, 677, 67, 28);
		noteTrayPane.add(noteName20);
		
		noteName21 = new JTextField();
		noteName21.setDragEnabled(false);
		noteName21.setColumns(10);
		noteName21.setBounds(79, 711, 67, 28);
		noteTrayPane.add(noteName21);
		
		noteName22 = new JTextField();
		noteName22.setDragEnabled(false);
		noteName22.setColumns(10);
		noteName22.setBounds(79, 745, 67, 28);
		noteTrayPane.add(noteName22);
		
		noteName23 = new JTextField();
		noteName23.setDragEnabled(false);
		noteName23.setColumns(10);
		noteName23.setBounds(79, 779, 67, 28);
		noteTrayPane.add(noteName23);
		
		noteName24 = new JTextField();
		noteName24.setDragEnabled(false);
		noteName24.setColumns(10);
		noteName24.setBounds(79, 813, 67, 28);
		noteTrayPane.add(noteName24);
		
		noteRatio19 = new JTextField();
		noteRatio19.setDragEnabled(false);
		noteRatio19.setColumns(10);
		noteRatio19.setBounds(158, 643, 67, 28);
		noteTrayPane.add(noteRatio19);
		
		noteRatio20 = new JTextField();
		noteRatio20.setDragEnabled(false);
		noteRatio20.setColumns(10);
		noteRatio20.setBounds(158, 677, 67, 28);
		noteTrayPane.add(noteRatio20);
		
		noteRatio21 = new JTextField();
		noteRatio21.setDragEnabled(false);
		noteRatio21.setColumns(10);
		noteRatio21.setBounds(158, 711, 67, 28);
		noteTrayPane.add(noteRatio21);
		
		noteRatio22 = new JTextField();
		noteRatio22.setDragEnabled(false);
		noteRatio22.setColumns(10);
		noteRatio22.setBounds(158, 745, 67, 28);
		noteTrayPane.add(noteRatio22);
		
		noteRatio23 = new JTextField();
		noteRatio23.setDragEnabled(false);
		noteRatio23.setColumns(10);
		noteRatio23.setBounds(158, 779, 67, 28);
		noteTrayPane.add(noteRatio23);
		
		noteRatio24 = new JTextField();
		noteRatio24.setDragEnabled(false);
		noteRatio24.setColumns(10);
		noteRatio24.setBounds(158, 813, 67, 28);
		noteTrayPane.add(noteRatio24);
		
		noteLabel19 = new JLabel("Note 19");
		noteLabel19.setBounds(6, 649, 61, 16);
		noteTrayPane.add(noteLabel19);
		
		noteLabel20 = new JLabel("Note 20");
		noteLabel20.setBounds(6, 683, 61, 16);
		noteTrayPane.add(noteLabel20);
		
		noteLabel21 = new JLabel("Note 21");
		noteLabel21.setBounds(6, 717, 61, 16);
		noteTrayPane.add(noteLabel21);
		
		noteLabel22 = new JLabel("Note 22");
		noteLabel22.setBounds(6, 751, 61, 16);
		noteTrayPane.add(noteLabel22);
		
		noteLabel23 = new JLabel("Note 23");
		noteLabel23.setBounds(6, 785, 61, 16);
		noteTrayPane.add(noteLabel23);
		
		noteLabel24 = new JLabel("Note 24");
		noteLabel24.setBounds(6, 819, 61, 16);
		noteTrayPane.add(noteLabel24);
		
		noteLabel25 = new JLabel("Note 25");
		noteLabel25.setBounds(6, 853, 61, 16);
		noteTrayPane.add(noteLabel25);
		
		noteName25 = new JTextField();
		noteName25.setDragEnabled(false);
		noteName25.setColumns(10);
		noteName25.setBounds(79, 847, 67, 28);
		noteTrayPane.add(noteName25);
		
		noteRatio25 = new JTextField();
		noteRatio25.setDragEnabled(false);
		noteRatio25.setColumns(10);
		noteRatio25.setBounds(158, 847, 67, 28);
		noteTrayPane.add(noteRatio25);
		
		btnLoadNoteFile = new JButton("Load Note File");
		btnLoadNoteFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadNote();
			}
		});
		btnLoadNoteFile.setBounds(228, 29, 117, 29);
		noteTrayPane.add(btnLoadNoteFile);
		
		txtNoteFileName = new JTextField();
		txtNoteFileName.setDragEnabled(false);
		txtNoteFileName.setBounds(231, 97, 134, 28);
		noteTrayPane.add(txtNoteFileName);
		txtNoteFileName.setColumns(10);
		
		lblNoteFileName = new JLabel("Note File Name");
		lblNoteFileName.setBounds(233, 75, 103, 16);
		noteTrayPane.add(lblNoteFileName);
		
		btnClearNote = new JButton("Clear All");
		btnClearNote.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtNoteFileName.setText("");
				for (int i = 0; i < noteNameList.length; i++) {
					noteNameList[i].setText("");
					noteRatioList[i].setText("");
				}
			}
		});
		btnClearNote.setBounds(228, 166, 103, 28);
		noteTrayPane.add(btnClearNote);

		lblAhenkName = new JLabel("Names");
		lblAhenkName.setBounds(12, 6, 61, 16);
		ahenkPane.add(lblAhenkName);
		
		lblBaseNote = new JLabel("Base Note");
		lblBaseNote.setBounds(91, 6, 75, 16);
		ahenkPane.add(lblBaseNote);
		
		ahenkName1 = new JTextField();
		ahenkName1.setColumns(10);
		ahenkName1.setBounds(6, 24, 83, 28);
		ahenkPane.add(ahenkName1);
		
		ahenkBase1 = new JTextField();
		ahenkBase1.setColumns(10);
		ahenkBase1.setBounds(99, 24, 67, 28);
		ahenkPane.add(ahenkBase1);
		
		ahenkName2 = new JTextField();
		ahenkName2.setColumns(10);
		ahenkName2.setBounds(6, 58, 83, 28);
		ahenkPane.add(ahenkName2);
		
		ahenkBase2 = new JTextField();
		ahenkBase2.setColumns(10);
		ahenkBase2.setBounds(99, 58, 67, 28);
		ahenkPane.add(ahenkBase2);
		
		ahenkName3 = new JTextField();
		ahenkName3.setColumns(10);
		ahenkName3.setBounds(6, 92, 83, 28);
		ahenkPane.add(ahenkName3);
		
		ahenkBase3 = new JTextField();
		ahenkBase3.setColumns(10);
		ahenkBase3.setBounds(99, 92, 67, 28);
		ahenkPane.add(ahenkBase3);
		
		ahenkName4 = new JTextField();
		ahenkName4.setColumns(10);
		ahenkName4.setBounds(6, 126, 83, 28);
		ahenkPane.add(ahenkName4);
		
		ahenkBase4 = new JTextField();
		ahenkBase4.setColumns(10);
		ahenkBase4.setBounds(99, 126, 67, 28);
		ahenkPane.add(ahenkBase4);
		
		ahenkName5 = new JTextField();
		ahenkName5.setColumns(10);
		ahenkName5.setBounds(6, 160, 83, 28);
		ahenkPane.add(ahenkName5);
		
		ahenkBase5 = new JTextField();
		ahenkBase5.setColumns(10);
		ahenkBase5.setBounds(99, 160, 67, 28);
		ahenkPane.add(ahenkBase5);
		
		ahenkName6 = new JTextField();
		ahenkName6.setColumns(10);
		ahenkName6.setBounds(6, 194, 83, 28);
		ahenkPane.add(ahenkName6);
		
		ahenkBase6 = new JTextField();
		ahenkBase6.setColumns(10);
		ahenkBase6.setBounds(99, 194, 67, 28);
		ahenkPane.add(ahenkBase6);
		
		ahenkName7 = new JTextField();
		ahenkName7.setColumns(10);
		ahenkName7.setBounds(6, 228, 83, 28);
		ahenkPane.add(ahenkName7);
		
		ahenkBase7 = new JTextField();
		ahenkBase7.setColumns(10);
		ahenkBase7.setBounds(99, 228, 67, 28);
		ahenkPane.add(ahenkBase7);
		
		lblFrequency = new JLabel("Frequency");
		lblFrequency.setBounds(178, 6, 75, 16);
		ahenkPane.add(lblFrequency);
		
		ahenkFreq1 = new JTextField();
		ahenkFreq1.setColumns(10);
		ahenkFreq1.setBounds(186, 23, 67, 28);
		ahenkPane.add(ahenkFreq1);
		
		ahenkFreq2 = new JTextField();
		ahenkFreq2.setColumns(10);
		ahenkFreq2.setBounds(186, 58, 67, 28);
		ahenkPane.add(ahenkFreq2);
		
		ahenkFreq3 = new JTextField();
		ahenkFreq3.setColumns(10);
		ahenkFreq3.setBounds(186, 92, 67, 28);
		ahenkPane.add(ahenkFreq3);
		
		ahenkFreq4 = new JTextField();
		ahenkFreq4.setColumns(10);
		ahenkFreq4.setBounds(186, 126, 67, 28);
		ahenkPane.add(ahenkFreq4);
		
		ahenkFreq5 = new JTextField();
		ahenkFreq5.setColumns(10);
		ahenkFreq5.setBounds(186, 160, 67, 28);
		ahenkPane.add(ahenkFreq5);
		
		ahenkFreq6 = new JTextField();
		ahenkFreq6.setColumns(10);
		ahenkFreq6.setBounds(186, 194, 67, 28);
		ahenkPane.add(ahenkFreq6);
		
		ahenkFreq7 = new JTextField();
		ahenkFreq7.setColumns(10);
		ahenkFreq7.setBounds(186, 228, 67, 28);
		ahenkPane.add(ahenkFreq7);
		
		lblMakamName = new JLabel("Makam Name");
		lblMakamName.setBounds(6, 6, 93, 16);
		
		lblTonic = new JLabel("Tonic Note");
		lblTonic.setBounds(99, 6, 83, 16);
		
		makamName1 = new JTextField();
		makamName1.setDragEnabled(false);
		makamName1.setColumns(10);
		makamName1.setBounds(6, 29, 83, 28);
		
		tonic1 = new JTextField();
		tonic1.setDragEnabled(false);
		tonic1.setColumns(10);
		tonic1.setBounds(99, 29, 67, 28);
		
		makamName2 = new JTextField();
		makamName2.setDragEnabled(false);
		makamName2.setColumns(10);
		makamName2.setBounds(6, 63, 83, 28);
		
		tonic2 = new JTextField();
		tonic2.setDragEnabled(false);
		tonic2.setColumns(10);
		tonic2.setBounds(99, 63, 67, 28);
		
		makamName3 = new JTextField();
		makamName3.setDragEnabled(false);
		makamName3.setColumns(10);
		makamName3.setBounds(6, 97, 83, 28);
		
		tonic3 = new JTextField();
		tonic3.setDragEnabled(false);
		tonic3.setColumns(10);
		tonic3.setBounds(99, 97, 67, 28);
		
		makamName4 = new JTextField();
		makamName4.setDragEnabled(false);
		makamName4.setColumns(10);
		makamName4.setBounds(6, 131, 83, 28);
		
		tonic4 = new JTextField();
		tonic4.setDragEnabled(false);
		tonic4.setColumns(10);
		tonic4.setBounds(99, 131, 67, 28);
		
		makamName5 = new JTextField();
		makamName5.setDragEnabled(false);
		makamName5.setColumns(10);
		makamName5.setBounds(6, 165, 83, 28);
		
		tonic5 = new JTextField();
		tonic5.setDragEnabled(false);
		tonic5.setColumns(10);
		tonic5.setBounds(99, 165, 67, 28);
		
		makamName6 = new JTextField();
		makamName6.setDragEnabled(false);
		makamName6.setColumns(10);
		makamName6.setBounds(6, 199, 83, 28);
		
		tonic6 = new JTextField();
		tonic6.setDragEnabled(false);
		tonic6.setColumns(10);
		tonic6.setBounds(99, 199, 67, 28);
		
		makamName7 = new JTextField();
		makamName7.setDragEnabled(false);
		makamName7.setColumns(10);
		makamName7.setBounds(6, 233, 83, 28);
		
		tonic7 = new JTextField();
		tonic7.setDragEnabled(false);
		tonic7.setColumns(10);
		tonic7.setBounds(99, 233, 67, 28);
		
		makamPane.add(makamName1);
		makamPane.add(makamName2);
		makamPane.add(makamName3);
		makamPane.add(makamName4);
		makamPane.add(makamName5);
		makamPane.add(makamName6);
		makamPane.add(makamName7);
		
		makamPane.add(tonic1);
		makamPane.add(tonic2);
		makamPane.add(tonic3);
		makamPane.add(tonic4);
		makamPane.add(tonic5);
		makamPane.add(tonic6);
		makamPane.add(tonic7);
		
		makamPane.add(lblMakamName);
		makamPane.add(lblTonic);
		
		ahenkSave = new JButton("Save to File");
		ahenkSave.setBounds(265, 144, 103, 29);
		ahenkSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAhenks();
			}
		});
		ahenkPane.add(ahenkSave);
		
		btnLoadAhenkFile = new JButton("Load Ahenk File");
		btnLoadAhenkFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadAhenk();
			}
		});
		btnLoadAhenkFile.setBounds(265, 59, 127, 29);
		ahenkPane.add(btnLoadAhenkFile);
		
		lblAhenkFileName = new JLabel("Ahenk File Name");
		lblAhenkFileName.setBounds(270, 90, 125, 16);
		ahenkPane.add(lblAhenkFileName);
		
		txtAhenkFileName = new JTextField();
		txtAhenkFileName.setColumns(10);
		txtAhenkFileName.setBounds(268, 112, 134, 28);
		ahenkPane.add(txtAhenkFileName);
		
		btnClearAhenk = new JButton("Clear All");
		btnClearAhenk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtAhenkFileName.setText("");
				for (int i = 0; i < ahenkNameList.length; i++) {
					ahenkNameList[i].setText("");
					ahenkBaseList[i].setText("");
					ahenkFreqList[i].setText("");
				}
			}
		});
		btnClearAhenk.setBounds(265, 173, 103, 28);
		ahenkPane.add(btnClearAhenk);
				
		makamSave = new JButton("Save to File");
		makamSave.setBounds(337, 253, 103, 29);
		makamSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveMakams();
			}
		});
		makamPane.add(makamSave);	
		
		lblHistogramPath = new JLabel("Histogram Paths");
		lblHistogramPath.setBounds(178, 6, 113, 16);
		makamPane.add(lblHistogramPath);
		
		histPath1 = new JTextField();
		histPath1.setDragEnabled(false);
		histPath1.setColumns(10);
		histPath1.setBounds(178, 29, 85, 28);
		makamPane.add(histPath1);
		
		histPath2 = new JTextField();
		histPath2.setDragEnabled(false);
		histPath2.setColumns(10);
		histPath2.setBounds(178, 63, 85, 28);
		makamPane.add(histPath2);
		
		histPath3 = new JTextField();
		histPath3.setDragEnabled(false);
		histPath3.setColumns(10);
		histPath3.setBounds(178, 97, 85, 28);
		makamPane.add(histPath3);
		
		histPath4 = new JTextField();
		histPath4.setDragEnabled(false);
		histPath4.setColumns(10);
		histPath4.setBounds(178, 131, 85, 28);
		makamPane.add(histPath4);
		
		histPath5 = new JTextField();
		histPath5.setDragEnabled(false);
		histPath5.setColumns(10);
		histPath5.setBounds(178, 165, 85, 28);
		makamPane.add(histPath5);
		
		histPath6 = new JTextField();
		histPath6.setDragEnabled(false);
		histPath6.setColumns(10);
		histPath6.setBounds(178, 199, 85, 28);
		makamPane.add(histPath6);
		
		histPath7 = new JTextField();
		histPath7.setDragEnabled(false);
		histPath7.setColumns(10);
		histPath7.setBounds(178, 233, 85, 28);
		makamPane.add(histPath7);
		
		makamName9 = new JTextField();
		makamName9.setDragEnabled(false);
		makamName9.setColumns(10);
		makamName9.setBounds(6, 298, 83, 28);
		makamPane.add(makamName9);
		
		tonic9 = new JTextField();
		tonic9.setDragEnabled(false);
		tonic9.setColumns(10);
		tonic9.setBounds(99, 298, 67, 28);
		makamPane.add(tonic9);
		
		histPath9 = new JTextField();
		histPath9.setDragEnabled(false);
		histPath9.setColumns(10);
		histPath9.setBounds(178, 298, 85, 28);
		makamPane.add(histPath9);
		
		makamName8 = new JTextField();
		makamName8.setDragEnabled(false);
		makamName8.setColumns(10);
		makamName8.setBounds(6, 264, 83, 28);
		makamPane.add(makamName8);
		
		tonic8 = new JTextField();
		tonic8.setDragEnabled(false);
		tonic8.setColumns(10);
		tonic8.setBounds(99, 264, 67, 28);
		makamPane.add(tonic8);
		
		histPath8 = new JTextField();
		histPath8.setDragEnabled(false);
		histPath8.setColumns(10);
		histPath8.setBounds(178, 264, 85, 28);
		makamPane.add(histPath8);
		
		btnLoadMakamFile = new JButton("Load Makam File");
		btnLoadMakamFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadMakam();
			}
		});
		btnLoadMakamFile.setBounds(337, 165, 130, 29);
		makamPane.add(btnLoadMakamFile);
		
		lblMakamFileName = new JLabel("Makam File Name");
		lblMakamFileName.setBounds(342, 195, 125, 16);
		makamPane.add(lblMakamFileName);
		
		txtMakamFileName = new JTextField();
		txtMakamFileName.setDragEnabled(false);
		txtMakamFileName.setColumns(10);
		txtMakamFileName.setBounds(340, 217, 134, 28);
		makamPane.add(txtMakamFileName);
		
		btnClearMakam = new JButton("Clear All");
		btnClearMakam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtMakamFileName.setText("");
				for (int i = 0; i < makamNameList.length; i++) {
					makamNameList[i].setText("");
					makamTonicList[i].setText("");
					histNameList[i].setText("");
				}
			}
		});
		btnClearMakam.setBounds(337, 278, 103, 28);
		makamPane.add(btnClearMakam);
		
		btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath1.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad.setBounds(258, 29, 67, 29);
		makamPane.add(btnLoad);
		
		btnLoad2 = new JButton("Load");
		btnLoad2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath2.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad2.setBounds(258, 63, 67, 29);
		makamPane.add(btnLoad2);
		
		btnLoad3 = new JButton("Load");
		btnLoad3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath3.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad3.setBounds(258, 97, 67, 29);
		makamPane.add(btnLoad3);
		
		btnLoad4 = new JButton("Load");
		btnLoad4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath4.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad4.setBounds(258, 131, 67, 29);
		makamPane.add(btnLoad4);
		
		btnLoad5 = new JButton("Load");
		btnLoad5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath5.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad5.setBounds(258, 165, 67, 29);
		makamPane.add(btnLoad5);
		
		btnLoad6 = new JButton("Load");
		btnLoad6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath6.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad6.setBounds(258, 199, 67, 29);
		makamPane.add(btnLoad6);
		
		btnLoad7 = new JButton("Load");
		btnLoad7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath7.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad7.setBounds(258, 233, 67, 29);
		makamPane.add(btnLoad7);
		
		btnLoad8 = new JButton("Load");
		btnLoad8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath8.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad8.setBounds(258, 264, 67, 29);
		makamPane.add(btnLoad8);
		
		btnLoad9 = new JButton("Load");
		btnLoad9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				select(".txt");
				if(!(chooser.getSelectedFile()==null)){
					histPath9.setText(chooser.getSelectedFile().getAbsolutePath());	
				} else {
					JOptionPane.showMessageDialog(null,"File not choosen");
				}
			}
		});
		btnLoad9.setBounds(258, 298, 67, 29);
		makamPane.add(btnLoad9);
		
		btnLoadIntervalsFile = new JButton("Load Intervals");
		btnLoadIntervalsFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadIntervals();
			}
		});
		btnLoadIntervalsFile.setBounds(337, 45, 130, 29);
		makamPane.add(btnLoadIntervalsFile);
		
		lblMakamIntervalsName = new JLabel("Makam Intervals");
		lblMakamIntervalsName.setBounds(342, 75, 125, 16);
		makamPane.add(lblMakamIntervalsName);
		
		txtIntervalFile = new JTextField();
		txtIntervalFile.setDragEnabled(false);
		txtIntervalFile.setColumns(10);
		txtIntervalFile.setBounds(340, 97, 134, 28);
		makamPane.add(txtIntervalFile);
		
		txtCultureFileName = new JTextField();
		txtCultureFileName.setDragEnabled(false);
		txtCultureFileName.setBounds(222, 10, 134, 28);
		culturePane.add(txtCultureFileName);
		txtCultureFileName.setColumns(10);
		
		lblCultureName = new JLabel("Culture Name");
		lblCultureName.setBounds(120, 16, 90, 16);
		culturePane.add(lblCultureName);
		
		btnSaveCulture = new JButton("Save Culture-Specific Settings");
		btnSaveCulture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(txtCultureFileName.getText().length()==0){
					JOptionPane.showMessageDialog(null, "Please specify a setting name");
				} else if(txtCultureFileName.getText().endsWith(".ser")){
					cultureData.setName(txtCultureFileName.getText());
					cultureData.serialize(txtCultureFileName.getText());
				} else{
					cultureData.setName(txtCultureFileName.getText()+".ser");
					cultureData.serialize(txtCultureFileName.getText());
				}
			}
		});
		btnSaveCulture.setBounds(52, 46, 218, 29);
		culturePane.add(btnSaveCulture);
		
		txtpnCulture = new JTextPane();
		txtpnCulture.setOpaque(false);
		txtpnCulture.setBackground(new Color(238, 238, 238));
		txtpnCulture.setText("   *   Culture Name must have only letter and no spaces\n\t(Exp : Makam, Dastgah,Turkish,Tunusian)\n\n   *   Setting File will be saved to default path\n\n   *   Default Path is application directory\n\n   *   You can load a culture settings file (*.ser) to make changes on it.\n\n   *\t1. Load a culture setting file.\n \t2. Click the tab which you'll make changes\n \t3. Click save button on specified tab\n \t4. Clik save culture setting file button on this tab");
		txtpnCulture.setBounds(20, 87, 440, 229);
		culturePane.add(txtpnCulture);
		
		btnLoadCulture = new JButton("Load Settings");
		btnLoadCulture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadCulture();
			}
		});
		btnLoadCulture.setBounds(335, 46, 116, 29);
		culturePane.add(btnLoadCulture);
		
		setNoteField();
		setMakamField();
		setAhenkField();
	}
	@SuppressWarnings("unchecked")
	private TreeMap<String, Note> sortByValue(TreeMap<String, Note> unsortedMap) {
		@SuppressWarnings("rawtypes")
		TreeMap<String, Note> sortedMap = new TreeMap(new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}	
}
@SuppressWarnings("rawtypes")
class ValueComparator implements Comparator, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TreeMap<String,Note> noteSorting;
	TreeMap<String,Ahenk> ahenkSorting;
	public ValueComparator(TreeMap<String, Note> map) {
		noteSorting = map;
	}
	@Override
	public int compare(Object keyA, Object keyB) {
		return noteSorting.get(keyA).ratio.compareTo(noteSorting.get(keyB).ratio);
	}
}