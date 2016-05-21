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

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import backEnd.PitchTracking;
import graphics.DialChart;

public class Tuner extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3967699817035377903L;
	private JButton btnStartStop;
	private AudioFormat format = new AudioFormat((float)44100,16,1,true,false);
	private PitchTracking track = new PitchTracking(format);
	private static DialChart chart = new DialChart();
	public static JLabel lblNote;
	/**
	 * Create the frame.
	 */
	public Tuner() {
		setResizable(false);
		setTitle("Tuner");
		getContentPane().setBackground(Color.DARK_GRAY);

		btnStartStop = new JButton("Start Tuner");
		btnStartStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(btnStartStop.getText().equals("Start Tuner")){
					track.captureAudio();
					btnStartStop.setText("Stop Tuner");				
				} else {
					try {
						track.running = false;
						track.stopCapture();
						btnStartStop.setText("Start Tuner");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		getContentPane().setLayout(null);
		btnStartStop.setBounds(6, 31, 117, 29);
		getContentPane().add(btnStartStop);
		
				
		JPanel panel = new JPanel();
		panel.setBounds(135, 6, 333, 315);
		panel.setLayout(null);
		chart.setBounds(31, 5, 270, 270);
		panel.add(chart);
		chart.setLayout(new CardLayout(0, 0));
		getContentPane().add(panel);
		
		lblNote = new JLabel("Note");
		lblNote.setBounds(143, 277, 68, 32);
		panel.add(lblNote);
		lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		setBounds(100, 100, 482, 349);
	}
	public static void change(float f){
		chart.change(f);
	}
}
