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
 
 * MakamBox is an implementation of MakamToolBox Turkish Makam music analysis tool which is developed by Baris Bozkurt
 * This is the project of music tuition system that is a tool that helps users to improve their skills to perform music. 
 * This thesis aims at developing a computer based interactive tuition system specifically for Turkish music. 
 * 
 * Designed, developed and implemented by @author Bilge Mirac Atici
 * Supervised by @supervisor Baris Bozkurt
 * Bahcesehir University, 2014-2015
 * 
 * http://www.miracatici.com/makambox
 */
package applied;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import javax.sound.sampled.AudioFormat;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import backEnd.AudioInput;
import backEnd.MakamBox;
import backEnd.SineSynth;
import datas.Ahenk;
import datas.Makam;
import graphics.About;
import utilities.Plot;

public class MakamBoxAnalysis {
	public static ResourceBundle LANG = ResourceBundle.getBundle("applied.language_en"); //$NON-NLS-1$

	public static JSlider volumeSlide, positionSlide;
	public static JProgressBar progressBar;
	protected JFrame frmMakambox;
	private JComboBox<String> ahenkList;
	private JFileChooser chooser;
	private JMenuItem mntmSelectFile,mntmTuning,btnShowHistogram,
	btnShowSongWith,btnShowIntervals,btnShowRecordWithtemplate,
	btnSongRecordTemp,btnShowTuningChart,btnShowHistogramWith,
	btnShowRecordHistogram,btnShowSetting, btnShowRecordPT,btnShowSongPT, mntmSelectData;
	private JMenuBar menuBar;
	private JMenu mnShowHistogram,mnCulturespecificSettings;
	private JPanel pitchComp,waveComp;
	private JLabel lblVolume,lblRecordStatus,lblStatusLabel,
		lblSaveStatus,lblShiftLabel,tonicNote,
		makamPane,tonicHz,shiftAmount,lblMakamName,
		lblTonicNote,lblTonicFrequency,lblNewLabel;
	private JButton btnPlay,btnStop,btnStartRecording,
		btnStopRecording,btnDetectMakam,btnPlayRecord,btnStopRecord,
		btnPlaySynth,btnSineSynth,btnSaveRecord,btnStopSynth,
		btnPause,btnPitchShift,btnShiftPlay,btnShiftPause,
		btnShiftStop,btnShiftSave,btnShiftCancel;
	private File dir=new File("/Users/mirac/");
	private Font btnFont = new Font("Lucida Grande", Font.PLAIN, 11);
	private MakamBox box,recBox ,synthBox,shiftedBox;
	private AudioFormat format;
	private AudioInput recorder;
	private SineSynth synth;
	private float commaValue;
	private JMenuItem mntmTuner;
	private JLabel shiftAmountCent;
	private JComboBox<String> makamList;
	private JButton btnSet;
	private JButton btnAnalyze;
	private JMenu mnSelect;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					new SelectCulture();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Create the application.
	 */
	public MakamBoxAnalysis() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		format = new AudioFormat(44100f,16,1,true,false);
		
		frmMakambox = new JFrame();
		frmMakambox.setMinimumSize(new Dimension(1050, 610));
		frmMakambox.setTitle(LANG.getString("MakamBoxAnalysis.frmMakambox.title")); //$NON-NLS-1$
		frmMakambox.setResizable(false);
		frmMakambox.setLocation(new Point(150, 150));
		frmMakambox.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMakambox.getContentPane().setLayout(null);
		frmMakambox.getContentPane().setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		frmMakambox.getContentPane().setBackground(Color.DARK_GRAY);
		frmMakambox.setVisible(true);
		
		waveComp = new JPanel();
		waveComp.setLayout(new SpringLayout());
		waveComp.setForeground(Color.BLACK);
		waveComp.setLocation(6, 21);
		waveComp.setBackground(Color.LIGHT_GRAY);
		waveComp.setSize(new Dimension(713, 230));
		waveComp.setMinimumSize(new Dimension(713, 230));
		waveComp.setPreferredSize(new Dimension(713, 230));
		frmMakambox.getContentPane().add(waveComp);
		
		pitchComp = new JPanel();
		pitchComp.setLayout(new BorderLayout(0, 0));
		pitchComp.setLocation(6, 322);
		pitchComp.setMinimumSize(new Dimension(713, 230));
		pitchComp.setBackground(Color.BLACK);
		pitchComp.setSize(new Dimension(713, 230));
		pitchComp.setPreferredSize(new Dimension(713, 230));
		frmMakambox.getContentPane().add(pitchComp);
		
