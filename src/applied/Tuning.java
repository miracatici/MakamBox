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

import graphics.MakamChart;
import graphics.TonicChart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import backEnd.MakamBox;
import datas.Makam;
import datas.TuningSystem;

public class Tuning {
	//private static final ResourceMakamBoxAnalysis.LANG MakamBoxAnalysis.LANG = MakamBoxAnalysis.LANG; //ResourceMakamBoxAnalysis.LANG.getMakamBoxAnalysis.LANG("applied.tuning"); //$NON-NLS-1$

	protected JFrame frmTuning;
	private JTextField txtTrack1;
	private JTextField txtTrack2;
	private JButton btnLoad1;
	private JButton btnLoad2;
	private JButton btnShowHistogram1;
	private JButton btnShowTemplate1;
	private JFileChooser chooser;
	private File dir;
	private MakamBox rec1,rec2;
	private JButton btnShowAll;
	private JLabel lblRecording;
	private JLabel lblRecording_1;
	private JButton btnShowHistogram2;
	private JButton btnShowTemplate2;
	private JButton btnShowBoth;
	private JLabel lblHistogram;
	private JList<String> list;
	private JButton btnCompare;
	private String[] makamList,systemList;
	private JCheckBox rdInterval;
	private JLabel lbltuningSystemComparison;
	private JList<String> list_2;
	private JButton btnCompareSystems;
	private JButton btnLoadTuningSys;
	private TreeMap<String,TuningSystem> tuningSystems = deserializeFromJar(Tuning.class.getResourceAsStream("/TuningSystems.ser"));
	private JComboBox<String> comboBox;
	private JButton btnSetTrack1;
	private JButton btnSetTrack2;
	private JButton btnTonicHist1;
	private JButton btnTonicHist2;
	public static JTextArea pane;
	private TonicChart tc1,tc2 ;
	private JLabel track1loaded;
	private JLabel track2loaded;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdTrack1;
	private JRadioButton rdTrack2;
	public static boolean isComma = false;
	private JScrollPane scrollPane;

	private void select(boolean isFolder,final String... extension){ 
		chooser = new JFileChooser();
		chooser.setDialogTitle("Select an Data File");
		if(isFolder){
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else {			
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);			
		}
		chooser.setCurrentDirectory(dir);
		chooser.setFileFilter(new FileFilter(){

			@Override
			public boolean accept(File arg0) {
				boolean acc = false;
				if(extension.length>=2){
					acc = (arg0.getName().endsWith(extension[0])|arg0.getName().endsWith(extension[1]));
				} else {
					acc = arg0.getName().endsWith(extension[0]);
				}
				return  acc;
			}

			@Override
			public String getDescription() {
				return null;
			}
		});
		int x=chooser.showOpenDialog(null);
		switch (x){
			case JFileChooser.CANCEL_OPTION:
				break;	
		}
		dir = chooser.getCurrentDirectory();
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Tuning window = new Tuning();
					window.frmTuning.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"An error while opening Tuning window");
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tuning() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTuning = new JFrame();
		frmTuning.setType(Type.POPUP);
		frmTuning.setTitle(MakamBoxAnalysis.LANG.getString("Tuning.frmTuning.title")); //$NON-NLS-1$
		frmTuning.getContentPane().setBackground(Color.DARK_GRAY);
		frmTuning.getContentPane().setLayout(null);
		frmTuning.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		txtTrack1 = new JTextField();
		txtTrack1.setEditable(false);
		txtTrack1.setText(MakamBoxAnalysis.LANG.getString("Tuning.txtTrack1.text")); //$NON-NLS-1$
		txtTrack1.setBounds(6, 77, 117, 28);
		txtTrack1.setColumns(10);
		frmTuning.getContentPane().add(txtTrack1);
		
		txtTrack2 = new JTextField();
		txtTrack2.setEditable(false);
		txtTrack2.setText(MakamBoxAnalysis.LANG.getString("Tuning.txtTrack2.text")); //$NON-NLS-1$
		txtTrack2.setBounds(310, 77, 117, 28);
		txtTrack2.setColumns(10);
		frmTuning.getContentPane().add(txtTrack2);
		
