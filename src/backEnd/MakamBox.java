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
package backEnd;

/**
 * MakamBox is an implementation of MakamToolBox Turkish Makam music analysis tool which is developed by Baris Bozkurt
 * The main class to get together all of features of framework that is created for master thesis
 * These constructors need a button object to assign a Stop button for interface
 * 
 * Created @author mirac
 * Bahcesehir University, 2014
 */

import graphics.HistogramChart;
import graphics.IntervalChart;
import graphics.PitchChart;
import graphics.TuneChart;
import graphics.TwoHistAllign;
import graphics.TwoHistChart;
import graphics.WaveChart;

import java.io.File;

import javax.sound.sampled.Clip;
import javax.swing.JButton;

import org.jfree.chart.ChartPanel;

public class MakamBox {
	private Wavefile audio;
	private PitchDetection pitch;
	private Histogram histogram;
	private MakamClassifier makam;
	private PitchShift pitchShift;
	private SineSynth synthed;
	private Player player;
	
	private WaveChart waveChart;
	private PitchChart pitchChart;
	private HistogramChart histoChart;
	private TwoHistChart histRecChart;
	private IntervalChart intervalChart;
	private TuneChart tuneChart;
	private TwoHistAllign allignedHist;
	private JButton btn;
		
	// Constructors
	public MakamBox(File musicFile,JButton b) throws Exception{
		this(new Wavefile(musicFile),b);
	}
	public MakamBox(String path,JButton b) throws Exception{
		this(new Wavefile(new File(path)),b);
	}
	public MakamBox(Wavefile af,JButton b) throws Exception{
		audio = af;
		btn = b;
	}
	
	// Creating objects with control system
	public void createPitchEstimate() throws Exception{
		if(pitch==null){
			pitch = new PitchDetection(audio);
		}
	}
	public void createHistogram() throws Exception{
		createPitchEstimate();
		if(histogram==null){
			histogram = new Histogram(pitch);
		}
	}
	public void createMakam(String makTemp) throws Exception{
		createHistogram();
		makam = new MakamClassifier(histogram);
		makam.measure(makTemp);
	}
	public void createMakam() throws Exception{
		createHistogram();
		makam = new MakamClassifier(histogram);
		makam.measure();
	}
	public void createSynth() throws Exception{
		if(synthed==null){
			synthed = new SineSynth(getPitchDetection());  
		}
	}
	public void createPitchShift(){
		if(pitchShift==null){
			pitchShift = new PitchShift(this);
		}
	}
	public void createPlayer() throws Exception{
		if(player==null){
			player = new Player(this,btn);
		}
	}
	
	// Creating graphics objects with control system
	public void createHistogramChart() throws Exception{
		histoChart = new HistogramChart(this);
	}
	public void createIntervalChart() throws Exception{
		intervalChart = new IntervalChart(this);
	}
	public void createPitchChart() throws Exception{
		if(pitchChart==null){
			pitchChart = new PitchChart(this);
		}
	}
	public void createWaveChart() throws Exception{
		if(waveChart==null){
			waveChart = new WaveChart(this);
		}
	}
	public void createAllignHistogram(MakamBox rec) throws Exception{
		allignedHist = new TwoHistAllign(this,rec);
	}
	/* Because of random records, there's no control cycle */
	public void createHistWithRec(MakamBox rec,boolean withTemplate) throws Exception{  
		histRecChart = new TwoHistChart(this,rec,withTemplate);
	}
	public void createTuneChart() throws Exception{
		if(tuneChart==null){
			tuneChart = new TuneChart();
		}
	}
	
	// Access object methods
	public Wavefile getWavefile(){
		return audio;
	}
	public PitchDetection getPitchDetection() throws Exception{
		createPitchEstimate();
		return pitch;
	}
	public Histogram getHistogram() throws Exception{
		createHistogram();
		return histogram;
	}
	public MakamClassifier getMakam() throws Exception{
		return makam;
	}
	public SineSynth getSynth() throws Exception{
		createSynth();
		return synthed;
	}
	public PitchShift getPitchShift(){
		createPitchShift();
		return pitchShift;
	}
	public Player getPlayer() throws Exception{
		createPlayer();
		return player;
	}
	
