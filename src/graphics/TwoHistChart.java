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

import org.jfree.chart.ChartFrame;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeriesCollection;

import backEnd.MakamBox;

public class TwoHistChart {
	private ChartFrame frame;
	private HistogramChart chartSong,chartRec;
	private String frameName;
	private XYPlot plot;
	private XYLineAndShapeRenderer renderer;
	private XYSeriesCollection collectionRec;
	
	public TwoHistChart(MakamBox obj,MakamBox recObj,boolean withTemplate) throws Exception{
		chartSong = obj.getHistogramChart();
		chartRec = recObj.getHistogramChart();
		chartSong.createOnlyChart(withTemplate);
		chartRec.createOnlyChart(false);
		renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesPaint(0, Color.BLACK);
		renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    renderer.setSeriesLinesVisible(1, false);
	    renderer.setSeriesShapesVisible(1, true);
	    renderer.setSeriesLinesVisible(2, false);
	    renderer.setSeriesShapesVisible(2, true);
	    renderer.setSeriesLinesVisible(3, false);
	    renderer.setSeriesShapesVisible(3, true);
	    collectionRec = chartRec.getCollection();
	    plot = (XYPlot) chartSong.getChart().getPlot();
	    plot.setDataset(1,collectionRec);
	    plot.setRenderer(1,renderer);
	    frameName = "Song Histogram & Record Histogram";
	    repaint();
	}
	public void repaint(){
		frame = new ChartFrame(frameName, chartSong.getChart());
		frame.getChartPanel().setRangeZoomable(false);
        frame.pack();
        frame.setVisible(true);
	}
	
}