		btnPlay = new JButton(LANG.getString("MakamBoxAnalysis.btnPlay.text")); //$NON-NLS-1$
		btnPlay.setFont(btnFont);
		btnPlay.setEnabled(false);
		btnPlay.setBounds(734, 32, 71, 25);
		btnPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnPlay.setEnabled(false);
				btnPause.setEnabled(true);
				btnStop.setEnabled(true);
				try {
					box.getPlayer().play();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please select a song"); // LANG.getString("MakamBoxAnalysis.frmMakambox.title")
				}
			}
		});
		frmMakambox.getContentPane().add(btnPlay);
		
		btnPause = new JButton(LANG.getString("MakamBoxAnalysis.btnPause.text")); //$NON-NLS-1$
		btnPause.setFont(btnFont);
		btnPause.setEnabled(false);
		btnPause.setBounds(734, 69, 71, 25);
		btnPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnPlay.setEnabled(true);
				btnPause.setEnabled(false);
				btnStop.setEnabled(true);
				try {
					box.getPlayer().pause();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please select a song");
				}
			}
		});
		frmMakambox.getContentPane().add(btnPause);
		
		btnStop = new JButton(LANG.getString("MakamBoxAnalysis.btnStop.text")); //$NON-NLS-1$
		btnStop.setFont(btnFont);
		btnStop.setEnabled(false);
		btnStop.setBounds(734, 106, 71, 25);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnPlay.setEnabled(true);
				btnPause.setEnabled(false);
				btnStop.setEnabled(false);
				try {
					btnPause.doClick();
					box.getPlayer().stop();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please select a song");
				}
			}
		});
		frmMakambox.getContentPane().add(btnStop);
		
		btnDetectMakam = new JButton(LANG.getString("MakamBoxAnalysis.btnDetectMakam.text")); //$NON-NLS-1$
		btnDetectMakam.setFont(btnFont);
		btnDetectMakam.setBounds(808, 67, 107, 25);
		btnDetectMakam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					box.createMakam();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Couldn't detect makam");
				}	
				try {
					makamPane.setText(box.getMakamName());
					tonicHz.setText(String.valueOf(Math.round(box.getTonicHz()))+" Hz");
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null,"Couldn't find the tonic Hz");
				}
				try {
					tonicNote.setText(SelectCulture.getCulture().getMakamsData().get(box.getMakamName()).getTonicNote());
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null,"Couldn't find the tonic note name");
				}
			}
		});
		frmMakambox.getContentPane().add(btnDetectMakam);

		makamPane = new JLabel(LANG.getString("MakamBoxAnalysis.makamPane.text")); //$NON-NLS-1$
		makamPane.setOpaque(true);
		makamPane.setBounds(926, 95, 97, 16);
		makamPane.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		makamPane.setForeground(Color.BLACK);
		makamPane.setBackground(Color.WHITE);
		makamPane.setMaximumSize(new Dimension(65, 16));
		makamPane.setMinimumSize(new Dimension(65, 16));
		makamPane.setPreferredSize(new Dimension(65, 16));
		
		btnStartRecording = new JButton(LANG.getString("MakamBoxAnalysis.btnStartRecording.text")); //$NON-NLS-1$
		btnStartRecording.setFont(btnFont);
		btnStartRecording.setBounds(742, 418, 107, 23);
		btnStartRecording.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnStartRecording.setEnabled(false);
				btnStopRecording.setEnabled(true);
				recorder = new AudioInput(format);
				recorder.captureAudio();
				lblRecordStatus.setForeground(Color.RED);
				lblRecordStatus.setText("!!! Now Recording !!!");
			}
		});
		frmMakambox.getContentPane().add(btnStartRecording);

		btnStopRecording = new JButton(LANG.getString("MakamBoxAnalysis.btnStopRecording.text")); //$NON-NLS-1$
		btnStopRecording.setFont(btnFont);
		btnStopRecording.setEnabled(false);
		btnStopRecording.setBounds(742, 453, 107, 29);
		btnStopRecording.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnStartRecording.setEnabled(true);
					btnStopRecording.setEnabled(false);
					btnPlayRecord.setEnabled(true);
					btnSaveRecord.setEnabled(true);
					btnAnalyze.setEnabled(true);
					recorder.stopCapture();
					lblRecordStatus.setForeground(Color.GREEN);
					lblRecordStatus.setText("Recording Stopped");
					recBox = recorder.getRecordedBox(btnStopRecord);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Error while saving recording");
				}
			}
		});
		frmMakambox.getContentPane().add(btnStopRecording);
	
		btnPlayRecord = new JButton(LANG.getString("MakamBoxAnalysis.btnPlayRecord.text")); //$NON-NLS-1$
		btnPlayRecord.setFont(btnFont);
		btnPlayRecord.setEnabled(false);
		btnPlayRecord.setBounds(852, 418, 107, 25);
		btnPlayRecord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnPlayRecord.setEnabled(false);
					btnStopRecord.setEnabled(true);
					lblRecordStatus.setForeground(Color.GREEN);
					lblRecordStatus.setText("Play Recording");
					recBox.getPlayer().play();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please make a Record");
				}
			}
		});
		frmMakambox.getContentPane().add(btnPlayRecord);
		
		btnStopRecord = new JButton(LANG.getString("MakamBoxAnalysis.btnStopRecord.text")); //$NON-NLS-1$
		btnStopRecord.setFont(btnFont);
		btnStopRecord.setEnabled(false);
		btnStopRecord.setBounds(851, 453, 107, 29);
		btnStopRecord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnPlayRecord.setEnabled(true);
					btnStopRecord.setEnabled(false);
					lblRecordStatus.setForeground(Color.GREEN);
					lblRecordStatus.setText("Stop Playing");
					recBox.getPlayer().stop();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please make a record");
				}
			}
		});
		frmMakambox.getContentPane().add(btnStopRecord);
		
		btnPlaySynth = new JButton(LANG.getString("MakamBoxAnalysis.btnPlaySynth.text")); //$NON-NLS-1$
		btnPlaySynth.setFont(btnFont);
		btnPlaySynth.setEnabled(false);
		btnPlaySynth.setBounds(835, 359, 90, 25);
		btnPlaySynth.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					btnPlaySynth.setEnabled(false);
					btnStopSynth.setEnabled(true);
					synthBox.getPlayer().play();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please select a song");
				}
			}
			
		});
		frmMakambox.getContentPane().add(btnPlaySynth);
		
		btnStopSynth = new JButton(LANG.getString("MakamBoxAnalysis.btnStopSynth.text")); //$NON-NLS-1$
		btnStopSynth.setFont(btnFont);
		btnStopSynth.setEnabled(false);
		btnStopSynth.setBounds(925, 359, 105, 25);
		btnStopSynth.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				btnPlaySynth.setEnabled(true);
				btnStopSynth.setEnabled(false);
				try {
					synthBox.getPlayer().stop();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please select a song");
				}
			}	
		});
		frmMakambox.getContentPane().add(btnStopSynth);
		
		lblRecordStatus = new JLabel(LANG.getString("MakamBoxAnalysis.lblRecordStatus.text")); //$NON-NLS-1$
		lblRecordStatus.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblRecordStatus.setForeground(Color.WHITE);
		lblRecordStatus.setBounds(897, 486, 140, 26);
		
		btnSineSynth = new JButton(LANG.getString("MakamBoxAnalysis.btnSineSynth.text")); //$NON-NLS-1$
		btnSineSynth.setFont(btnFont);
		btnSineSynth.setEnabled(false);
		btnSineSynth.setBounds(742, 359, 94, 25);
		btnSineSynth.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					btnSineSynth.setEnabled(false);
					btnPlaySynth.setEnabled(true);
					synth = new SineSynth(box.getPitchDetection());
					synth.synth();
					synthBox = new MakamBox(synth.getSynthedWave(),btnStopSynth);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please select a song");
				}
			}
		});
		frmMakambox.getContentPane().add(btnSineSynth);
		
		btnSaveRecord = new JButton(LANG.getString("MakamBoxAnalysis.btnSaveRecord.text")); //$NON-NLS-1$
		btnSaveRecord.setFont(btnFont);
		btnSaveRecord.setEnabled(false);
		btnSaveRecord.setBounds(742, 483, 107, 29);
		btnSaveRecord.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					recBox.saveAudio();
					lblSaveStatus.setForeground(Color.GREEN);
					lblSaveStatus.setText("Record saved");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please make a record");
					lblSaveStatus.setForeground(Color.RED);
					lblSaveStatus.setText("Error while Saving");
				}
			}
		});
		frmMakambox.getContentPane().add(btnSaveRecord);
		
		lblSaveStatus = new JLabel(LANG.getString("MakamBoxAnalysis.lblSaveStatus.text")); //$NON-NLS-1$
		lblSaveStatus.setBounds(815, 515, 144, 19);
		
		btnShowHistogram = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowHistogram.text")); //$NON-NLS-1$
		btnShowHistogram.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnShowHistogram.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					box.showHistogram();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Couldn't create histogram");
				}
			}
		});
		
		btnShowHistogramWith = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowHistogramWith.text")); //$NON-NLS-1$
		btnShowHistogramWith.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnShowHistogramWith.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					box.showHistWithTemp();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Couldn't create histogram");
				}
			}
		});
		
		btnShowRecordHistogram = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowRecordHistogram.text")); //$NON-NLS-1$
		btnShowRecordHistogram.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnShowRecordHistogram.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					recBox.showHistogram();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please make a record");
				}
			}
		});
		
		btnShowIntervals = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowIntervals.text")); //$NON-NLS-1$
		btnShowIntervals.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnShowIntervals.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					box.showIntervalChart();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please select a song");
				}
			}
		});
		
		btnShowTuningChart = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowTuningChart.text")); //$NON-NLS-1$
		btnShowTuningChart.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnShowTuningChart.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					box.showTuneChart();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Tuning window couldn't created");
				}
			}			
		});
		
		btnShowSongWith = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowSongWith.text")); //$NON-NLS-1$
		btnShowSongWith.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnShowSongWith.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					box.showHistWithRec(recBox,false);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please make a record");
				}
			}	
		});
		
		btnSongRecordTemp = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnSongRecordTemp.text")); //$NON-NLS-1$
		btnSongRecordTemp.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnSongRecordTemp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					box.showHistWithRec(recBox,true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please make a record");
				}
			}	
		});
		
		menuBar = new JMenuBar();
		mnShowHistogram = new JMenu(LANG.getString("MakamBoxAnalysis.mnShowHistogram.text")); //$NON-NLS-1$
		mnShowHistogram.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		btnShowSongPT = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowSongPT.text")); //$NON-NLS-1$
		btnShowSongPT.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnShowSongPT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					box.getPitchChart().createDialog();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Pitch chart couldn't be created !!!");
				}
			}
		});
		mnShowHistogram.add(btnShowSongPT);
		mnShowHistogram.add(btnShowHistogram);
		mnShowHistogram.add(btnShowHistogramWith);
		mnShowHistogram.add(btnShowIntervals);
		mnShowHistogram.add(btnShowTuningChart);
		
		btnShowRecordPT = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowRecordPT.text")); //$NON-NLS-1$
		btnShowRecordPT.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnShowRecordPT.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Plot.plot(recBox.getPitchTrackData());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please make a record");
				}
			}			
		});
		mnShowHistogram.add(btnShowRecordPT);
		mnShowHistogram.add(btnShowRecordHistogram);
		
		btnShowRecordWithtemplate = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowRecordWithtemplate.text")); //$NON-NLS-1$
		btnShowRecordWithtemplate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					recBox.showHistWithTemp();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please make a record");
				}
				
			}
		});
		btnShowRecordWithtemplate.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		mnShowHistogram.add(btnShowRecordWithtemplate);
		mnShowHistogram.add(btnShowSongWith);
		mnShowHistogram.add(btnSongRecordTemp);
		
		volumeSlide = new JSlider();
		volumeSlide.setValueIsAdjusting(true);
		volumeSlide.setSnapToTicks(true);
		volumeSlide.setMaximumSize(new Dimension(120, 29));
		volumeSlide.setMinimum(-30);
		volumeSlide.setMaximum(0);
		volumeSlide.setValue(0);
		volumeSlide.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				try {
					box.getPlayer().setVolume(volumeSlide.getValue());
				} catch (Exception e) {
				}
			}	
		});
		
		lblVolume = new JLabel(LANG.getString("MakamBoxAnalysis.lblVolume.text")); //$NON-NLS-1$
		lblVolume.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblVolume.setForeground(Color.BLACK);
		lblVolume.setBackground(Color.WHITE);
		
		mnSelect = new JMenu(LANG.getString("MakamBoxAnalysis.mnSelect.text")); //$NON-NLS-1$
		mnSelect.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		menuBar.add(mnSelect);
		
		mntmSelectFile = new JMenuItem(LANG.getString("MakamBoxAnalysis.mntmSelectFile.text")); //$NON-NLS-1$
		mntmSelectFile.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		mntmSelectFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lblStatusLabel.setForeground(Color.RED);
				lblStatusLabel.setText("!!! Please Wait !!!");
				selectFile();
			}
		});
		mnSelect.add(mntmSelectFile);
		
		mntmSelectData= new JMenuItem(LANG.getString("MakamBoxAnalysis.mntmLoadPitchtrackData.text")); //$NON-NLS-1$
		mntmSelectData.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		mnSelect.add(mntmSelectData);
		mntmSelectData.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				lblStatusLabel.setForeground(Color.RED);
				lblStatusLabel.setText("!!! Please Wait !!!");
				JOptionPane.showMessageDialog(null,"This function is not ready");
			}
		});
		menuBar.add(mnShowHistogram);
		
		mntmTuning = new JMenuItem(LANG.getString("MakamBoxAnalysis.mntmTuning.text")); //$NON-NLS-1$
		mntmTuning.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							Tuning window = new Tuning();
							window.frmTuning.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							window.frmTuning.setVisible(true);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null,"Tuning window couldn't created");
						}
					}
				});
			}
		});
		mntmTuning.setMaximumSize(new Dimension(120, 19));
		mntmTuning.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		menuBar.add(mntmTuning);
		
		mntmTuner = new JMenuItem(LANG.getString("MakamBoxAnalysis.mntmTuner.text")); //$NON-NLS-1$
		mntmTuner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							Tuner frame = new Tuner();
							frame.setVisible(true);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null,"Tuner window couldn't created");
						}
					}
				});
			}
		});
		mntmTuner.setMaximumSize(new Dimension(120, 19));
		mntmTuner.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		menuBar.add(mntmTuner);
		menuBar.add(lblVolume);
		menuBar.add(volumeSlide);
		
		mnCulturespecificSettings = new JMenu(LANG.getString("MakamBoxAnalysis.mnCulturespecificSettings.text")); //$NON-NLS-1$
		mnCulturespecificSettings.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		menuBar.add(mnCulturespecificSettings);
		
		btnShowSetting = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnShowSetting.text")); //$NON-NLS-1$
		btnShowSetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "The selected culture file is: "+SelectCulture.getCulture().getName());
			}
		});
		btnShowSetting.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		mnCulturespecificSettings.add(btnShowSetting);
		
		JMenuItem btnChangeSettingFile = new JMenuItem(LANG.getString("MakamBoxAnalysis.btnChangeSettingFile.text")); //$NON-NLS-1$
		btnChangeSettingFile.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		btnChangeSettingFile.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure? ", "Change Settings File", JOptionPane.YES_NO_OPTION);
				if(reply == JOptionPane.YES_OPTION){
					frmMakambox.dispose();
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								new SelectCulture();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
		});
		mnCulturespecificSettings.add(btnChangeSettingFile);
		
		JMenuItem mntmAbout = new JMenuItem(LANG.getString("MakamBoxAnalysis.mntmAbout.text")); //$NON-NLS-1$
		mntmAbout.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		mntmAbout.setMaximumSize(new Dimension(80, 19));
		mntmAbout.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				About ab = new About();
				ab.setVisible(true);
			}
		});
		menuBar.add(mntmAbout);
		frmMakambox.setJMenuBar(menuBar);
		
		JMenuItem menuItem = new JMenuItem(LANG.getString("MakamBoxAnalysis.menuItem.text")); //$NON-NLS-1$
		menuBar.add(menuItem);
		
		JMenuItem mntmExit = new JMenuItem(LANG.getString("MakamBoxAnalysis.mntmExit.text")); //$NON-NLS-1$
		mntmExit.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		mntmExit.setMaximumSize(new Dimension(55, 19));
		mntmExit.setPreferredSize(new Dimension(55, 19));
		mntmExit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menuBar.add(mntmExit);
		frmMakambox.getContentPane().add(lblSaveStatus);
		frmMakambox.getContentPane().add(lblRecordStatus);
		frmMakambox.getContentPane().add(makamPane);
		
		tonicNote = new JLabel(LANG.getString("MakamBoxAnalysis.tonicNote.text")); //$NON-NLS-1$
		tonicNote.setHorizontalTextPosition(SwingConstants.LEFT);
		tonicNote.setOpaque(true);
		tonicNote.setBackground(Color.WHITE);
		tonicNote.setForeground(Color.BLACK);
		tonicNote.setBounds(950, 120, 73, 16);
		frmMakambox.getContentPane().add(tonicNote);
		
		tonicHz = new JLabel(LANG.getString("MakamBoxAnalysis.tonicHz.text")); //$NON-NLS-1$
		tonicHz.setOpaque(true);
		tonicHz.setForeground(Color.BLACK);
		tonicHz.setBackground(Color.WHITE);
		tonicHz.setBounds(950, 145, 73, 16);
		frmMakambox.getContentPane().add(tonicHz);
		
		shiftAmount = new JLabel(LANG.getString("MakamBoxAnalysis.shiftAmount.text")); //$NON-NLS-1$
		shiftAmount.setOpaque(true);
		shiftAmount.setBackground(Color.WHITE);
		shiftAmount.setForeground(Color.BLACK);
		shiftAmount.setBounds(864, 219, 91, 16);
		frmMakambox.getContentPane().add(shiftAmount);
		
		lblShiftLabel = new JLabel(LANG.getString("MakamBoxAnalysis.lblShiftLabel.text")); //$NON-NLS-1$
		lblShiftLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblShiftLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		lblShiftLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblShiftLabel.setForeground(Color.WHITE);
		lblShiftLabel.setBounds(855, 178, 126, 36);
		frmMakambox.getContentPane().add(lblShiftLabel);
		
		btnPitchShift = new JButton(LANG.getString("MakamBoxAnalysis.btnPitchShift.text")); //$NON-NLS-1$
		btnPitchShift.setEnabled(false);
		btnPitchShift.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lblStatusLabel.setForeground(Color.RED);
				lblStatusLabel.setText("!!! Please Wait !!!");
				try {
					box.getPitchShift().shiftPitch();
					btnPitchShift.setEnabled(false);
					btnShiftCancel.setEnabled(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please select an ahenk");
				}
				lblStatusLabel.setForeground(Color.GREEN);
				lblStatusLabel.setText("We're ready :)");
			}
		});
		btnPitchShift.setFont(btnFont);
		btnPitchShift.setBounds(742, 254, 87, 29);
		frmMakambox.getContentPane().add(btnPitchShift);
		
		ahenkList = new JComboBox<String>();
		ahenkList.setEnabled(false);
		ahenkList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 setTranspose();
			}
		});
		ahenkList.setSize(new Dimension(95, 25));
		ahenkList.setBounds(742, 219, 110, 23);
		if(SelectCulture.getCulture()!=null){
			setAhenkList(SelectCulture.getCulture().getAhenksData());
		}		
		frmMakambox.getContentPane().add(ahenkList);
				
		btnShiftPlay = new JButton(LANG.getString("MakamBoxAnalysis.btnShiftPlay.text")); //$NON-NLS-1$
		btnShiftPlay.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnShiftPlay.setEnabled(false);
		btnShiftPlay.setBounds(834, 258, 55, 25);
		btnShiftPlay.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				btnShiftPlay.setEnabled(false);
				btnShiftPause.setEnabled(true);
				btnShiftStop.setEnabled(true);
				try {
					shiftedBox.getPlayer().play();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please select an ahenk");
				}				
			}
			
		});
		frmMakambox.getContentPane().add(btnShiftPlay);
		
		btnShiftPause = new JButton(LANG.getString("MakamBoxAnalysis.btnShiftPause.text")); //$NON-NLS-1$
		btnShiftPause.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnShiftPause.setEnabled(false);
		btnShiftPause.setBounds(885, 258, 71, 25);
		btnShiftPause.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				btnShiftPlay.setEnabled(true);
				btnShiftPause.setEnabled(false);
				btnShiftStop.setEnabled(true);
				try {
					shiftedBox.getPlayer().pause();;
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please select an ahenk");
				}				
			}
			
		});
		frmMakambox.getContentPane().add(btnShiftPause);
		
		btnShiftStop = new JButton(LANG.getString("MakamBoxAnalysis.btnShiftStop.text")); //$NON-NLS-1$
		btnShiftStop.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnShiftStop.setEnabled(false);
		btnShiftStop.setBounds(950, 258, 68, 25);
		btnShiftStop.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				btnShiftPlay.setEnabled(true);
				btnShiftPause.setEnabled(false);
				btnShiftStop.setEnabled(false);
				try {
					btnShiftPause.doClick();
					shiftedBox.getPlayer().stop();;
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,"Please select an ahenk");
				}				
			}
		});
		frmMakambox.getContentPane().add(btnShiftStop);
		
		btnShiftSave = new JButton(LANG.getString("MakamBoxAnalysis.btnShiftSave.text")); //$NON-NLS-1$
		btnShiftSave.setFont(btnFont);
		btnShiftSave.setEnabled(false);
		btnShiftSave.setBounds(742, 311, 88, 29);
		btnShiftSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					shiftedBox.saveAudio();
					btnShiftSave.setEnabled(false);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"Please make a pitch shifting");
				}
			}
		});
		frmMakambox.getContentPane().add(btnShiftSave);
		
		lblMakamName = new JLabel(LANG.getString("MakamBoxAnalysis.lblMakamName.text")); //$NON-NLS-1$
		lblMakamName.setForeground(Color.WHITE);
		lblMakamName.setBounds(816, 95, 90, 16);
		frmMakambox.getContentPane().add(lblMakamName);
		
		lblTonicNote = new JLabel(LANG.getString("MakamBoxAnalysis.lblTonicNote.text")); //$NON-NLS-1$
		lblTonicNote.setForeground(Color.WHITE);
		lblTonicNote.setBounds(816, 120, 81, 16);
		frmMakambox.getContentPane().add(lblTonicNote);
		
		lblTonicFrequency = new JLabel(LANG.getString("MakamBoxAnalysis.lblTonicFrequency.text")); //$NON-NLS-1$
		lblTonicFrequency.setForeground(Color.WHITE);
		lblTonicFrequency.setBounds(816, 146, 110, 16);
		frmMakambox.getContentPane().add(lblTonicFrequency);
		
		lblNewLabel = new JLabel(LANG.getString("MakamBoxAnalysis.lblNewLabel.text")); //$NON-NLS-1$
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setBounds(749, 191, 89, 16);
		frmMakambox.getContentPane().add(lblNewLabel);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(844, 295, 150, 20);
		progressBar.setMinimum(0);
		progressBar.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(progressBar.getValue()==progressBar.getMaximum()){
					try {
						shiftedBox = new MakamBox(box.getShiftedWave(),btnShiftStop);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,"Pitch shifted wave couldn't created");
					}
					btnShiftPlay.setEnabled(true);
					btnShiftSave.setEnabled(true);
					btnShiftCancel.setEnabled(false);
				}
			}
		});
		frmMakambox.getContentPane().add(progressBar);
		
		btnShiftCancel = new JButton(LANG.getString("MakamBoxAnalysis.btnShiftCancel.text")); //$NON-NLS-1$
		btnShiftCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				box.stopShifting();
				btnShiftCancel.setEnabled(false);
			}
		});
		btnShiftCancel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnShiftCancel.setEnabled(false);
		btnShiftCancel.setBounds(742, 283, 80, 29);
		frmMakambox.getContentPane().add(btnShiftCancel);
		
		shiftAmountCent = new JLabel(LANG.getString("MakamBoxAnalysis.shiftAmountCent.text")); //$NON-NLS-1$
		shiftAmountCent.setOpaque(true);
		shiftAmountCent.setForeground(Color.BLACK);
		shiftAmountCent.setBackground(Color.WHITE);
		shiftAmountCent.setBounds(968, 219, 68, 16);
		frmMakambox.getContentPane().add(shiftAmountCent);
		
		JLabel lblCent = new JLabel(LANG.getString("MakamBoxAnalysis.lblCent.text")); //$NON-NLS-1$
		lblCent.setHorizontalTextPosition(SwingConstants.CENTER);
		lblCent.setHorizontalAlignment(SwingConstants.CENTER);
		lblCent.setForeground(Color.WHITE);
		lblCent.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		lblCent.setBounds(984, 181, 46, 36);
		frmMakambox.getContentPane().add(lblCent);
		
		makamList = new JComboBox<String>();
		makamList.setSize(new Dimension(95, 25));
		makamList.setEnabled(false);
		makamList.setBounds(927, 20, 110, 23);
		if(SelectCulture.getCulture()!=null){
			setMakamList(SelectCulture.getCulture().getMakamsData());
			makamList.setEnabled(true);
		}
		makamList.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(makamList.getSelectedIndex()==0){
					btnSet.setEnabled(false);
				} else {
					btnSet.setEnabled(true);
				}
			}
		});
		frmMakambox.getContentPane().add(makamList);
		
		JLabel makamUser = new JLabel(LANG.getString("MakamBoxAnalysis.makamUser.text")); //$NON-NLS-1$
		makamUser.setForeground(Color.WHITE);
		makamUser.setBounds(815, 26, 97, 31);
		frmMakambox.getContentPane().add(makamUser);
		
		btnSet = new JButton(LANG.getString("MakamBoxAnalysis.btnSet.text")); //$NON-NLS-1$
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(makamList.getSelectedIndex()!=0){
					try {
						String tempName = (String) makamList.getSelectedItem();
						box.createMakam(tempName);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,"Couldn't detect makam");
					}	
					try {
						makamPane.setText(box.getMakamName());
						tonicHz.setText(String.valueOf(Math.round(box.getTonicHz()))+" Hz");
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null,"Couldn't find the tonic Hz");
					}
					try {
						tonicNote.setText(SelectCulture.getCulture().getMakamsData().get(box.getMakamName()).getTonicNote());
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null,"Couldn't find the tonic note name");
					}				}
			}
		});
		btnSet.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnSet.setBounds(956, 58, 61, 25);
		btnSet.setEnabled(false);
		frmMakambox.getContentPane().add(btnSet);
		
		btnAnalyze = new JButton(LANG.getString("MakamBoxAnalysis.btnAnalyze.text")); //$NON-NLS-1$
		btnAnalyze.setEnabled(false);
		btnAnalyze.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					recBox.createMakam();
					lblRecordStatus.setText("Record Analyzed");
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null,"Recording couldn't analyzed");
				}
			}
		});
		btnAnalyze.setBounds(959, 418, 85, 25);
		frmMakambox.getContentPane().add(btnAnalyze);
		
		JLabel label = new JLabel("Position");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		label.setBounds(6, 263, 57, 29);
		frmMakambox.getContentPane().add(label);
		
		positionSlide = new JSlider();
		positionSlide.setValue(0);
		positionSlide.setBounds(68, 292, 631, 16);
		frmMakambox.getContentPane().add(positionSlide);
		
		lblStatusLabel = new JLabel("Status");
		lblStatusLabel.setForeground(Color.WHITE);
		lblStatusLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		lblStatusLabel.setBackground(Color.LIGHT_GRAY);
		lblStatusLabel.setBounds(602, 264, 107, 26);
		frmMakambox.getContentPane().add(lblStatusLabel);
	}
	public void setMakamList(TreeMap<String,Makam> makams){
		if(!(makams==null)){
			// Get a set of the entries
			Set<Map.Entry<String,Makam>> set = makams.entrySet();
			// Get an iterator
			Iterator<Entry<String, Makam>> it = set.iterator();
			// Display elements
			makamList.addItem("Default");
			while(it.hasNext()){
				Map.Entry<String,Makam> me = it.next();
				makamList.addItem(me.getValue().getName());
			}
		}
	}
	public void setAhenkList(TreeMap<String,Ahenk> ahenks){
//		if(ahenkList.getItemCount()!=0){
//			ahenkList = new JComboBox<String>();
//			ahenkList.removeAllItems();
//		}
		if(!(ahenks==null)){
			// Get a set of the entries
			Set<Map.Entry<String,Ahenk>> set = ahenks.entrySet();
			// Get an iterator
			Iterator<Entry<String, Ahenk>> it = set.iterator();
			// Display elements
			ahenkList.addItem("Default");
			while(it.hasNext()){
				Map.Entry<String,Ahenk> me = it.next();
				ahenkList.addItem(me.getValue().name);
			}
		}
	}
	public void setTranspose(){
		int ahenkInd = ahenkList.getSelectedIndex();
		if (ahenkInd==0) {
			shiftAmount.setText("0");
			commaValue = 0;
			btnPitchShift.setEnabled(false);
		} else {
			try {
				String tonicNote  = "";
				try {
					tonicNote = SelectCulture.getCulture().getMakamsData().get(box.getMakamName()).getTonicNote();
				}  catch (NullPointerException e){
					JOptionPane.showMessageDialog(null,"Please detect makam");
				}
				System.out.println(tonicNote);
				float tonicFreq = box.getTonicHz();
				System.out.println(tonicFreq);
				float tonicRatio = SelectCulture.getCulture().getNotesData().get(tonicNote).ratio;
				System.out.println(tonicRatio);
				
				String ahenkBaseName = SelectCulture.getCulture().getAhenksData().get(ahenkList.getSelectedItem()).baseNote;
				System.out.println(ahenkBaseName);
				float ahenkBase = SelectCulture.getCulture().getAhenksData().get(ahenkList.getSelectedItem()).baseFreq;
				System.out.println(ahenkBase);
				float ahenkBaseRatio = SelectCulture.getCulture().getNotesData().get(ahenkBaseName).ratio;
				
				float relatedRatio = (ahenkBaseRatio) / (tonicRatio) ;
				System.out.println(relatedRatio);
				float boxBase = box.getTonicHz()*relatedRatio;
				System.out.println(boxBase);
				
				commaValue = box.getPitchShift().holderCommaAmount(boxBase, ahenkBase);
				
				System.out.println(commaValue);
				box.getPitchShift().setPitchShift(commaValue);
				shiftAmount.setText(String.valueOf(commaValue));
				shiftAmountCent.setText(String.valueOf(commaValue*(1200.0/53.0)));
				btnPitchShift.setEnabled(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void selectFile(){
		int x=0;
		chooser = new JFileChooser();
		chooser.setDialogTitle("Select an Audio File");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setCurrentDirectory(dir);
		
		x=chooser.showOpenDialog(null);
		if(x==JFileChooser.APPROVE_OPTION){
			while(!(chooser.getSelectedFile().getName().endsWith(".wav")||
					chooser.getSelectedFile().getName().endsWith(".mp3"))){
				JOptionPane.showMessageDialog(null,"Please select a supported file"+
												" Uncompressed WAVE files or MP3 files are supported");
				x=chooser.showOpenDialog(null);
				if(x==JFileChooser.CANCEL_OPTION){
					JOptionPane.showMessageDialog(null,"File not choosen");
					break;
				}
			}
			try{
				if(box!=null){
					box.killObject();
					box=null;
					System.gc();
				}
				if(chooser.getSelectedFile().getName().endsWith(".wav")){
					box = new MakamBox(chooser.getSelectedFile(),btnStop);	
				} else if(chooser.getSelectedFile().getName().endsWith(".mp3")) {
					int t = JOptionPane.showConfirmDialog(null,"File will converted to WAV. Process will take a minute","File type is MP3", JOptionPane.YES_NO_OPTION);
					if(t==JOptionPane.YES_OPTION){
						box = new MakamBox(chooser.getSelectedFile(),btnStop);
					}
				}
				positionSlide.setMaximum((int)box.getWavefile().getFrameLength());
				dir = chooser.getCurrentDirectory();
				btnPlay.setEnabled(true);
				btnSineSynth.setEnabled(true);
				btnPlaySynth.setEnabled(false);
				btnStopSynth.setEnabled(false);
				ahenkList.setEnabled(true);
				waveComp.removeAll();
				pitchComp.removeAll();
				waveComp.add(box.showWaveChart(),BorderLayout.CENTER);
				pitchComp.add(box.showPitchChart(),BorderLayout.NORTH);
				frmMakambox.setTitle(box.getName());
				System.gc();
			}
			catch(Exception ex){
				System.out.println("hata 1");
				JOptionPane.showMessageDialog(null,"Please select a file");
			}
			makamPane.setText("");
			tonicHz.setText("");
			tonicNote.setText("");
			waveComp.repaint();
			pitchComp.repaint();
		}
		else{
			JOptionPane.showMessageDialog(null,"File not choosen");
		}	
		lblStatusLabel.setForeground(Color.GREEN);
		lblStatusLabel.setText("We're ready :)");
	}
}