	// Access graphics objects methods
	public WaveChart getWaveChart() throws Exception{
		createWaveChart();
		return waveChart;
	}
	public PitchChart getPitchChart() throws Exception{
		createPitchChart();
		return pitchChart;
	}
	public IntervalChart getIntervalChart() throws Exception{
		createIntervalChart();
		return intervalChart;
	}
	public HistogramChart getHistogramChart() throws Exception{
		createHistogramChart();
		return histoChart;
	}
	public TwoHistChart getHistWithRecChart(MakamBox rec,boolean withTemplate) throws Exception{
		createHistWithRec(rec,withTemplate);
		return histRecChart;
	}
	public TuneChart getTuneChart() throws Exception{
		createTuneChart();
		return tuneChart;
	}
	public TwoHistAllign getAllignedHistogram(MakamBox rec) throws Exception{
		createAllignHistogram(rec);
		return allignedHist;
	}
	
	// Methods for objects attribute
	public String getName(){
		return getWavefile().getName();
	}
	public String getPath(){
		return getWavefile().getPath();
	}
	public String getFolder(){
		return getWavefile().getFolder();
	}
	public Clip getClip(){
		return getWavefile().getClip();
	}
	public double getDuration(){
		return getWavefile().getLengthInSecond();
	}
	public float[] getPitchTrackData() throws Exception{
		return getPitchDetection().getPitchResult();
	}
	public float[] getHistogramData() throws Exception{
		return getHistogram().getHistogram();
	}
	public String getMakamName() throws Exception{
		return getMakam().getMakamName();
	}
	public float getTonicHz() throws Exception{
		return getMakam().getTonicHz();
	}
	public float getTonicCent() throws Exception{
		return getMakam().getTonicCent();
	}
	public Wavefile getSynthWave() throws Exception{
		return getSynth().getSynthedWave();
	}
	public Wavefile getShiftedWave() throws Exception{
		return getPitchShift().getShiftedWave();
	}
	public void stopShifting(){
		pitchShift.stopShift();
	}
	public void saveAudio() throws Exception{
		new WaveWriter(getWavefile());
	}
	public void saveSynthed() throws Exception{
		new WaveWriter(getSynth().getSynthedWave());
	}
	
	//Methods for graphics object attributes
	public void showHistogram() throws Exception{
		getHistogramChart().createFrame(false);
	}
	public void showHistWithTemp() throws Exception{
		getHistogramChart().createFrame(true);
	}
	public void showHistWithRec(MakamBox rec,boolean withTemplate) throws Exception{
		getHistWithRecChart(rec,withTemplate);
	}
	public void showIntervalChart() throws Exception{
		getIntervalChart().createFrame();
	}
	public void showTuneChart() throws Exception{
		getTuneChart().createFrame(this);
	}
	public void showAllignedHistogram(MakamBox rec) throws Exception{
		getAllignedHistogram(rec).createFrame(false);
	}
	public void showAllHistogram(MakamBox rec) throws Exception{
		getAllignedHistogram(rec).createFrame(true);
	}
	public ChartPanel showPitchChart() throws Exception{
		getPitchChart().createFrame();
		return getPitchChart().getPanel();
	}
	public ChartPanel showWaveChart() throws Exception{
		getWaveChart().createFrame();
		return getWaveChart().getPanel();
	}
	public void killObject(){
		audio=null;
		pitch=null;
		histogram=null;
		makam=null;
		synthed=null;

		histoChart=null;
		intervalChart=null;
		waveChart=null;
		pitchChart=null;
		histRecChart=null;
		tuneChart=null;
		pitchShift=null;
	}
}
