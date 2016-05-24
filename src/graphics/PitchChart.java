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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import backEnd.MakamBox;
import backEnd.Player;
import utilities.AudioUtilities;

public class PitchChart {
	
	private String yLabel, xLabel;
	private JPanel contentPanel,buttonPanel;
	private JDialog frame;
	private static JFreeChart chart,bigChart;
	private ChartPanel panel,bigPanel;
	private XYPlot plot;
	private XYLineAndShapeRenderer renderer;
	private XYSeriesCollection result;
	private Player play;
	private XYSeries series;
	private int[] centData;
	private float[] data,time;
	private double xPrev,xCurr;
	private float bufferSize,frameRate;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public PitchChart(MakamBox obj) throws Exception{		
		data = obj.getPitchTrackData(); 
		centData = AudioUtilities.hertzToCent(data);
		time = new float[data.length];
        bufferSize = obj.getPitchDetection().getBufferSize();
        for (int i = 0; i < time.length; i++) {
			time[i] = i*(bufferSize/obj.getWavefile().getSampleRate());
		}
        play = obj.getPlayer();
        frameRate = obj.getWavefile().getFrameRate();  
		xLabel = "Time (sec)"; yLabel = "Pitches (Hertz)";
	}

	/**
	 * @throws CloneNotSupportedException 
	 * @wbp.parser.entryPoint
	 */
	public void createDialog() throws CloneNotSupportedException{
		frame = new JDialog();
		frame.getContentPane().setPreferredSize(new Dimension(640, 515));
		frame.getContentPane().setLayout(new BorderLayout());
		
		contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(640, 480));
		contentPanel.setSize(640, 480);
		contentPanel.setMinimumSize(new Dimension(640, 480));
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		buttonPanel = new JPanel();
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		createFrame2(data,"Pitches (Cent)",false);

