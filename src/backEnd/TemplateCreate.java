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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.util.FastMath;

import utilities.Plot;
import applied.HistogramExtractor;
import datas.Makam;

public class TemplateCreate {
	private int maxFiles;
	private File mainDirectory, theoryFile;
	private String[] fileExtension;
	private BufferedReader buffReader;
	private Thread eThread;
	private float histogramDifference=1;
	private float[] referenceHistogram = new float[3272], 
					songHistogram = new float[636],
					distanceArray = new float[3272-636+1],
					makamAverageHistogram = new float[3272];
	private File entryCollection;
	private Iterator<File> iterateCollection;
	private Collection<File> filesCollection;
	
	private Map.Entry<File,float[]> entrySong;
	private Iterator<Entry<File,float[]>> iterateSong;
	private TreeMap<File,float[]> listedSong = new TreeMap<File,float[]>();
	
	private Map.Entry<String,Makam> entryMakam;
	private Iterator<Entry<String, Makam>> iterateMakam;
	private TreeMap<String,Makam> makamsMap = new TreeMap<String,Makam>();
	
	/***************Constructors******************************/
	public TemplateCreate(String folderPath, String dataPath){
		this(new File(folderPath),new File(dataPath));
	}
	public TemplateCreate(File directory, File data){
		mainDirectory = directory;
		theoryFile = data;
		fileExtension = new String[]{"wav","mp3"};
	}
	
