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

import javax.swing.JFrame;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.XYItemEntity;

import backEnd.MakamBox;
import backEnd.Player;
import backEnd.SineSynth;

public class TuneChart {
	private JFrame frame;
	private ChartPanel panel;
	private HistogramChart histChart;
	private JFreeChart chart;
	private String frameName;
	private SineSynth sineWave;
	private Player sinePlayer;
	
	public TuneChart() {
		sineWave = new SineSynth();
		try {
			sinePlayer = new Player(sineWave.getSineWave(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void createFrame(MakamBox obj) throws Exception {
		histChart = obj.getHistogramChart();
		histChart.createOnlyChart(false);
		chart = histChart.getChart();
		frameName = "Tune Sound from Histogram";
		createPanel();
		repaint();
	}
	private void createPanel(){
		panel = new ChartPanel(chart,true);
	    panel.addChartMouseListener(new ChartMouseListener(){
	    	@Override
			public void chartMouseClicked(ChartMouseEvent event) {
	    		if(event.getEntity() instanceof XYItemEntity){
					double chartX = chart.getXYPlot().getDomainAxis().java2DToValue(
							event.getTrigger().getX(), 
							panel.getScreenDataArea(), 
							chart.getXYPlot().getDomainAxisEdge());	
					sineWave.synthSine((float) chartX);
					try {
						sinePlayer.setPlayer(sineWave.getSineWave());
					} catch (Exception e) {
						e.printStackTrace();
					}
					sinePlayer.play();
		    		}
	    		}
	    		@Override
				public void chartMouseMoved(ChartMouseEvent event) {}
	    });
	    panel.setPreferredSize(new Dimension(713,280));
	    panel.setRangeZoomable(false);
	    panel.setVisible(true);
	}
	public void repaint(){
		frame = new JFrame(frameName);
		frame.add(panel);
		frame.setVisible(true);
        frame.pack();
	}
}