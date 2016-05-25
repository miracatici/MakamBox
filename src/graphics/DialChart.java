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

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;



public class DialChart extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultValueDataset dataset = new DefaultValueDataset(0);
	private DialPlot plot ;
  
  public DialChart() {
    setPreferredSize(new Dimension(270, 270));
    add(buildDialPlot(0, 1000, 100));
    setVisible(true);
    repaint();
  }

  private ChartPanel buildDialPlot(int minimumValue, int maximumValue,int majorTickGap) {
    plot = new DialPlot(dataset);
    plot.setDialFrame(new StandardDialFrame());
    plot.addLayer(new DialValueIndicator());
    plot.addLayer(new DialPointer.Pointer());

    StandardDialScale scale = new StandardDialScale(minimumValue, maximumValue,-120, -300, majorTickGap, majorTickGap - 1);
    scale.setTickRadius(0.88);
    scale.setTickLabelOffset(0.20);
    plot.addScale(0, scale);
    return new ChartPanel(new JFreeChart(plot));
  }
  public void change(float f){
	  dataset = new DefaultValueDataset(f);
	  plot.setDataset(dataset);
  }
}