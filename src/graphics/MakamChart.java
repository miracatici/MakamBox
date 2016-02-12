package graphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import backEnd.MakamBox;
import datas.Makam;
import datas.TuningSystem;

public class MakamChart extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7502229854331926766L;
	private final JPanel contentPanel = new JPanel();
	private int count;
	private final float comma;
	private float shiftAmount,tonicCent, mincent;
	private float[][] datas;
	private String[] names;
	private boolean euroTheory;
	private XYSeriesCollection system;
	private XYSeriesCollection tempResult;
	private XYTextAnnotation[] annotation,sysAnnotation;
	public static boolean isComma=false;
	JList<String> la; String[] sa;TreeMap<String, TuningSystem> ts; MakamBox so;

	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public MakamChart(final MakamBox box, boolean euro, float[]... data) {
		setBounds(100, 100, 679, 487);
		getContentPane().setLayout(new BorderLayout(0, 0));
		contentPanel.setLayout(new BorderLayout(0, 0));
		getContentPane().add(contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JButton btnSetComma = new JButton("Set Comma");
				btnSetComma.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(btnSetComma.getText().equals("Set Comma")){
							try {
								isComma = true;
								setSystem();
								addAnnotation(box.getMakam().getPeaksComma());
								createFrame2();
								btnSetComma.setText("Set Cent");
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						} else {
							try {
								isComma = false;
								setSystem();
								addAnnotation(box.getMakam().getPeaksCent());
								createFrame();
								btnSetComma.setText("Set Comma");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
				buttonPane.add(btnSetComma);
			}
			{
				JButton okButton = new JButton("Exit");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						isComma = false;
						dispose();
					}
				});
				okButton.setActionCommand("Exit");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		datas = data.clone();
        euroTheory = euro;
		comma = 1200f/159f;
		try {
			shiftAmount = box.getMakam().getShiftAmount();
			tonicCent = box.getTonicCent();
			mincent = box.getHistogram().getMinimum();
		} catch (Exception e) {
			if(box==null){
				JOptionPane.showMessageDialog(null,"Please load a recording to the first slot");
			} else {
				JOptionPane.showMessageDialog(null, "Makam Chart couldn't created");
			}		
		}
	}
	public MakamChart(boolean euro, float[]... data){
		getContentPane().setLayout(new BorderLayout(0,0));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				final JButton btnSetComma = new JButton("Set Comma");
				btnSetComma.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(btnSetComma.getText().equals("Set Comma")){
							createFrame2();
							btnSetComma.setText("Set Cent");
						} else {
							createFrame();
							btnSetComma.setText("Set Comma");
						}
					}
				});
				buttonPane.add(btnSetComma);
			}
			{
				JButton okButton = new JButton("Exit");
				okButton.setActionCommand("Exit");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		datas = data.clone();
        euroTheory = euro;
		comma = 1200f/159f;
		shiftAmount = 1635;
		tonicCent = 0;
		mincent = 0;
	}
	public void createFrame() {
		String frameName = "Makam Histogram Comparison"; //  Makam - Eser Histogram Karşılaştırması
		JFreeChart chart = ChartFactory.createXYLineChart(
				frameName, // chart title
	            "Intervals (Cent)", // x axis label Frekans (Holder Koma)
	            "Frequency of Occurrence", // y axis label   Çalınma Sıklığı
	            createMultiple(datas.length),
	            PlotOrientation.VERTICAL,
	            true, // include legend
	            true, // tooltips
	            true // urls
		);	

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);
		renderer.setBaseLinesVisible(true);
	    XYPlot plot = (XYPlot) chart.getPlot();
	    if(euroTheory){
	    	BasicStroke grids = new BasicStroke(1.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 100.0f, new float[] {10f}, 1.0f);
	    	plot.setDomainGridlinesVisible(true);
	    	plot.setDomainGridlinePaint(Color.BLUE);
	    	plot.setDomainGridlineStroke(grids);
	    }
	    if(!(system==null)){
	    	for (int i = 0; i < system.getSeriesCount(); i++) {
	    		tempResult.addSeries(system.getSeries(i));
	    		renderer.setSeriesShape(count+i, new Ellipse2D.Double(-3,0,6,6));
	    		renderer.setSeriesLinesVisible(count+i, false);
	    	    renderer.setSeriesShapesVisible(count+i, true); 
	    	    for (int j = 1; j < sysAnnotation.length; j++) {
					plot.addAnnotation(sysAnnotation[j]);
				}
			}
	    }
	    if(!(annotation==null)){
	    	for (int i = 1; i < annotation.length; i++) {
				plot.addAnnotation(annotation[i]);
			}
	    }
