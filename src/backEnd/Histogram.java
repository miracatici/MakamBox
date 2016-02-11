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
 * Histogram class can be used with Pitch Detection object, YIN output or other pitch result from text file.
 * Histogram resolution can be set by comma value (Default is 1/3 Holderian comma)
 * 
 * Created @author mirac
 * Bahcesehir University, 2014
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

import be.tarsos.dsp.beatroot.Peaks;
import utilities.AudioUtilities;

public class Histogram{
	
	private PitchDetection pitch;
	private Scanner scanner;
	private String name;
	private int minCentF0,maxCentF0,medianCent;
	private int[] sortedCentF0;
	private float[] f0track,sortedf0,histData, f0YIN;
	private float commaCent;
	private int[] peaks;
	
	public Histogram(){												// Constructor to access other methods without creating histogram
																	// Maybe it will be required :)
	}
	public Histogram(PitchDetection pii) throws IOException{		// Constructor to create object with Pitch Detection 
		dataSetting(pii);											// Setting all data to use (for Pitch Detec.)
		AudioUtilities.normalizeMax(histData);								// Normalize to max=1
		AudioUtilities.normalizeSum(histData);								// Normalize to area=1
		histData = AudioUtilities.smootize(histData, 3);						// Smootize with element averaging
		calcPeaks(histData);
	}
	public Histogram(File pitchFile) throws Exception{				// Constructor for text file 
		if(pitchFile.getName().endsWith(".yin.txt")){				// It can be YIN output or etc.
			dataSetting3(pitchFile);
		} else{
			dataSetting2(pitchFile);
		}
		AudioUtilities.normalizeMax(histData);								// Same process 
		AudioUtilities.normalizeSum(histData);
		histData = AudioUtilities.smootize(histData, 3);
	}
	private void dataSetting(PitchDetection pii) throws IOException{// Data setting for pitch detection
		pitch = pii;												// Datas will be used for calculate the histogram
		name  = pitch.getName();
		f0track = kickZeros(pitch.getPitchResult());				// Kick zeros in the pitch array, be simple
		sortedf0 = f0track.clone();									// Clone for sorting to ascending order
		Arrays.sort(sortedf0);
		sortedCentF0 = AudioUtilities.hertzToCent(sortedf0);					// Conversion from hertz to cent, linear domain
		medianCent = AudioUtilities.findMedian(sortedCentF0);						// Median of sorted cent array
		minCentF0 = medianCent - 2400;								// Minimum value is 2 octave lower,avoid extreme low					
		maxCentF0 = medianCent + 2400;								// Maximum value is 2 octave upper,avoid extreme high
		commaCent = (float) (1200.0/159.0);							// Comma resolution (1/3 holderian comma)
		histData = new float[Math.round((maxCentF0-minCentF0)/commaCent)];	// Histogram data array will be 4 octave 
		createHistogram(sortedCentF0);								// Calculation histogram array
	}
	private void dataSetting2(File file) throws Exception{	// For other pitch track results
		ArrayList<Float> temp = new ArrayList<Float>();		// Reading pitch values from text file  (different from YIN files)
		scanner = new Scanner (new FileReader(file));
		while(scanner.hasNext()){
			temp.add(Float.valueOf(scanner.nextLine()));
		}
		float[] temp2 = new float[temp.size()];				// Converting Arraylist to Array
		for(int i=0;i<temp.size();i++){
			temp2[i] = temp.get(i);
		}		
		name = file.getName();								// Same process with dataSetting()					
		f0track = kickZeros(temp2);							// Except temp2 array which is created from ArrayList
		sortedf0 = f0track.clone();
		Arrays.sort(sortedf0);
		sortedCentF0 = AudioUtilities.hertzToCent(sortedf0);
		medianCent = AudioUtilities.findMedian(sortedCentF0);
		minCentF0 = medianCent - 2400;
		maxCentF0 = medianCent + 2400;
		commaCent = (float) (1200.0/159.0); 
		histData = new float[Math.round((maxCentF0-minCentF0)/commaCent)];
		createHistogram(sortedCentF0);
	}
	private void dataSetting3(File file) throws Exception{	// For YIN Files
		ArrayList<Float> temp = new ArrayList<Float>();		// Reading pitch values from text file
		scanner = new Scanner (new FileReader(file));		
		while(scanner.hasNext()){
			temp.add(Float.valueOf(scanner.nextLine().split("\\b")[2]));
		}
		float[] temp2 = new float[temp.size()];
		for(int i=0;i<temp.size();i++){
			temp2[i] = temp.get(i);
		}
		f0YIN = temp2.clone();
		name = file.getName();								// Same process with dataSetting()
		f0track = kickZeros(temp2);
		sortedf0 = f0track.clone();
		Arrays.sort(sortedf0);
		sortedCentF0 = new int[sortedf0.length];			//f0track is based on cent values so don't need to convert 
		for (int i = 0; i < sortedf0.length; i++) {
			sortedCentF0[i] = (int) sortedf0[i];
		}
		medianCent = AudioUtilities.findMedian(sortedCentF0);				// Same process with dataSetting()
		minCentF0 = medianCent - 2400;
		maxCentF0 = medianCent + 2400;
		commaCent = (float) (1200.0/159.0);
		histData = new float[Math.round((maxCentF0-minCentF0)/commaCent)];
		createHistogram(sortedCentF0);
	}
	private void createHistogram(int[] array) throws IOException{
		for(int i=0;i<array.length;i++){
			if(array[i]>=minCentF0 && array[i]<maxCentF0){
				++histData[(int) ((array[i]-minCentF0)/commaCent)];
			}
		}
	}
	private float[] kickZeros(float [] arr){				// Kick zeros from pitch array
		ArrayList<Float> temp = new ArrayList<Float>();
		for(int i=0;i<arr.length;i++){
			if(arr[i]>0){
				temp.add(arr[i]);
			}
		}
		float[] out = new float[temp.size()];
		for(int k=0;k<temp.size();k++){
			out[k] = temp.get(k);
		}
		return out;
	}
	public float[] getHistogram() {
		return histData;
	}
	public String getName() {
		return name;
	}
	public int getLength(){
		return getHistogram().length;
	}
	public int getMinimum() {
		return minCentF0;
	}
	public int getMaximum(){
		return maxCentF0;
	}
	public float[] getPitchTrack(){
		return f0YIN;
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
			peakCent[i] = ((peaks[i])*commaCent+minCentF0);
		}
		return peakCent;
	}
	public float[] getPeaksCent(int count){													// It returns the cent values of peak points
		float[] peakCent = new float[count];
		for (int i = 0; i < count; i++) {
			peakCent[i] = ((peaks[i])*commaCent+minCentF0);
		}
		return peakCent;
	}
}
