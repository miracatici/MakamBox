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
package datas;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is used for serializing makam data to a *.ser file which is used for makam classifying for the audio file
 */


public class Makam implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5399959002925310220L;
	
	private String name;
	private String tonicNote;
	private String histogramPath;
	private float[] histogramData,intervals,intervalArray;
	public ArrayList<File> songList;
	
	public Makam(String n, float[] i){
		this(n,null,null,new float[3272],i);
		songList = new ArrayList<File>();
		intervalArray = new float[intervals.length+1];
		float commaCent = 1200f/53f;
		for (int t = 0; t < intervals.length; t++) {
			intervalArray[t+1] = intervals[t]*commaCent;
		}	
	}
	
	public Makam(String n, String t, String p,float[] h, float[] i){
		name = n;
		tonicNote = t;
		histogramData = h;
		histogramPath = p;
		intervals = i;
		float commaCent = 1200f/53f;
		if(intervals!=null){
			intervalArray = new float[intervals.length+1];
			for (int c = 0; c < intervals.length; c++) {
				intervalArray[c+1] = intervals[c]*commaCent;
			}	
		}
	}
	
	public void distribute(){
		for (int j = -8; j < 9 ; j++) {
			histogramData[1636 + j] = (8-Math.abs(j))/(float)8; 
		}
		for (int i = 0; i < intervals.length ; i++) {
			for (int j = -8; j < 9 ; j++) {
				histogramData[1636 + Math.round(3*intervals[i]) + j] = (8-Math.abs(j))/(float)8; 
			}
		}
	}
	
	public String toString(){
		String info = null;
		info = name+"\t"+tonicNote+"\t"+Arrays.toString(intervalArray);
		return info;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTonicNote() {
		return tonicNote;
	}
	public void setTonicNote(String tonicNote) {
		this.tonicNote = tonicNote;
	}
	public String getHistogramPath() {
		return histogramPath;
	}
	public void setHistogramPath(String histogramPath) {
		this.histogramPath = histogramPath;
	}
	public float[] getHistogramData() {
		return histogramData;
	}
	public void setHistogramData(float[] histogramData) {
		this.histogramData = histogramData;
	}
	public float[] getIntervals() {
		return intervals;
	}
	public void setIntervals(float[] intervals) {
		this.intervals = intervals;
	}

	public float[] getIntervalArray() {
		return intervalArray;
	}

	public void setIntervalArray(float[] intervalArray) {
		this.intervalArray = intervalArray;
	}
}