		final JCheckBox rangeZoom = new JCheckBox("Vertical Zoom On");
		rangeZoom.setSelected(true);
		rangeZoom.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(rangeZoom.isSelected()){
					rangeZoom.setText("Vertical Zoom On");
					bigPanel.setRangeZoomable(true);
				} else {
					rangeZoom.setText("Vertical Zoom Off");
					bigPanel.setRangeZoomable(false);
				}
			}	
		});
		buttonPanel.add(rangeZoom);

		JRadioButton rdFrequency = new JRadioButton("Frequency");
		buttonPanel.add(rdFrequency);
		buttonGroup.add(rdFrequency);
		rdFrequency.setSelected(true);
		
		JRadioButton rdCent = new JRadioButton("Cent");
		buttonPanel.add(rdCent);
		buttonGroup.add(rdCent);
		rdCent.setSelected(false);
		
		final JToggleButton toggleCent = new JToggleButton("Absolute");
		buttonPanel.add(toggleCent);
		toggleCent.setEnabled(false);
		toggleCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(toggleCent.getText().equals("Absolute")){
					toggleCent.setText("Interval");
				} else {
					toggleCent.setText("Absolute");
				}
			}
		});
		
		JButton btnExit = new JButton("Exit");
		buttonPanel.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		rdCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleCent.setEnabled(true);
			}
		});
		rdFrequency.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleCent.setEnabled(false);
			}
		});
									
		
		frame.getContentPane().add(bigPanel);
		frame.setVisible(true);
		frame.repaint();
		frame.pack();
	}
	public void createFrame() {
		chart = ChartFactory.createXYLineChart(
				"", // chart title
	            xLabel, // x axis label
	            yLabel, // y axis label
	            createDataset(time,data), // data 
	            PlotOrientation.VERTICAL,
	            false, // include legend
	            true, // tooltips
	            false // urls
	            );
		plot = (XYPlot) chart.getPlot();
		renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShape(0, ShapeUtilities.createDiamond(1));
		renderer.setSeriesLinesVisible(0, false);
	    renderer.setSeriesShapesVisible(0, true);
	    renderer.setSeriesPaint(0, Color.BLACK);
	    plot.setRenderer(renderer);   	
	    
	    panel = new ChartPanel(chart,true);
	    panel.setPreferredSize(new Dimension(713,230));
	    panel.setRangeZoomable(false);
        panel.addMouseListener(new MouseListener(){
	    	@Override
			public void mousePressed(MouseEvent arg0) {
				ChartMouseEvent event = new ChartMouseEvent(chart, arg0, null);
				xPrev = chart.getXYPlot().getDomainAxis().java2DToValue(
								event.getTrigger().getX(), 
								panel.getScreenDataArea(), 
								chart.getXYPlot().getDomainAxisEdge());				
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				ChartMouseEvent event = new ChartMouseEvent(chart, arg0, null);			
				xCurr = chart.getXYPlot().getDomainAxis().java2DToValue
						(event.getTrigger().getX(), 
								panel.getScreenDataArea(), 
								chart.getXYPlot().getDomainAxisEdge());	
				if(xCurr>xPrev){
					xPrev = chart.getXYPlot().getDomainAxis().getRange().getLowerBound();
					xCurr = chart.getXYPlot().getDomainAxis().getRange().getUpperBound();
					play.setLoopPoint((int)(xPrev*frameRate),(int)(xCurr*frameRate));
					WaveChart.zoom(xPrev, xCurr);
				} else if(xCurr==xPrev){
					play.setPosition((int)(xPrev*frameRate));
				} else if (xCurr<xPrev){
					play.stopButton();
					WaveChart.zoom(chart.getXYPlot().getDomainAxis().getRange().getLowerBound(),
							chart.getXYPlot().getDomainAxis().getRange().getUpperBound());
				}
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
	    });
	}
    private XYDataset createDataset(float[] xx,float[] yy) {
        result = new XYSeriesCollection();
        series = new XYSeries("Song Pitch Track");
        for (int i = 0; i <xx.length; i++) {
        	if(yy[i]!=0){
	        	double x = xx[i];
	            double y = yy[i];
	            if(y<5000){
	            	series.add(x, y);
	            }
        	}
        }
        result.addSeries(series);   
        return result;
    }
	public ChartPanel getPanel() {
		return panel;
	}
	public static void zoom(double a, double b){
		chart.getXYPlot().getDomainAxis().setRange(a, b);
	}
	public XYPlot getXYPlot(){
		return plot;
	}
	public void createFrame2(float[] dat, String yLabel, boolean respectTonic) {
		bigChart = ChartFactory.createXYLineChart(
				"", // chart title
	            xLabel, // x axis label
	            yLabel, // y axis label
	            createDataset2(time,dat,respectTonic), // data 
	            PlotOrientation.VERTICAL,
	            false, // include legend
	            true, // tooltips
	            false // urls
	            );
		XYPlot plot = (XYPlot) bigChart.getPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShape(0, ShapeUtilities.createDiamond(1));
		renderer.setSeriesLinesVisible(0, false);
	    renderer.setSeriesShapesVisible(0, true);
	    renderer.setSeriesPaint(0, Color.BLACK);
	    plot.setRenderer(renderer);   	
	    
	    bigPanel = new ChartPanel(bigChart,true);
	    bigPanel.setPreferredSize(new Dimension(713,230));
	    bigPanel.setRangeZoomable(true);
	    bigPanel.addMouseListener(new MouseListener(){
	    	@Override
			public void mousePressed(MouseEvent arg0) {
				ChartMouseEvent event = new ChartMouseEvent(bigChart, arg0, null);
				xPrev = bigChart.getXYPlot().getDomainAxis().java2DToValue(
								event.getTrigger().getX(), 
								panel.getScreenDataArea(), 
								bigChart.getXYPlot().getDomainAxisEdge());				
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				ChartMouseEvent event = new ChartMouseEvent(bigChart, arg0, null);			
				xCurr = bigChart.getXYPlot().getDomainAxis().java2DToValue
						(event.getTrigger().getX(), 
								panel.getScreenDataArea(), 
								bigChart.getXYPlot().getDomainAxisEdge());	
				if(xCurr>xPrev){
					xPrev = bigChart.getXYPlot().getDomainAxis().getRange().getLowerBound();
					xCurr = bigChart.getXYPlot().getDomainAxis().getRange().getUpperBound();
					play.setLoopPoint((int)(xPrev*frameRate),(int)(xCurr*frameRate));
					WaveChart.zoom(xPrev, xCurr);
				} else if(xCurr==xPrev){
					play.setPosition((int)(xPrev*frameRate));
				} else if (xCurr<xPrev){
					play.stopButton();
					WaveChart.zoom(bigChart.getXYPlot().getDomainAxis().getRange().getLowerBound(),
							bigChart.getXYPlot().getDomainAxis().getRange().getUpperBound());
				}
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
	    });
	}
    private XYDataset createDataset2(float[] xx,float[] yy, boolean respectTonic) {
        result = new XYSeriesCollection();
        series = new XYSeries("Song Pitch Track");
        if(respectTonic){
        	for (int i = 0; i <xx.length; i++) {
            	if(yy[i]!=0){
    	        	double x = xx[i];
    	            double y = yy[i];
    	            if(y<5000){
    	            	series.add(x, y);
    	            }
            	}
            }
        } else {
        	for (int i = 0; i <xx.length; i++) {
            	if(yy[i]!=0){
    	        	double x = xx[i];
    	            double y = yy[i];
    	            if(y<5000){
    	            	series.add(x, y);
    	            }
            	}
            }
        }
        result.addSeries(series);   
        return result;
    }
}