package graphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import backEnd.MakamBox;

public class IntervalChart extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1320756884950338285L;
	private final JPanel contentPanel = new JPanel();
	private int mincent,shift,ind;
	private float comma,tonic;
	private float[] data,cent;
	private ChartPanel panel;
	private JFreeChart chart;
	private String histName,frameName;
	private XYPlot plot;
	private XYLineAndShapeRenderer renderer;
	private Stroke grids;
	private XYSeriesCollection result;
	private XYSeries series,ton;


	/**
	 * Create the dialog.
	 */
	public IntervalChart(MakamBox obj) {
		setTitle(frameName);
		setBounds(100, 100, 679, 487);
		getContentPane().setLayout(new BorderLayout(0,0));
		contentPanel.setLayout(new BorderLayout(0, 0));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		final JButton btnSetComma = new JButton("Set Comma");
		btnSetComma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(btnSetComma.getText().equals("Set Comma")){
					setCommaAxis();	
					btnSetComma.setText("Set Cent");
				} else {
					createFrame();
					btnSetComma.setText("Set Comma");
				}
			}
		});
		buttonPane.add(btnSetComma);
	
	
		JButton okButton = new JButton("Exit");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		okButton.setActionCommand("Exit");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
			
		
		// create 'a' array for X axis
			try {
				data = obj.getMakam().getShiftHistogram(); 
			} catch (Exception e){
				JOptionPane.showMessageDialog(null,"Please detect Makam");
				return;
			}
	        cent = new float[data.length];
	        comma = 1200f/159f;
	        try {
				histName = obj.getMakam().getName();
				mincent = obj.getHistogram().getMinimum();
				shift = obj.getMakam().getShiftAmount();
				tonic = obj.getTonicCent();
			} catch (Exception e) {
				e.printStackTrace();
			}
	        for (int i = shift; i < shift+1636; i++) {
				cent[i] = (i-shift)*comma+mincent;
				if(cent[i]==tonic){
					ind = i;
				}
			}
	}
	public void createFrame() {
		
		frameName = "Histogram Data with Intervals";
		chart = ChartFactory.createXYLineChart(
				histName, // chart title
	            "Intervals (Cent)", // x axis label
	            "Frequency of Occurence", // y axis label
	            createDataset(cent,data), // data 
	            PlotOrientation.VERTICAL,
	            true, // include legend
	            true, // tooltips
	            true // urls
	            );		
		plot = (XYPlot) chart.getPlot();
		renderer = new XYLineAndShapeRenderer();
	    renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    renderer.setSeriesPaint(0, Color.RED);
	    renderer.setSeriesPaint(1, Color.BLACK);

	    grids = new BasicStroke(1.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 100.0f, new float[] {10f}, 1.0f);
	    plot.setDomainGridlinesVisible(true);
	    plot.setDomainGridlinePaint(Color.BLUE);
	    plot.setDomainGridlineStroke(grids);
	    plot.setRenderer(renderer);
	
	    panel = new ChartPanel(chart,true);
	    panel.setRangeZoomable(false);
	    panel.setVisible(true);
	    contentPanel.removeAll();
	    contentPanel.add(panel);
	    setVisible(true);
	    repaint();
	    pack();
	}
     
    private XYDataset createDataset(float[] xx,float[] yy) {
        result = new XYSeriesCollection();
        series = new XYSeries("Song Histogram");
        ton = new XYSeries("Tonic Cent = " + String.valueOf(tonic));
        for (int i = 0; i <xx.length; i++) {
	        if(xx[i]<tonic+2000 && xx[i]>tonic-2000){
		    	double x = (xx[i] - tonic);
		        double y = yy[i];
		        series.add(x, y);
	        }
        }
        ton.add(0,data[ind]);
        result.addSeries(series);
        result.addSeries(ton);
        return result;
    }
    private XYDataset createDataset2(float[] xx,float[] yy) {
        result = new XYSeriesCollection();
        series = new XYSeries("Song Histogram");
        ton = new XYSeries("Tonic Cent = " + String.valueOf(tonic));
        for (int i = 0; i <xx.length; i++) {
	        if(xx[i]<tonic+2000 && xx[i]>tonic-2000){
		    	double x = (xx[i] - tonic)/(1200f/53f);
		        double y = yy[i];
		        series.add(x, y);
	        }
        }
        ton.add(0,data[ind]);
        result.addSeries(series);
        result.addSeries(ton);
        return result;
    }
    public void setCommaAxis(){
    	frameName = "Histogram Data with Intervals";
		chart = ChartFactory.createXYLineChart(
				histName, // chart title
	            "Intervals (Holderian Comma)", // x axis label
	            "Frequency of Occurence", // y axis label
	            createDataset2(cent,data), // data 
	            PlotOrientation.VERTICAL,
	            true, // include legend
	            true, // tooltips
	            true ); // urls		
		plot = (XYPlot) chart.getPlot();
		renderer = new XYLineAndShapeRenderer();
	    renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    renderer.setSeriesPaint(0, Color.RED);
	    renderer.setSeriesPaint(1, Color.BLACK);

	    grids = new BasicStroke(1.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 100.0f, new float[] {10f}, 1.0f);
	    plot.setDomainGridlinesVisible(true);
	    plot.setDomainGridlinePaint(Color.BLUE);
	    plot.setDomainGridlineStroke(grids);
	    plot.setRenderer(renderer);
	    
	    panel = new ChartPanel(chart,true);
	    panel.setRangeZoomable(false);
	    panel.setVisible(true);
	    contentPanel.removeAll();
	    contentPanel.add(panel);
	    setVisible(true);
	    repaint();
	    pack();
    }
}
