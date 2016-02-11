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
import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import backEnd.MakamBox;
import datas.Culture;
import datas.Makam;
import utilities.AudioUtilities;

public class MultiAnalysis {

	public static JProgressBar progressBar;
	public JFrame frmMultiTrack;
	private JTextField songPath;
	private JButton btnFolderLoad;
	private JButton btnStartExtraction;
	private JLabel lblFolder;
	private JFileChooser chooser;
	private File dir = new File("/Users/mirac/Documents/workspace/MakamToolBox/testDatasets/turkTestData");
	private JButton btnExit;
	private JComboBox<String> comboBox;
	private JTextField txtDegre;
	private JLabel lblMinumumValue;
	private JLabel lblMaximumValue;
	private JLabel lblMin;
	private JLabel lblMax;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				SelectCulture.setCulture(setList("TurkishExtended"));
				MultiAnalysis m = new MultiAnalysis();
				m.frmMultiTrack.setVisible(true);
				m.frmMultiTrack.repaint();
				m.frmMultiTrack.pack();;
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MultiAnalysis() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMultiTrack = new JFrame();
		frmMultiTrack.getContentPane().setBackground(new Color(255, 248, 220));
		frmMultiTrack.getContentPane().setLayout(null);
		frmMultiTrack.setTitle("Multi Track Analysis v1.0");
		frmMultiTrack.setMinimumSize(new Dimension(445, 210));
		frmMultiTrack.setBounds(100, 100, 445, 210);
		frmMultiTrack.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		lblFolder = new JLabel("Audio File Folder");
		lblFolder.setBounds(6, 6, 106, 16);
		frmMultiTrack.getContentPane().add(lblFolder);
		
		songPath = new JTextField();
		songPath.setColumns(10);
		songPath.setBounds(6, 34, 177, 28);
		frmMultiTrack.getContentPane().add(songPath);
		
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
		btnFolderLoad.setBounds(183, 34, 75, 29);
		frmMultiTrack.getContentPane().add(btnFolderLoad);
		
		btnStartExtraction = new JButton("Start Extraction");
		btnStartExtraction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				progressBar.setStringPainted(true);
				progressBar.setString("Starting");
				final TreeSet<Float> ts = new TreeSet<Float>();
				try {
					File folder = new File(songPath.getText());
					final File[] files = folder.listFiles(new FileFilter() {		
						@Override
						public boolean accept(File pathname) {
							if(pathname.getName().endsWith(".mp3") || pathname.getName().endsWith(".wav") ){
								return true;
							} else {
								return false;
							}
						}
					});
					if(files.length == 0){
						JOptionPane.showMessageDialog(null,"Folder is empty, or no supported media");
					} else {
						progressBar.setString("Processing");
						new Thread(new Runnable(){
							@Override
							public void run() {
								progressBar.setMaximum(files.length - 1);
								for (int i = 0; i < files.length; i++) {
									if (files[i].getName().endsWith(".mp3")) {
										files[i] = AudioUtilities.convertMP3toWAV(files[i]);
									}
									MakamBox mb = null; float[] peaks =  null;
									try {
										mb = new MakamBox(files[i],null);
										mb.createMakam((String) comboBox.getSelectedItem());
//										mb.showHistWithTemp();
//										mb.showIntervalChart();
										peaks = mb.getHistogram().getPeaksCent();
										int degree = 0; int tonicPos = mb.getMakam().getTonicInPeaks();
										try { 
											degree = Integer.valueOf(txtDegre.getText());
//											System.out.println(files[i].getName() + " " + tonicPos);
											ts.add((peaks[tonicPos + degree-1] - mb.getTonicCent()));
											progressBar.setValue(i);
										} catch (Exception a){
											JOptionPane.showMessageDialog(null,"Please write only number");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								lblMin.setText(String.valueOf(Collections.min(ts)));
								lblMax.setText(String.valueOf(Collections.max(ts)));
								progressBar.setString("Finished");

							}
							
						}).start();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnStartExtraction.setBounds(54, 106, 129, 29);
		frmMultiTrack.getContentPane().add(btnStartExtraction);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(6, 74, 252, 20);
		frmMultiTrack.getContentPane().add(progressBar);
		
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(87, 141, 63, 29);
		frmMultiTrack.getContentPane().add(btnExit);
		
		comboBox = new JComboBox<String>();
		comboBox.setEnabled(false);
		comboBox.setBounds(258, 29, 110, 23);
		frmMultiTrack.getContentPane().add(comboBox);
		
		JLabel lblMakamName = new JLabel("Makam Name");
		lblMakamName.setBounds(262, 6, 85, 16);
		frmMultiTrack.getContentPane().add(lblMakamName);
		
		txtDegre = new JTextField();
		txtDegre.setBounds(371, 26, 63, 26);
		frmMultiTrack.getContentPane().add(txtDegre);
		txtDegre.setColumns(10);
		
		JLabel lblDegree = new JLabel("Degree");
		lblDegree.setBounds(371, 6, 61, 16);
		frmMultiTrack.getContentPane().add(lblDegree);
		
		lblMinumumValue = new JLabel("Minumum Value");
		lblMinumumValue.setBounds(270, 78, 101, 16);
		frmMultiTrack.getContentPane().add(lblMinumumValue);
		
		lblMaximumValue = new JLabel("Maximum Value");
		lblMaximumValue.setBounds(270, 123, 106, 16);
		frmMultiTrack.getContentPane().add(lblMaximumValue);
		
		lblMin = new JLabel("");
		lblMin.setBounds(388, 78, 61, 16);
		frmMultiTrack.getContentPane().add(lblMin);
		
		lblMax = new JLabel("");
		lblMax.setBounds(384, 123, 101, 16);
		frmMultiTrack.getContentPane().add(lblMax);
		if(SelectCulture.getCulture()!=null){
			setMakamList(SelectCulture.getCulture().getMakamsData());
			comboBox.setEnabled(true);
		}
		
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
	public void setMakamList(TreeMap<String,Makam> makams){
		if(!(makams==null)){
			// Get a set of the entries
			Set<Map.Entry<String,Makam>> set = makams.entrySet();
			// Get an iterator
			Iterator<Entry<String, Makam>> it = set.iterator();
			// Display elements
			comboBox.addItem("Default");
			while(it.hasNext()){
				Map.Entry<String,Makam> me = it.next();
				comboBox.addItem(me.getValue().getName());
			}
		}
	}
	public static Culture setList(String arg){
		try {
			return Culture.deserializeFromJar(SelectCulture.class.getResourceAsStream("/"+arg+".ser"));			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Culture file isn't found!");
			return null;
		}
	}
}
