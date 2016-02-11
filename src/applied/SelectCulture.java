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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

import datas.Culture;

public class SelectCulture {

	private static Culture culture;
	private static JFileChooser chooser;
	public static void select(String extension,boolean isFolder){ 
		chooser = new JFileChooser();
		chooser.setDialogTitle("Select an Data File");
		if(isFolder){
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		} else {			
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);			
		}
		chooser.setCurrentDirectory(new File("/Users/mirac/Desktop/app/Culture Settings"));
		
		int x=chooser.showOpenDialog(null);
		if(x==JFileChooser.APPROVE_OPTION){
			while(!(chooser.getSelectedFile().getName().endsWith(extension))){
				JOptionPane.showMessageDialog(null,"Please select a supported file"+
													" *.ser file that created with Data Tool");
				x=chooser.showOpenDialog(null);
				if(x==JFileChooser.CANCEL_OPTION){
					JOptionPane.showMessageDialog(null,"File not choosen");
					return;
				}
			}
			culture = Culture.deserialize(chooser.getSelectedFile().getAbsolutePath());
			System.out.println("culture deserialed");
		} else {
			JOptionPane.showMessageDialog(null,"File not choosen");
		}
	}
	public static void setCulture(Culture cul){
		culture = cul;
	}
	public static Culture getCulture(){
		if(culture==null){
			select(".ser",false);
		}
		return culture;
	}
	/**
	 * Create the application.
	 */
	public SelectCulture() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		final JFrame frmSelection;
		JTextPane lblSelectText;
		JButton btnSelectFile,btnSet;
		
		frmSelection = new JFrame();
		frmSelection.setTitle("Select Culture Settings File");
		frmSelection.setPreferredSize(new Dimension(370, 135));
		frmSelection.setResizable(false);
		frmSelection.setBounds(100, 100, 370, 177);
		frmSelection.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmSelection.getContentPane().setLayout(null);
		frmSelection.setVisible(true);

		btnSet = new JButton("Set & Start");
		btnSet.setBounds(46, 55, 91, 29);
		frmSelection.getContentPane().add(btnSet);
		
		btnSelectFile = new JButton("Select File");
		btnSelectFile.setBounds(235, 63, 117, 29);
		frmSelection.getContentPane().add(btnSelectFile);
		
		lblSelectText = new JTextPane();
		lblSelectText.setBackground(new Color(238,238,238));
		lblSelectText.setText("Please select a Culture Data File (*.ser) to prepare\nMakamBox Culture-Specific Music Analysis Software");
		lblSelectText.setBounds(19, 16, 334, 32);
		frmSelection.getContentPane().add(lblSelectText);
		
		JLabel lblDefault = new JLabel("OR");
		lblDefault.setBounds(188, 70, 26, 16);
		frmSelection.getContentPane().add(lblDefault);

		JLabel lblLanguage = new JLabel("Language");
		lblLanguage.setBounds(19, 123, 61, 16);
		frmSelection.getContentPane().add(lblLanguage);
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(15, 82, 161, 27);
		comboBox.addItem("TurkishCulture");
		comboBox.addItem("TurkishExtended");
		comboBox.addItem("ArabCulture");
		comboBox.setSelectedItem("TurkishCulture");
		frmSelection.getContentPane().add(comboBox);
		
		final JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.setBounds(92, 121, 124, 27);
		comboBox_1.addItem("English");
		comboBox_1.addItem("Türkçe");
		frmSelection.getContentPane().add(comboBox_1);
		frmSelection.repaint();
		
		
		
		btnSelectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				select(".ser",false);
				if(culture!=null){
					frmSelection.dispose();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								setLanguage((String) comboBox_1.getSelectedItem());
								MakamBoxAnalysis window = new MakamBoxAnalysis();
								window.frmMakambox.setVisible(true);
								window.frmMakambox.repaint();
								window.frmMakambox.pack();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();	
				} 
			}
		});
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setList((String) comboBox.getSelectedItem());
				if(culture!=null){
					frmSelection.dispose();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								setLanguage((String) comboBox_1.getSelectedItem());
								MakamBoxAnalysis window = new MakamBoxAnalysis();
								window.frmMakambox.setVisible(true);
								window.frmMakambox.repaint();
								window.frmMakambox.pack();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();	
				} else {
					System.exit(0);
				}
			}
		});
	}
	public void setList(String arg){
		try {
			culture = Culture.deserializeFromJar(SelectCulture.class.getResourceAsStream("/"+arg+".ser"));			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Culture file isn't found!");
		}
	}
	public void setLanguage(String lang){
		if(lang.equals("English")){
			MakamBoxAnalysis.LANG = ResourceBundle.getBundle("applied.language_en");
		} else {
			MakamBoxAnalysis.LANG = ResourceBundle.getBundle("applied.language_tr");
		}
	}
}