//	    plot.setBackgroundPaint(Color.WHITE);
	    plot.setRenderer(renderer);  
	    ChartPanel frame = new ChartPanel(chart, true);
	    frame.setRangeZoomable(false);
	    contentPanel.removeAll();
	    contentPanel.add(frame);
	    setVisible(true);
	    repaint();
	    pack();
	} 
	public void createFrame2() {
		String frameName = "Makam Histogram Comparison"; //  Makam - Eser Histogram Karşılaştırması
		JFreeChart chart = ChartFactory.createXYLineChart(
				frameName, // chart title
	            "Intervals (Holderian Comma)", // x axis label Frekans (Holder Koma)
	            "Frequency of Occurrence", // y axis label   Çalınma Sıklığı
	            createMultiple2(datas.length),
	            PlotOrientation.VERTICAL,
	            true, // include legend
	            true, // tooltips
	            true // urls
		);	

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setBaseShapesVisible(false);
		renderer.setBaseLinesVisible(true);
	    XYPlot plot = (XYPlot) chart.getPlot();
	    if(euroTheory){
	    	BasicStroke grids = new BasicStroke(1.0f,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 100.0f, new float[] {10f}, 1.0f);
	    	plot.setDomainGridlinesVisible(true);
	    	plot.setDomainGridlinePaint(Color.BLUE);
	    	plot.setDomainGridlineStroke(grids);
	    }
	    if(!(system==null)){
	    	for (int i = 0; i < system.getSeriesCount(); i++) {
	    		tempResult.addSeries(system.getSeries(i));
	    		renderer.setSeriesShape(count+i, new Ellipse2D.Double(-3,0,6,6));
	    		renderer.setSeriesLinesVisible(count+i, false);
	    	    renderer.setSeriesShapesVisible(count+i, true);
	            for (int j = 1; j < sysAnnotation.length; j++) {
					plot.addAnnotation(sysAnnotation[j]);
				}
			}
	    }
	    if(!(annotation==null)){
	    	for (int i = 1; i < annotation.length; i++) {
				plot.addAnnotation(annotation[i]);
			}
	    }
//	    plot.setBackgroundPaint(Color.WHITE);
	    plot.setRenderer(renderer);   
	    plot.getDomainAxis().setRange(-200, 200);
	    ChartPanel frame = new ChartPanel(chart, true);
	    frame.setRangeZoomable(false);
	    contentPanel.removeAll();
	    contentPanel.add(frame);
	    setVisible(true);
	    repaint();
	    pack();
	} 
    private XYDataset createMultiple(int number) {
    	count = number;
        tempResult = new XYSeriesCollection();
        float[] freq = new float[3272];
        for (int i = 0; i < 3272; i++) {
			freq[i] = (i-shiftAmount)*comma+mincent;
        }
        for (int i = 0; i < number; i++) {
        	XYSeries tempSerie = new XYSeries(names[i]);
        	for (int j = 1200; j <2200; j++) {
        		double x = freq[j] - tonicCent;
        		double y = datas[i][j];
        		if(x<2400 && x>-2400){
        			tempSerie.add(x, y);
        		}        	}
        	tempResult.addSeries(tempSerie);
		}
        return tempResult;
    }
    private XYDataset createMultiple2(int number) {
    	count = number;
        tempResult = new XYSeriesCollection();
        float[] freq = new float[3272];
        for (int i = 0; i < 3272; i++) {
			freq[i] = (i-shiftAmount)*comma+mincent;
        }
        for (int i = 0; i < number; i++) {
        	XYSeries tempSerie = new XYSeries(names[i]);
        	for (int j = 1200; j <2200; j++) {
        		double x = (freq[j] - tonicCent)/(1200f/53f);
        		double y = datas[i][j];
        		if(x<2400 && x>-2400){
        			tempSerie.add(x, y);
        		}
        	}
        	tempResult.addSeries(tempSerie);
		}
        return tempResult;
    }
	public void setNames(String... v){
		this.names = v;
	}
	public void addSystem(XYSeriesCollection add){
		this.system = add;
	}
	public void addAnnotation(float[] peaks){
		annotation = new XYTextAnnotation[peaks.length];
		for (int i = 1; i < peaks.length; i++) {
			if(isComma){
				annotation[i] = new XYTextAnnotation(String.valueOf(Math.round(peaks[i]*100.0)/100.0)+" comma", peaks[i], 0.01);
			} else {				
				annotation[i] = new XYTextAnnotation(String.valueOf(Math.round(peaks[i]*100.0)/100.0)+" cent", peaks[i], 0.01);
			}
			annotation[i].setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			annotation[i].setRotationAngle(200);
		}
	}
	public void addSysAnnotation(float[] peaks,float offset){
		sysAnnotation = new XYTextAnnotation[peaks.length];
		for (int i = 1; i < peaks.length; i++) {
			if(isComma){
				sysAnnotation[i] = new XYTextAnnotation(String.valueOf(Math.round((peaks[i]/(1200f/53f))*100.0)/100.0)+" comma", (peaks[i]/(1200f/53f)), offset);
			} else {				
				sysAnnotation[i] = new XYTextAnnotation(String.valueOf(Math.round(peaks[i]*100.0)/100.0)+" cent", peaks[i], offset);
			}
			sysAnnotation[i].setFont(new Font("Lucida Grande", Font.PLAIN, 11));
			sysAnnotation[i].setRotationAngle(200);
		}
	}
	public void setProp(JList<String> l, String[] ss,TreeMap<String, TuningSystem> t, MakamBox s1){
		la = l; sa = ss; ts = t; so = s1;	
		setSystem();
	}
	public void setSystem(){
		int[] selected = la.getSelectedIndices();
		String[] tempName = new String[selected.length];
		float[][] temp = new float[selected.length][];
		Makam b = null;
		for (int i = 0; i < selected.length; i++) {
			String name = sa[selected[i]];
			tempName[i] = name;
			TreeMap<String, Makam> a = null;
			try {
				a = ts.get(name).getMakamsMap();
			} catch( Exception e){
				JOptionPane.showMessageDialog(null,"Couldn't load tuning system data");
				return;
			}
			try {
				b = a.get(so.getMakamName());
				temp[i] = b.getIntervalArray();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,"Please select a file");
				return;
			}
		}
		if (selected.length>1){
			addSysAnnotation(new float[1], 0);
		} else {
			addSysAnnotation(temp[0], 0.016f);
		}
		XYSeriesCollection tempResult = new XYSeriesCollection();
        float[] freq = new float[3272];
        for (int i = 0; i < 3272; i++) {
			freq[i] = i*comma;
		}
           
    	for (int i = 0; i < selected.length; i++) {
        	XYSeries tempSerie = new XYSeries(tempName[i]);
        	for (int j = 0; j <temp[i].length; j++) {
        		double x;
        		if(isComma){
        			x = temp[i][j] / (1200f/53f);
        		} else {        			
        			x = temp[i][j];
        		}
        		double y = 0.01 + 0.003*(1+i);
        		tempSerie.add(x, y);
        	}
        	tempResult.addSeries(tempSerie);	
		}
    	system = tempResult;
	}
}
