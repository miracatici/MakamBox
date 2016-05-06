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
 * Makam Classifier class based on histogram matching algorithm. L1-Norm (sum of absolute values) 
 * Makam template file is created with an seperate software, HistogramExtractor (Java implementation of MATLAB tool) 
 * These templates contains 3272 (first and last 636 data is zero for matching histogram) data-point.
 * 1636th. point is tonic frequency.
 * 
 * After measuring distance arrays and select minimum one, tonic is measured with this point.
 * Tonic frequency and tonic index stored.
 *
 * Normalizing Max is dividing all elements by the maximum value of occurrence
 * Normalizing Sum is dividing all elements by the sum of all elements (The area of histogram plot is 1)
 * Smootizing is a histogram smoothing via element averaging method.
 * It makes better matching result
 * 
 * Created @author miracatici
 * Bahcesehir University, 2014
 */
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.math3.util.FastMath;

import applied.SelectCulture;
import be.tarsos.dsp.beatroot.Peaks;
import datas.Makam;
import utilities.AudioUtilities;


public class MakamClassifier {
	private int tempLen = 3272,histLen=636,makamNumber;  // Template array and Histogram array length is fixed for 
	private int shiftAmount;							// 4 octave resolution, common register for Makam culture	
	private int minCent;
	private int tonicIndex;
	private int ind;
	private int[] peaks;
	private float tonicHz,tonicCent,commaCent;
	private float[] histo=new float[histLen],longHist;
	private float[][] templates; 
	private float[][] distArray;
	private String[] templatesNam;
	private String makamName,name;
	private Histogram hi;