	/************************************ Methods ******************************************************/
	public void createTemplates(){
		eThread = new Thread(new Runnable(){
			@Override
			public void run() {				
				listFiles();
				if(isConfirmed()){
					HistogramExtractor.lblProgress.setText(0 + "/" + maxFiles);
					readTheory();
					searchFilesName();
					createTheoryTemplate();
					createSongHistogram();
					allignFileHistogram();
					HistogramExtractor.lblProgress.setText("Done !!! :) ");
				}
			}
		});
		eThread.start();
	}
	private void listFiles(){
		filesCollection = FileUtils.listFiles(mainDirectory, fileExtension, true);
		iterateCollection = filesCollection.iterator();
		while(iterateCollection.hasNext()) {
			entryCollection = iterateCollection.next();
			listedSong.put(entryCollection, null);
		}
		maxFiles = filesCollection.size();
		HistogramExtractor.progressBar.setMaximum(maxFiles);
	}
	private void readTheory(){
		try {
			String line;
			buffReader = new BufferedReader (new FileReader(theoryFile));
			while((line = buffReader.readLine())!=null){
				String[] temp = line.split("\\t");
				float[] interval = new float[temp.length-1];
				for (int i = 1; i < temp.length; i++) {
					 interval[i-1] = Float.valueOf(temp[i]); 
				}
				makamsMap.put(temp[0], new Makam(temp[0],interval));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void searchFilesName(){
		iterateSong = listedSong.entrySet().iterator();
		while(iterateSong.hasNext()) {
			entrySong = iterateSong.next();
			String nameFile = entrySong.getKey().getName();
			nameFile = nameFile.toLowerCase();
			iterateMakam = makamsMap.entrySet().iterator();
			while(iterateMakam.hasNext()) {
				entryMakam = iterateMakam.next();
				String nameMakam = entryMakam.getValue().getName();
				nameMakam = nameMakam.toLowerCase();
				if(nameFile.contains("_"+nameMakam)){
					entryMakam.getValue().songList.add(entrySong.getKey());
				}
			}
		}
	}
	private void createTheoryTemplate(){
		iterateMakam = makamsMap.entrySet().iterator();
		while(iterateMakam.hasNext()) {
			entryMakam = iterateMakam.next();
			if(entryMakam.getValue().songList.size()>0){
				entryMakam.getValue().distribute();
				Plot.plot(entryMakam.getValue().getHistogramData());
			}
		}
	}
	private void createSongHistogram(){
		iterateSong = listedSong.entrySet().iterator();
		int currFile = 0;
		while(iterateSong.hasNext()) {
			try {
				entrySong = iterateSong.next();
				MakamBox box = new MakamBox(entrySong.getKey(),null);
				listedSong.put(entrySong.getKey(),box.getHistogramData());
				Plot.plot(box.getHistogramData());
				System.gc(); 
				++currFile;
				HistogramExtractor.progressBar.setValue(currFile);
				HistogramExtractor.lblProgress.setText(currFile + "/" + maxFiles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void allignFileHistogram(){
		HistogramExtractor.lblProgress.setText("Alligning Histograms");
		iterateMakam = makamsMap.entrySet().iterator();
		while(iterateMakam.hasNext()) {
			referenceHistogram = new float[3272];
			songHistogram = new float[636];
			distanceArray = new float[3272-636+1];
			entryMakam = iterateMakam.next();
			if(entryMakam.getValue().songList.size()>0){
				int count = 0;
				while(histogramDifference>0.1){
					makamAverageHistogram = new float[3272];
					referenceHistogram = entryMakam.getValue().getHistogramData();
					for (int i = 0; i < entryMakam.getValue().songList.size(); i++) {
						songHistogram = listedSong.get(entryMakam.getValue().songList.get(i));
						allign(songHistogram,referenceHistogram);					
					}
					normalize(makamAverageHistogram,entryMakam.getValue().songList.size());
					histogramDifference = L1distance(referenceHistogram,makamAverageHistogram);
					entryMakam.getValue().setHistogramData(makamAverageHistogram);
					count++;
				}
				System.out.println(count);
				histogramDifference = 1f;
				Plot.plot(makamAverageHistogram);
				HistogramExtractor.lblProgress.setText("Saving Templates");
				saveTemplate(entryMakam);
			}
		}
	}
	private void allign(float[] a, float[] b){
		float[] c = a.clone();
		createDistanceArray(a,b);
		int shiftAmount = sortAndPick(distanceArray);
		int indis = 1636 - shiftAmount;
		indis = reCompute(c,indis,5);
		shiftAmount = 1636 - indis;
		sumHistograms(a,shiftAmount);
	}
	private void createDistanceArray(float[] song, float[] ref){
		for (int pos = 0; pos <3272-636+1; pos++) {
			float[] temp = reFill(song,pos);
			distanceArray[pos] = L1distance(temp,ref);
		}
	}
	private float[] reFill(float[] song , int t){
		float[] longHistogram = new float[3272];
		for(int i = 0;i<song.length;i++){
			longHistogram[t+i] = song[i];
		}
		return longHistogram;
	}
	private float L1distance(float[] p1, float[] p2) {
        float sum = 0f;
        for (int i = 0; i < p1.length; i++) {
            sum += FastMath.abs(p1[i] - p2[i]);
        }
        return sum;
    }
	private int sortAndPick(float[] distArray){
		List<Float> b = Arrays.asList(ArrayUtils.toObject(distArray));
		int tonicPoint = ArrayUtils.indexOf(distArray, Collections.min(b));
		return tonicPoint;
	}
	private void sumHistograms(float[] hist, int point){
		float[] longHisto = reFill(hist, point);
		for (int i = 0; i < longHisto.length; i++) {
			makamAverageHistogram[i]+=longHisto[i];
		}
	}
	private int reCompute(float[] arr,int tonInd,int offset){
		float[] sub = ArrayUtils.subarray(arr, tonInd - offset, tonInd + offset);
		int add=-1; float max=-1;
		for (int i = 0; i < sub.length; i++) {
			if(sub[i]>max){
				max=sub[i];
				add=i;
			}
		}
		int yeni = (tonInd+add-offset);
		return yeni;
	}
	private void normalize(float[] array, float k){
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i]/k;
		}
	}
	private void saveTemplate(Entry<String, Makam> ent){
		try {
			new File(mainDirectory.getAbsolutePath()+"/Templates/").mkdir();
			BufferedWriter wr = new BufferedWriter(new FileWriter(mainDirectory.getAbsolutePath()
								+"/Templates/"+ent.getValue().getName()+".txt"));
			for (int i = 0; i < makamAverageHistogram.length; i++) {
				wr.write(new DecimalFormat("0.0000000000000000").format((makamAverageHistogram[i])));
				wr.write("\n");
			}
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean isConfirmed(){
		int reply = JOptionPane.showConfirmDialog(null, String.valueOf(maxFiles)+
				" files found. Extraction might takes long time. Are you sure?", "Confirmation"
				, JOptionPane.YES_NO_OPTION);
		if (reply ==JOptionPane.YES_OPTION){
			return true;			
		} else {
			return false;
		}
	}
	@Override
	public void finalize(){
		System.out.println("object is garbage collected");
	}  
}