		btnLoad1 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnLoad1.text")); //$NON-NLS-1$
		btnLoad1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				track1loaded.setForeground(Color.white);
				track1loaded.setText("Please wait");
				select(false,".wav",".mp3");
				if(!(chooser.getSelectedFile()==null)){
					txtTrack1.setText(chooser.getSelectedFile().getName());
					try {
						rec1 = new MakamBox(chooser.getSelectedFile(),null);
						rec1.createMakam();
						tc1 = new TonicChart();
						tc1.createFrame(rec1);
						track1loaded.setForeground(Color.GREEN);
						track1loaded.setText("Loaded");
					} catch (Exception e1) {
						track1loaded.setForeground(Color.RED);
						track1loaded.setText("Couldn't Loaded");						
					}
				} else {
					track1loaded.setForeground(Color.white);
					track1loaded.setText("Press Load");
				}
			}
		});
		btnLoad1.setBounds(6, 114, 99, 29);
		frmTuning.getContentPane().add(btnLoad1);
		
		btnLoad2 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnLoad2.text")); //$NON-NLS-1$
		btnLoad2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				track2loaded.setForeground(Color.white);
				track2loaded.setText("Please wait");
				select(false,".wav",".mp3");
				if(!(chooser.getSelectedFile()==null)){
					txtTrack2.setText(chooser.getSelectedFile().getName());
					try {
						rec2 = new MakamBox(chooser.getSelectedFile(),null);
						rec2.createMakam();
						tc2 = new TonicChart();
						tc2.createFrame(rec2);
						track2loaded.setForeground(Color.GREEN);
						track2loaded.setText("Loaded");
					} catch (Exception e1) {
						track2loaded.setForeground(Color.RED);
						track2loaded.setText("Couldn't Loaded");
					}
				} else {
					track2loaded.setForeground(Color.white);
					track2loaded.setText("Press Load");
				}
			}
		});
		btnLoad2.setBounds(328, 117, 99, 29);
		frmTuning.getContentPane().add(btnLoad2);
		
		btnShowHistogram1 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnShowHistogram1.text")); //$NON-NLS-1$
		btnShowHistogram1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					rec1.showHistogram();
				} catch (Exception e1) {
					if(rec1==null){
						JOptionPane.showMessageDialog(null,"Please load a recording to the first slot");
					} else {
						JOptionPane.showMessageDialog(null,"Histogram couldn't created");
					}
				}
			}
		});
		btnShowHistogram1.setBounds(6, 143, 99, 29);
		frmTuning.getContentPane().add(btnShowHistogram1);
		
		btnShowTemplate1 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnShowTemplate1.text")); //$NON-NLS-1$
		btnShowTemplate1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					rec1.showHistWithTemp();
				} catch (Exception e1) {
					if(rec1==null){
						JOptionPane.showMessageDialog(null,"Please load a recording to the first slot");
					} else {
						JOptionPane.showMessageDialog(null,"Histogram couldn't created");
					}
				}
			}
		});
		btnShowTemplate1.setBounds(6, 170, 187, 29);
		frmTuning.getContentPane().add(btnShowTemplate1);
		
		btnShowHistogram2 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnShowHistogram2.text")); //$NON-NLS-1$
		btnShowHistogram2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					rec2.showHistogram();
				} catch (Exception e1) {
					if(rec2==null){
						JOptionPane.showMessageDialog(null,"Please load a recording to the second slot");
					} else {
						JOptionPane.showMessageDialog(null,"Histogram couldn't created");
					}
				}
			}
		});
		btnShowHistogram2.setBounds(328, 143, 99, 29);
		frmTuning.getContentPane().add(btnShowHistogram2);
		
		btnShowTemplate2 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnShowTemplate2.text")); //$NON-NLS-1$
		btnShowTemplate2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					rec2.showHistWithTemp();
				} catch (Exception e1) {
					if(rec2==null){
						JOptionPane.showMessageDialog(null,"Please load a recording to the second slot");
					} else {
						JOptionPane.showMessageDialog(null,"Histogram couldn't created");
					}
				}
			}
		});
		btnShowTemplate2.setBounds(240, 170, 187, 29);
		frmTuning.getContentPane().add(btnShowTemplate2);
		
		lblRecording = new JLabel(MakamBoxAnalysis.LANG.getString("Tuning.lblRecording.text")); //$NON-NLS-1$
		lblRecording.setForeground(Color.WHITE);
		lblRecording.setBounds(32, 59, 75, 16);
		frmTuning.getContentPane().add(lblRecording);
		
		lblRecording_1 = new JLabel(MakamBoxAnalysis.LANG.getString("Tuning.lblRecording_1.text")); //$NON-NLS-1$
		lblRecording_1.setForeground(Color.WHITE);
		lblRecording_1.setBounds(334, 59, 75, 16);
		frmTuning.getContentPane().add(lblRecording_1);
		
		btnShowBoth = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnShowBoth.text")); //$NON-NLS-1$
		btnShowBoth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rec1 == null){
					JOptionPane.showMessageDialog(null,"Please load a recording to firs slot");
					return;
				} else if(rec2 == null){
					JOptionPane.showMessageDialog(null,"Please load a recording to second slot");
					return;
				} else if(rec1.getName().equals(rec2.getName())){
					JOptionPane.showMessageDialog(null,"The recordings are same. Please choose different one");
					return;
				} else {
					try {
						rec1.showAllignedHistogram(rec2);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,"Histogram couldn't created");
					}
				}
			}
		});
		btnShowBoth.setBounds(148, 243, 129, 29);
		frmTuning.getContentPane().add(btnShowBoth);
		
		btnShowAll = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnShowAll.text")); //$NON-NLS-1$
		btnShowAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rec1 == null){
					JOptionPane.showMessageDialog(null,"Please load a recording to firs slot");
					return;
				} else if(rec2 == null){
					JOptionPane.showMessageDialog(null,"Please load a recording to second slot");
					return;
				} else if(rec1.getName().equals(rec2.getName())){
					JOptionPane.showMessageDialog(null,"The recordings are same. Please choose different one");
					return;
				} else {
					try {
						rec1.showAllHistogram(rec2);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,"Histogram couldn't created");
					}
				}
			}
		});
		btnShowAll.setBounds(114, 268, 203, 29);
		frmTuning.getContentPane().add(btnShowAll);
		
		lblHistogram = new JLabel(MakamBoxAnalysis.LANG.getString("Tuning.lblHistogram.text")); //$NON-NLS-1$
		lblHistogram.setForeground(Color.WHITE);
		lblHistogram.setBounds(501, 16, 120, 34);
		frmTuning.getContentPane().add(lblHistogram);
		
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setBounds(501, 59, 129, 159);
		setMakamkList(SelectCulture.getCulture().getMakamsData());
		
