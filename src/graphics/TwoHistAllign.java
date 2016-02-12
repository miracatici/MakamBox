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

import backEnd.MakamBox;

public class TwoHistAllign {
	private int mincent,mincent2,shift,shift2,ind;
	private float comma,songTonic,recTonic;
	private float[] songData,recData,template,cent,cent2;
	private ChartFrame frame;
	private JFreeChart chart;
	private String histName,histName2,tempName,frameName;
	private XYPlot plot;
	private XYLineAndShapeRenderer renderer;
	private XYSeriesCollection result;
	private XYSeries series, series2,series3, ton;
	
	public TwoHistAllign(MakamBox song,MakamBox rec ) throws Exception{
		try {
			histName = song.getMakam().getName();
			tempName = song.getMakam().getMakamName();
			template = song.getMakam().getMakamTemplate();
			songData = song.getMakam().getShiftHistogram();        
			shift = song.getMakam().getShiftAmount();
			songTonic = song.getMakam().getTonicCent();
			histName2= rec.getMakam().getName();
			recData = rec.getMakam().getShiftHistogram();
			shift2 = rec.getMakam().getShiftAmount();
			recTonic = rec.getMakam().getTonicCent();
		} catch (Exception e){
			JOptionPane.showMessageDialog(null,"Please detect Makam");
			return;
		}
		
		mincent = song.getHistogram().getMinimum();
		mincent2 = rec.getHistogram().getMinimum();		
        
        comma = 1200f/159f;
        cent = new float[songData.length];
        for (int i = shift; i < shift+1636; i++) {
			cent[i] = (i-shift)*comma+mincent;
			if(cent[i]==songTonic){
				ind = i;
			}
		}
        cent2 = new float[recData.length];
        for (int i = shift2; i < shift2+1636; i++) {
			cent2[i] = ((i-shift2)*comma+mincent2);
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
        series = new XYSeries(histName);
        series2 =  new XYSeries(histName2);
        series3 = new XYSeries("Template Histogram = " + tempName);
        ton = new XYSeries("Tonic Frequency = " + String.valueOf(songTonic));
        
        for (int i = 0; i <2636; i++) {
            double x = xx[i]-songTonic;
            double y = yy[i];
            double k = cent2[i]-recTonic;
            double t = recData[i];
            double z = template[i];
            if(x<2400 && x>-2400){
	            series.add(x, y);
	            series2.add(k,t);
	            series3.add(x,z);
            }
        }
        ton.add(0,songData[ind]);
        result.addSeries(series);
        result.addSeries(series2);
        result.addSeries(ton);
        if(temp){
        	result.addSeries(series3);
        }
        return result;
    }
	public void createOnlyChart(boolean withTemplate){
		chart = ChartFactory.createXYLineChart(
				histName, // chart title
	            "Intervals (Cent)", // x axis label
	            "Frequency of Occuarence", // y axis label
	            createDataset(cent,songData,withTemplate), // data 
	            PlotOrientation.VERTICAL,
	            true, // include legend
	            true, // tooltips
	            true // urls
	            );	

		renderer = new XYLineAndShapeRenderer();
		
	    renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    renderer.setSeriesPaint(0, Color.RED);
	    
	    renderer.setSeriesLinesVisible(1, true);
	    renderer.setSeriesShapesVisible(1, false); 
	    renderer.setSeriesPaint(1, Color.BLACK);
	    
	    renderer.setSeriesLinesVisible(2, false);
	    renderer.setSeriesShapesVisible(2, true); 
	    renderer.setSeriesPaint(2, Color.BLUE);
	    
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
	public XYSeriesCollection getCollection() {
		return result;
	}
	public JFreeChart getChart() {
		return chart;
	}
	public XYPlot getPlot() {
		return plot;
	}
	public int getShift() {
		return shift;
	}
	public void setShift(int shift) {
		this.shift = shift;
	}
}