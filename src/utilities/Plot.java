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

package utilities;

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Plot {
	private static float [][] datas;
	private static String[] names;
	private static XYTextAnnotation[] annotation;
	
	public static void setNames(String... v){
		names = v;
	}
	
	public static void plot(float[]... arrays){
		new Plot().createFrame(arrays);
	}
	public static void addBar(int[] barPos){
		annotation = new XYTextAnnotation[barPos.length];
		for (int i = 1; i < barPos.length; i++) {
				annotation[i] = new XYTextAnnotation(String.valueOf(Math.round(barPos[i]))+" SEP", barPos[i], 550);
				annotation[i].setFont(new Font("Lucida Grande", Font.PLAIN, 11));
				annotation[i].setRotationAngle(200); 
		}
	}
	private void createFrame(float[]... data) {	 
		datas = data;
		createChart();
	}
	private void createChart(){
		JFreeChart chart = ChartFactory.createXYLineChart(
				"plotting", // chart title
	            "X Axis", // x axis label
	            "Y Axis", // y axis label
	            createMultiple(datas.length), // data 
	            PlotOrientation.VERTICAL,
	            true, // include legend
	            true, // tooltips
	            true // urls
	            );	

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);
		renderer.setBaseLinesVisible(true);
		XYPlot plot = (XYPlot) chart.getPlot();
	    plot.setRenderer(renderer);  
	    if(annotation != null){
	    	for (int i = 1; i < annotation.length; i++) {
				plot.addAnnotation(annotation[i]);
		    }
	    }
	    ChartFrame frame = new ChartFrame("Multiple Plot", chart);
//	    frame.getChartPanel().setRangeZoomable(false);
	    frame.setVisible(true);
	    frame.pack();
	}
	
	private XYDataset createMultiple(int number) {
		float[] xArray = new float[datas[0].length];
		for (int i = 0; i < xArray.length; i++) {
			xArray[i] = i;
		}
		XYSeriesCollection tempResult = new XYSeriesCollection();
        for (int i = 0; i < number; i++) {
        	XYSeries tempSerie = null;
        	if(names == null){
        		tempSerie = new XYSeries("Line " + String.valueOf(i));
        	} else {
        		tempSerie = new XYSeries(names[i]);
        	}
        	for (int j = 0; j <datas[i].length; j++) {
        		double x = xArray[j];
        		double y = datas[i][j];
        		tempSerie.add(x, y);
        	}
        	tempResult.addSeries(tempSerie);			
		}
        return tempResult;
    }
	
	
}