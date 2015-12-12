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
package graphics;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.XYItemEntity;

import backEnd.MakamBox;
import utilities.AudioUtilities;

public class TonicChart{
	/**
	 * 
	 */
	private JFrame frame;
	private HistogramChart histChart;
	private ChartPanel panel;
	private JFreeChart chart;
	private String frameName;
	private JTextField text;
	private JButton btnSet;

	public TonicChart() {
		
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createFrame(final MakamBox obj) throws Exception {
		frame = new JFrame(frameName);
		frame.setPreferredSize(new Dimension(715, 356));
		frame.setBounds(new Rectangle(100, 100, 715, 356));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		text = new JTextField();
		text.setBounds(322, 292, 85, 28);
		
		JLabel lblEnterShiftAmount = new JLabel("Enter shift amount in cent");
		lblEnterShiftAmount.setBounds(140, 286, 170, 16);
		
		btnSet = new JButton("Set");
		btnSet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					obj.getMakam().setShiftTonic(Float.valueOf(text.getText()));
					JOptionPane.showMessageDialog(null,"New tonic set");
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,"Invalid number type, please enter a valid cent amount");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,"New tonic couldn't set");
				}
			}
		});
		btnSet.setBounds(419, 293, 79, 28);
		
		histChart = obj.getHistogramChart();
		histChart.createOnlyChart(false);
		chart = histChart.getChart();
		frameName = "Tonic Assignment from Histogram Peaks";
		createPanel(obj);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(panel);
		frame.getContentPane().add(lblEnterShiftAmount);
		frame.getContentPane().add(text);
		frame.getContentPane().add(btnSet);
		
		JLabel lblOrClickHistogram = new JLabel("or click histogram peaks");
		lblOrClickHistogram.setBounds(140, 308, 170, 16);
		frame.getContentPane().add(lblOrClickHistogram);
	}
	private void createPanel(final MakamBox box){
		panel = new ChartPanel(chart,true);
		panel.setBounds(0, 0, 713, 280);
	    panel.addChartMouseListener(new ChartMouseListener(){
	    	@Override
			public void chartMouseClicked(ChartMouseEvent event) {
	    		if(event.getEntity() instanceof XYItemEntity){
					double chartX = chart.getXYPlot().getDomainAxis().java2DToValue(
							event.getTrigger().getX(), 
							panel.getScreenDataArea(), 
							chart.getXYPlot().getDomainAxisEdge());	
					try {
						float a = AudioUtilities.hertzToCent((float)chartX) - box.getTonicCent();
						text.setText(String.valueOf(Math.round(a)));
					} catch (Exception e) {
						e.printStackTrace();
					}
	    		}
	    	}
	    	@Override
			public void chartMouseMoved(ChartMouseEvent event) {}
	    });
	    panel.setPreferredSize(new Dimension(713,280));
	    panel.setRangeZoomable(false);
	    panel.setVisible(true);
	}
	public void show(){
		frame.setVisible(true);
		frame.pack();
	}
}