//		frmTuning.getContentPane().add((list));
		
		btnCompare = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnCompare.text")); //$NON-NLS-1$
		btnCompare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					compareHistograms();	
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"System comparing couldn't start");
				}
			}
		});
		btnCompare.setBounds(511, 268, 117, 29);
		frmTuning.getContentPane().add(btnCompare);
		
		rdInterval = new JCheckBox(MakamBoxAnalysis.LANG.getString("Tuning.rdInterval.text")); //$NON-NLS-1$
		rdInterval.setForeground(Color.WHITE);
		rdInterval.setBounds(501, 218, 129, 23);
		frmTuning.getContentPane().add(rdInterval);
		
		lbltuningSystemComparison = new JLabel(MakamBoxAnalysis.LANG.getString("Tuning.lbltuningSystemComparison.text")); //$NON-NLS-1$
		lbltuningSystemComparison.setForeground(Color.WHITE);
		lbltuningSystemComparison.setBounds(685, 16, 120, 34);
		frmTuning.getContentPane().add(lbltuningSystemComparison);
		
		list_2 = new JList<String>();
		list_2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list_2.setBounds(685, 59, 129, 159);
		frmTuning.getContentPane().add(list_2);
		
		btnCompareSystems = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnCompareSystems.text")); //$NON-NLS-1$
		btnCompareSystems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(rdTrack1.isSelected()){
						compareSystems(rec1,true);
					} else {
						compareSystems(rec2,true);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"System comparing couldn't start");
				}
			}
		});
		btnCompareSystems.setBounds(695, 268, 117, 29);
		frmTuning.getContentPane().add(btnCompareSystems);
		setSystemList(tuningSystems);
		btnLoadTuningSys = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnLoadTuningSys.text")); //$NON-NLS-1$
		btnLoadTuningSys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				select(false,".ser");
				if(chooser.getSelectedFile()!=null){
					tuningSystems = deserialize(chooser.getSelectedFile().getAbsolutePath());
					setSystemList(tuningSystems);
				}
			}
		});
		btnLoadTuningSys.setBounds(685, 220, 134, 29);
		frmTuning.getContentPane().add(btnLoadTuningSys);
		
		JLabel lbluserDefinedMakam = new JLabel(MakamBoxAnalysis.LANG.getString("Tuning.lbluserDefinedMakam.text")); //$NON-NLS-1$
		lbluserDefinedMakam.setForeground(Color.WHITE);
		lbluserDefinedMakam.setBounds(164, 16, 97, 31);
		frmTuning.getContentPane().add(lbluserDefinedMakam);
		
		comboBox = new JComboBox<String>();
		comboBox.setSize(new Dimension(95, 25));
		comboBox.setEnabled(false);
		comboBox.setBounds(157, 48, 110, 23);
		comboBox.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboBox.getSelectedIndex()==0){
					btnSetTrack1.setEnabled(false);
					btnSetTrack2.setEnabled(false);
				} else {
					btnSetTrack1.setEnabled(true);
					btnSetTrack2.setEnabled(true);
				}
			}
			
		});
		frmTuning.getContentPane().add(comboBox);
		
		btnSetTrack1 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnSetTrack1.text")); //$NON-NLS-1$
		btnSetTrack1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tempName = (String) comboBox.getSelectedItem();
				try {
					rec1.createMakam(tempName);
					JOptionPane.showMessageDialog(null,"New makam set");
				} catch (Exception e){
					JOptionPane.showMessageDialog(null,"New makam couldn't set");
				}
			}
		});
		btnSetTrack1.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnSetTrack1.setEnabled(false);
		btnSetTrack1.setBounds(6, 24, 117, 25);
		frmTuning.getContentPane().add(btnSetTrack1);
		
		btnSetTrack2 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnSetTrack2.text")); //$NON-NLS-1$
		btnSetTrack2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tempName = (String) comboBox.getSelectedItem();
				try {
					rec2.createMakam(tempName);
					JOptionPane.showMessageDialog(null,"New makam set");
				} catch (Exception e){
					JOptionPane.showMessageDialog(null,"New makam couldn't set");
				}
			}
		});
		btnSetTrack2.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnSetTrack2.setEnabled(false);
		btnSetTrack2.setBounds(310, 22, 117, 25);
		frmTuning.getContentPane().add(btnSetTrack2);
		
		btnTonicHist1 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnTonicHist1.text")); //$NON-NLS-1$
		btnTonicHist1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					tc1.show();
				} catch (Exception e1) {
					if(rec1==null){
						JOptionPane.showMessageDialog(null,"Please load a recording to the first slot");
					} else {
						JOptionPane.showMessageDialog(null, "Tonic Chart couldn't created");
					}
				}
			}
		});
		btnTonicHist1.setBounds(6, 203, 187, 29);
		frmTuning.getContentPane().add(btnTonicHist1);
		
		btnTonicHist2 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnTonicHist2.text")); //$NON-NLS-1$
		btnTonicHist2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					tc2.show();
				} catch (Exception e1) {
					if(rec2==null){
						JOptionPane.showMessageDialog(null,"Please load a recording to the second slot");
					} else {
						JOptionPane.showMessageDialog(null, "Tonic Chart couldn't created");
					}				
				}	
			}
		});
		btnTonicHist2.setBounds(240, 203, 187, 29);
		frmTuning.getContentPane().add(btnTonicHist2);
		
		JButton btnReset1 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnReset1.text")); //$NON-NLS-1$
		btnReset1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					rec1.createMakam();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Track 1 couldn't reset");
				}
			}
		});
		btnReset1.setBounds(6, 232, 75, 29);
		frmTuning.getContentPane().add(btnReset1);
		
		JButton btnReset2 = new JButton(MakamBoxAnalysis.LANG.getString("Tuning.btnReset2.text")); //$NON-NLS-1$
		btnReset2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rec2.createMakam();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,"Track 2 couldn't reset");
				}
			}
		});
		btnReset2.setBounds(352, 232, 75, 29);
		frmTuning.getContentPane().add(btnReset2);
		
		track1loaded = new JLabel(MakamBoxAnalysis.LANG.getString("Tuning.track1loaded.text")); //$NON-NLS-1$
		track1loaded.setBounds(117, 119, 82, 16);
		frmTuning.getContentPane().add(track1loaded);
		
		track2loaded = new JLabel(MakamBoxAnalysis.LANG.getString("Tuning.track2loaded.text")); //$NON-NLS-1$
		track2loaded.setBounds(240, 119, 76, 16);
		frmTuning.getContentPane().add(track2loaded);
		
		rdTrack1 = new JRadioButton(MakamBoxAnalysis.LANG.getString("Tuning.rdTrack1.text")); //$NON-NLS-1$
		rdTrack1.setSelected(true);
		buttonGroup.add(rdTrack1);
		rdTrack1.setForeground(Color.WHITE);
		rdTrack1.setBounds(564, 243, 79, 23);
		frmTuning.getContentPane().add(rdTrack1);
		
		rdTrack2 = new JRadioButton(MakamBoxAnalysis.LANG.getString("Tuning.rdTrack2.text")); //$NON-NLS-1$
		buttonGroup.add(rdTrack2);
		rdTrack2.setForeground(Color.WHITE);
		rdTrack2.setBounds(655, 243, 79, 23);
		frmTuning.getContentPane().add(rdTrack2);
		
		scrollPane = new JScrollPane(list);
		scrollPane.setBounds(501, 48, 139, 171);
		frmTuning.getContentPane().add(scrollPane);
		if(SelectCulture.getCulture()!=null){
			setMakamList(SelectCulture.getCulture().getMakamsData());
			comboBox.setEnabled(true);
		}
		frmTuning.setBounds(100, 100, 861, 325);
		frmTuning.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	@SuppressWarnings("unchecked")
	private TreeMap<String,TuningSystem> deserialize(String path){
		FileInputStream fileIn;
		ObjectInputStream in;
		TreeMap<String,TuningSystem> newSystems = null;
		try{
			fileIn = new FileInputStream(path);
	        in = new ObjectInputStream(fileIn);
	        newSystems = (TreeMap<String,TuningSystem>) in.readObject();
	        in.close();
	        fileIn.close();
			JOptionPane.showMessageDialog(null, "Tuning Systems file is read");
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null,"Please select a file");
		}
		return newSystems;
	}
	@SuppressWarnings("unchecked")
	private TreeMap<String,TuningSystem> deserializeFromJar(InputStream st){
		ObjectInputStream in;
		TreeMap<String,TuningSystem> newSystems = null;
		try{
	        in = new ObjectInputStream(st);
	        newSystems = (TreeMap<String,TuningSystem>) in.readObject();
	        in.close();
	        st.close();
			JOptionPane.showMessageDialog(null, "Tuning Systems file is read");
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null,"Please select a file");
		}
		return newSystems;
	}
	private void setSystemList(TreeMap<String,TuningSystem> systems){
		if(!(systems==null)){
			// Get a set of the entries
			Set<Map.Entry<String,TuningSystem>> set = systems.entrySet();
			// Get an iterator
			Iterator<Entry<String, TuningSystem>> it = set.iterator();
			// Display elements
			int i=0;
			systemList = new String[set.size()];
			while(it.hasNext()){
				Map.Entry<String,TuningSystem> me = it.next();
				systemList[i]=me.getKey();
				i++;
			}
			list_2.setListData(systemList);
			list_2.repaint();
		}
	}
	private void setMakamkList(TreeMap<String,Makam> makams){
		if(!(makams==null)){
			// Get a set of the entries
			Set<Map.Entry<String,Makam>> set = makams.entrySet();
			// Get an iterator
			Iterator<Entry<String, Makam>> it = set.iterator();
			// Display elements
			int i=0;
			makamList = new String[set.size()];
			while(it.hasNext()){
				Map.Entry<String,Makam> me = it.next();
				makamList[i]=me.getValue().getName();
				i++;
			}
			list.setListData(makamList);
		}
	}
	private void compareSystems(MakamBox song,boolean euroTheory) throws Exception{
				
        try {
        	MakamChart mc = new MakamChart(song,euroTheory,song.getMakam().getShiftHistogram());
        	mc.setProp(list_2, systemList, tuningSystems, song);
        	mc.addAnnotation(song.getMakam().getPeaksCent());
			mc.setNames(song.getName());
			mc.createFrame();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Comparison chart couldn't created");
		}
	}
	
	private void compareHistograms(){
		int[] selected = list.getSelectedIndices();
		String[] tempName = new String[selected.length];
		float[][] temp = new float[selected.length][];
		for (int i = 0; i < selected.length; i++) {
			String name = makamList[selected[i]];
			tempName[i] = name;
			temp[i] = SelectCulture.getCulture().getMakamsData().get(name).getHistogramData();
		}
		MakamChart mc = null;
		try {
			mc = new MakamChart(rdInterval.isSelected(),temp);
			mc.setNames(tempName);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"An error while comparing histograms");
		}
		mc.createFrame();
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
}
