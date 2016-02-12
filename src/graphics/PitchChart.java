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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

public class PitchChart {
	
	private static JFreeChart chart;
	private ChartPanel panel;
	private XYPlot plot;
	private XYLineAndShapeRenderer renderer;
	private XYSeriesCollection result;
	private Player play;
	private XYSeries series;
	private float[] data,time;
	private double xPrev,xCurr;
	private float bufferSize,frameRate;
	
	public PitchChart(MakamBox obj) throws Exception{
		data = obj.getPitchTrackData();       
        time = new float[data.length];
        bufferSize = obj.getPitchDetection().getBufferSize();
        for (int i = 0; i < time.length; i++) {
			time[i] = i*(bufferSize/obj.getWavefile().getSampleRate());
		}
        play = obj.getPlayer();
        frameRate = obj.getWavefile().getFrameRate();  
	}
	public void createFrame() {
		chart = ChartFactory.createXYLineChart(
				"", // chart title
	            "Time (sec)", // x axis label
	            "Pitches (Hertz)", // y axis label
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
}