	public MakamClassifier(Histogram h) throws Exception{ // Constructor to use with Histogram object
		hi = h;
		histo = h.getHistogram();
		name = h.getName();
		minCent = h.getMinimum();
	}
	public MakamClassifier(File f) throws Exception{ // Constructor to use with text file which contains histogram data
		name = f.getName();							 // For example, MakamToolBox outputs.
		createHistFromFile(f);
	}
	private void createHistFromFile(File f) throws Exception{   // Method to read a text file which contains histogram data
		Scanner reader = new Scanner (new FileReader(f.getPath()));
		for (int i = 0; reader.hasNextLine(); i++) {
			histo[i] = Float.parseFloat(reader.nextLine());
		}
		reader.close();
		AudioUtilities.normalizeMax(histo);						// Normalizing processes for better result
		AudioUtilities.normalizeSum(histo);
	}
	public void measure(String makamTempName){					// Histogram matching and tonic identification
		setMakamTemplate(makamTempName);						// and all other processes 
		createDistanceArray();									// FOR ONLY CERTAIN MAKAM (USER DEFINED)
		sortAndPick();
		computeTonic();
		reFill(shiftAmount);
		calcPeaks(getShiftHistogram());
	}
	public void measure() throws Exception{									// Histogram matching and tonic identification
		setMakamTemplates(SelectCulture.getCulture().getMakamsData()); 		// and all other processes
		createDistanceArray();												// for all makam templates in culture setting file
		sortAndPick();
		computeTonic();
		reFill(shiftAmount);
		calcPeaks(getShiftHistogram());
	}
	private void createDistanceArray(){										// Based on L1 Norm (Manhattan, sum of absolute values)
		for (int i = 0; i < makamNumber; i++) {								// Template histogram array is fixed 
			for (int j = 0; j <tempLen-histLen+1; j++) {					// Recording histogram array is sliding on each step
				reFill(j);													// reFill method is sliding method for reference (see below)
				distArray[i][j] = L1distance(templates[i],longHist);		// L1distance method calculates the distance measure between to histograms. 
			}	
		}
	}
	private void reFill(int j){												// We have 636-Point (4 octave, 1/3 holderian comma resolution) histogram
		longHist = new float[tempLen];										// It should be size with template histogram array
		for(int i = 0;i<histo.length;i++){									// So we create long version of histogram array
			longHist[j+i] = histo[i];										// And it slides for each step in  createDistanceArray() method.
		}
	}
	private float L1distance(float[] p1, float[] p2) {						// it can be used for two array in same size				
        float sum = 0f;														// it calculates sum of absolute values in two different array
        for (int i = 0; i < p1.length; i++) {								// 0 is the maximum matching (element by element same array)
            sum += FastMath.abs(p1[i] - p2[i]);								// Lower values are better matching
        }
        return sum;
    }
	private void sortAndPick(){												// All of the output values of L1distance
		float value = 0f; shiftAmount = 0; ind=0;							// We calculate the distance measure array for each position of recording histogram array (2637 sliding point)	
		float[] values = new float[makamNumber];							// Calculation is done for all makam template histogram ( so we have [makam numnber][2637] 2-D array
		int [] indexes = new int[makamNumber];								// Then we're trying to find minimum value
		for (int i = 0; i < distArray.length; i++) {						// It means that best matching point for all templates
			List<Float> b = Arrays.asList(ArrayUtils.toObject(distArray[i]));   // Which distance array has minimum value and where it is used for makam recognition and tonic identification
			value = Collections.min(b);
			values[i] = value;
			indexes[i] = ArrayUtils.indexOf(distArray[i], value);
		}
		List<Float> t = Arrays.asList(ArrayUtils.toObject(values));
		ind = ArrayUtils.indexOf(values, Collections.min(t));
		shiftAmount = indexes[ind];
		makamName = WordUtils.capitalize(templatesNam[ind]);
	}
	private void computeTonic(){											// In template histograms, 1636th. point is tonic point
		commaCent = 1200.0f/159.0f;											// It measures the distance between this point and best matching point (output of the sortAndPick)
		tonicIndex = 1636-shiftAmount;										// The difference between them is tonic difference
		reCompute(tonicIndex,6);											// Sometimes, it doesn't match on histogram peak, so we try to find nearest histogram peak in given offset
		shiftAmount = 1635 - tonicIndex;									// Tonic cent value is calculated via sum of minimum value of histogram and tonic difference value.
		tonicCent = minCent + tonicIndex*commaCent;
		tonicHz = AudioUtilities.centToHertz(tonicCent);					// Conversion from cent to hertz
	}
	private void reCompute(int tonInd,int offset){										// Finding maximum value point for given offset
		float[] sub = ArrayUtils.subarray(histo, tonInd - offset, tonInd + offset);
		int add=-1; float max=-1;
		for (int i = 0; i < sub.length; i++) {
			if(sub[i]>max){
				max=sub[i];
				add=i;
			}
		}
		tonicIndex = (tonInd+add-offset);
	}
	private void setMakamTemplate(String templateName){									// User defined makam template histogram reading
		makamNumber = 1;																// It reads the template histogram given by user from culture setting file
		templates = new float[makamNumber][tempLen];
		distArray = new float[makamNumber][tempLen - histLen + 1];
		templatesNam = new String[makamNumber];
		templates[0] = SelectCulture.getCulture().getMakamsData().get(templateName).getHistogramData();
		templatesNam[0] = templateName;
		AudioUtilities.normalizeMax(templates[0]);
		AudioUtilities.normalizeSum(templates[0]);
		templates[0] = AudioUtilities.smootize(templates[0], 3);
	}
	private void setMakamTemplates(TreeMap<String,Makam> makams){   		// It reads all the template histograms from culture setting file
		// Get a set of the entries
		Set<Map.Entry<String,Makam>> set = makams.entrySet();
		makamNumber = set.size();
		templates = new float[makamNumber][tempLen];
		distArray = new float[makamNumber][tempLen-histLen +1];
		templatesNam = new String[makamNumber];
		// Get an iterator
		Iterator<Entry<String, Makam>> it = set.iterator();
		// Display elements
		for(int p=0;it.hasNext();p++) {
			Map.Entry<String,Makam> me = it.next();
			float [] temp =  me.getValue().getHistogramData();
			templates[p] = temp;
			templatesNam[p] = me.getValue().getName();
			AudioUtilities.normalizeMax(templates[p]);
			AudioUtilities.normalizeSum(templates[p]);
			templates[p] = AudioUtilities.smootize(templates[p], 3);
		}	
	}
	public void calcPeaks(float[] data){													// Very very very basic histogram peaks calculation method
		float [] tempAr = data.clone();
		double [] tempArD = new double[data.length];
		for (int i = 0; i < tempArD.length; i++) {
			tempArD[i] = (double) tempAr[i];
		}
		List<Float> b = Arrays.asList(ArrayUtils.toObject(tempAr));							// If the value of a point is greater than the right and left ones, it is a peak point :)
		float maxV = (Collections.max(b)/15);
		Object [] tempObj = Peaks.findPeaks(tempArD, 6, maxV).toArray();
		peaks = new int[tempObj.length];
		for (int i = 0; i < tempObj.length; i++) {
			peaks[i] = (int) tempObj[i];
		}
	}
	public int[] getPeaks(){
		return peaks;
	}
	public float[] getPeaksCent(){													// It returns the cent values of peak points
		float[] peakCent = new float[peaks.length];
		for (int i = 0; i < peaks.length; i++) {
			peakCent[i] = ((peaks[i] - shiftAmount)*commaCent+minCent)-tonicCent;
		}
		return peakCent;
	}
	public float[] getPeaksComma(){													// It returns the comma values of peak points
		float[] peakComma = new float[peaks.length];
		for (int i = 0; i < peaks.length; i++) {
			peakComma[i] = (((peaks[i] - shiftAmount)*commaCent+minCent)-tonicCent)/(1200f/53f);
		}
		return peakComma;
	}
	public String getMakamName() {
		return makamName;
	}
	public String getName() {
		return name;
	}
	public float getTonicHz() {
		return tonicHz;
	}
	public float getTonicCent(){
		return tonicCent;
	}
	public float[] getMakamTemplate(){
		return templates[ind];
	}
	public float[] getShiftHistogram(){
		reFill(shiftAmount);
		return longHist;
	}
	public float getTonicIndex(){
		return ind;
	}
	public int getShiftAmount(){
		return shiftAmount;
	}
	public void setTonicHz(float hz){
		tonicHz = hz;
	}
	public void setTonicCent(float cent){
		tonicCent = cent;
	}
	public void setShiftTonic(float cent){
		tonicCent += cent;
		tonicHz = AudioUtilities.centToHertz(tonicCent);
	}
	public int getTonicInPeaks(){
		float[] peakC = hi.getPeaksCent();
		float[] dist = new float[peakC.length];
		for (int i = 0; i < dist.length; i++) {
			dist[i] = Math.abs(peakC[i] - tonicCent);
		}
		List<Float> k = Arrays.asList(ArrayUtils.toObject(dist));
		return ArrayUtils.indexOf(dist, Collections.min(k));
	}
	public float[][] getDistanceArray(){
		return distArray;
	}
}

