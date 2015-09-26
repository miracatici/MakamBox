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

import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import utilities.AudioUtilities;
import backEnd.MakamBox;

public class HistogramChart {
	private int mincent,shift,ind;
	private int[] peaks;
	private float comma,tonic;
	private float[] data,template,freq;
	private ChartFrame frame;
	private JFreeChart chart;
	private String histName,tempName,frameName;
	private XYSeriesCollection result;
	private XYPlot plot;
	
	public HistogramChart(MakamBox obj) {
		try {
			template = obj.getMakam().getMakamTemplate();
			data = obj.getMakam().getShiftHistogram(); 
			peaks = obj.getMakam().getPeaks();
	        histName = obj.getMakam().getName();
	        tempName = obj.getMakam().getMakamName();
	        shift = obj.getMakam().getShiftAmount();
	        tonic = obj.getTonicHz();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Please detect Makam");
			return;
		}
        try {
			mincent = obj.getHistogram().getMinimum();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Couldn't create histogram");
			return;
		}
        freq = new float[data.length];
        comma = 1200f/159f;
        for (int i = shift; i < shift+1636; i++) {
			freq[i] = AudioUtilities.centToHertz((i-shift)*comma+mincent);
			if(freq[i]==tonic){
				ind = i;
			}
		}
	}
	public void createFrame(boolean withTemplate) {
		if(withTemplate){
			frameName = "Histogram Data with Makam Template";
		} else {
			frameName = "Histogram Data";
		}
		createOnlyChart(withTemplate);
		repaint();
	}
    private XYDataset createDataset(float[] xx,float[] yy,boolean temp) {
        result = new XYSeriesCollection();
        XYSeries series = new XYSeries(histName);
        XYSeries series2 = new XYSeries("Template Histogram = " + tempName);
        XYSeries series3 = new XYSeries("Peaks");

        XYSeries ton = new XYSeries("Tonic Frequency = " + String.valueOf(tonic));
        ton.add(tonic,data[ind]);
        
        for (int i = 0; i <2636; i++) {
            double x = xx[i];
            double y = yy[i];
            double z = template[i];
            if(x<tonic*4 && x>50){
	            series.add(x, y);
	            series2.add(x,z);
            }
        }
        for (int i = 0; i < peaks.length; i++) {
			series3.add(AudioUtilities.centToHertz((peaks[i]-shift)*comma+mincent),data[peaks[i]]);
		}
        result.addSeries(series);
        result.addSeries(ton);
        result.addSeries(series3);
        if(temp){
        	result.addSeries(series2);
        }
        return result;
    }
	public void createOnlyChart(boolean withTemplate){
		chart = ChartFactory.createXYLineChart(
				histName, // chart title
	            "Frequency", // x axis label
	            "Frequency of Occuarence", // y axis label
	            createDataset(freq,data,withTemplate), // data 
	            PlotOrientation.VERTICAL,
	            true, // include legend
	            true, // tooltips
	            true // urls
	            );	

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	    renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    renderer.setSeriesPaint(0, Color.RED);
	    renderer.setSeriesLinesVisible(1, false);
	    renderer.setSeriesShapesVisible(1, true); 
	    renderer.setSeriesPaint(1, Color.BLACK);
	    renderer.setSeriesLinesVisible(2, false);
	    renderer.setSeriesShapesVisible(2, true); 
	    renderer.setSeriesPaint(2, Color.BLACK);
	    renderer.setSeriesLinesVisible(3, true);
	    renderer.setSeriesShapesVisible(3, false); 
	    renderer.setSeriesPaint(3, Color.BLUE);
	    plot = (XYPlot) chart.getPlot();
	    plot.setRenderer(renderer); 
	}
	public void repaint(){
		frame = new ChartFrame(frameName, chart);
		frame.getChartPanel().setRangeZoomable(false);
        frame.setVisible(true);
        frame.pack();
	}
	public JFreeChart getChart() {
		return chart;
	}
	public int getShift() {
		return shift;
	}
	public void setShift(int shift) {
		this.shift = shift;
	}
	public XYSeriesCollection getCollection() {
		return result;
	}
	public XYPlot getXYPlot(){
		return plot;
	}
}