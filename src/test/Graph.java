package test;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;

import applied.SelectCulture;
import backEnd.MakamBox;

public class Graph {
	public static void main (String [] ar) throws Exception{

		SelectCulture.getCulture();
		System.out.println("1");
		MakamBox m = new MakamBox("111.mp3",null);
		System.out.println("1");
		MakamBox m2 = new MakamBox("222.mp3",null);
		System.out.println("1");
		m.createMakam();
		System.out.println("1");
		m2.createMakam();
		System.out.println("1");
		m.showPitchChart();
		System.out.println("1");
		m2.showPitchChart();
		
		m.showHistogram();
		m2.showHistogram();
		
		// Create a single plot containing both the scatter and line
		XYPlot plot = new XYPlot();
		System.out.println("1");
		/* SETUP SCATTER */

		// Create the scatter data, renderer, and axis
		XYDataset collection1 = m.getPitchChart().getXYPlot().getDataset();//
		XYItemRenderer renderer1 = new XYLineAndShapeRenderer(true, false);   // Shapes only
		ValueAxis domain1 = new NumberAxis("Time (sec)");
		ValueAxis range1 = new NumberAxis("Frequency (hz)");
		System.out.println("1");

		// Set the scatter data, renderer, and axis into plot
		plot.setDataset(0, collection1);
		plot.setRenderer(0, renderer1);
		plot.setDomainAxis(0, domain1);
		plot.setRangeAxis(0, range1);
		System.out.println("1");

		// Map the scatter to the first Domain and first Range
		plot.mapDatasetToDomainAxis(0, 0);
		plot.mapDatasetToRangeAxis(0, 0);
		System.out.println("1");

		/* SETUP LINE */

		// Create the line data, renderer, and axis
		XYDataset collection2 = m2.getPitchChart().getXYPlot().getDataset();;
		XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);   // Lines only
		System.out.println("1");

		// Set the line data, renderer, and axis into plot
		plot.setDataset(1, collection2);
		plot.setRenderer(1, renderer2);
		System.out.println("1");

		// Map the line to the second Domain and second Range
		plot.mapDatasetToDomainAxis(1, 0);
		plot.mapDatasetToRangeAxis(1, 0);
		System.out.println("1");

		// Create the chart with the plot and a legend
		JFreeChart chart = new JFreeChart("Analysis Result", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
		ChartFrame frame = new ChartFrame("Analysis Result", chart);
		frame.getChartPanel().setRangeZoomable(false);
        frame.setVisible(true);
        frame.pack();

	}
}
