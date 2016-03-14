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
 * The main f0 Pitch track estimation class. 
 * It can be used with wave file object
 * It uses the YIN implementation of TarsosDSP by Joren Six
 * 
 * Due to implementation of YIN, we need to split float array to chunk with buffer size. 
 * This buffer size is calculated from sample rate (40 msec window size)
 * It also create pitch file folder in project folder and write pitch result array to text file
 * 
 * Created @author mirac
 * Bahcesehir University, 2014
 * 
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javax.sound.sampled.AudioFormat;

import org.apache.commons.lang3.ArrayUtils;

import utilities.AudioUtilities;


public class PitchDetection {
	private AudioFormat format;
	private Wavefile audio;
	private Yin yin;
	private String name;
	private int bufferSize;
	private float sampleRate;
	private float[] floatData,pitchResult;
	ArrayList<Integer> chunkPosition = new ArrayList<Integer>();

	public PitchDetection(Wavefile afs) throws Exception{
		audio = afs ;
		format = audio.getFormat();
		name = audio.getName();
		sampleRate = audio.getSampleRate();
		bufferSize = (int) Math.round(sampleRate*0.01);
		floatData = audio.getFloatData().clone();
		estimate();
	}
	
	public PitchDetection(String name, float[] audioFloatData,float samplerate) {
		audio = null;
		this.name = name;
		sampleRate = samplerate;
		bufferSize = (int) Math.round(sampleRate*0.01);
		floatData = audioFloatData;
		estimate();
	}
	
	public void estimate(){
		float[][] chunked = AudioUtilities.chunkArray(floatData,bufferSize);
		yin = new Yin(sampleRate,bufferSize,0.1);
		pitchResult = new float[chunked.length];
		for (int i=0; i<pitchResult.length;++i){
			float r = yin.getPitch(chunked[i]).getPitch();
			if(r==-1){
				pitchResult[i] = 0;
			}
			else{
				pitchResult[i] = r;
			}
		}
	}
	public PitchDetection(File f) throws Exception{
		this(new Wavefile(f));
	}
	public float[] getPitchResult() {
		return pitchResult;
	}
	public Wavefile getAudio() {
		return audio;
	}
	public int getBufferSize() {
		return bufferSize;
	}
	public String getName() {
		return name;
	}
	public int[] getChunkPosition() {
		int[] chunkPos = new int[chunkPosition.size()];
		for (int i = 0; i < chunkPosition.size(); i++) {
			chunkPos[i] = chunkPosition.get(i);
		}
		return chunkPos;
	}
	public float[][] chunkPitchTrack(float[] pitchTrack){
		float bottom_limit = 0.97f; float upper_limit = 1.033f;
		ArrayList<Float[]> chunkList = new ArrayList<Float[]>();
		ArrayList<Float> tempChunk = new ArrayList<Float>();
		tempChunk.add(pitchTrack[0]);
		for (int i = 1; i < pitchTrack.length; i++) {
			float interval = pitchTrack[i] / pitchTrack[i-1];		
			if (bottom_limit < interval && interval < upper_limit){
				tempChunk.add(pitchTrack[i]);
			} else {
				chunkList.add(tempChunk.toArray(new Float[tempChunk.size()]));
				tempChunk.clear();
				tempChunk.add(pitchTrack[i]);
				chunkPosition.add(i);
			}
		}
		chunkList.add(tempChunk.toArray(new Float[tempChunk.size()]));
		Float[][] chunkedPitch = new Float[chunkList.size()][];
		for (int i = 0; i < chunkList.size(); i++) {
			chunkedPitch[i] = new Float[chunkList.get(i).length];
			for (int j = 0; j < chunkedPitch[i].length; j++) {
				chunkedPitch[i][j] = (float) chunkList.get(i)[j];
			}
		}
		Arrays.sort(chunkedPitch, new Comparator<Float[]>() {
			@Override
            public int compare(final Float[] entry1, final Float[] entry2) {
                final Float time1 = (float) entry1.length;
                final Float time2 = (float) entry2.length;
                return time2.compareTo(time1);
            }
		});
		float[][] chunkedPitchPri = new float[chunkedPitch.length][];
		for (int i = 0; i < chunkedPitchPri.length; i++) {
			chunkedPitchPri[i] = ArrayUtils.toPrimitive(chunkedPitch[i]);
		}
		return chunkedPitchPri; 			
	}
	public float[][] pickLongChunks(float[][] chunkedArray,int chunkNumber){
		float[][] longChunks = new float[chunkNumber][];
		for (int i = 0; i < longChunks.length; i++) {
			longChunks[i] = chunkedArray[i];
		}
		return longChunks;
	}
	public AudioFormat getFormat(){
		return format;
	}
	public float[] getChunkResults(int noteNumber){
		float[][] tempResult = pickLongChunks(chunkPitchTrack(getPitchResult()), noteNumber);
		float[] answerResult = new float[noteNumber];

		for (int i = 0; i < tempResult.length; i++) {
			answerResult[i] = AudioUtilities.findMedian(tempResult[i]);
		}
		Arrays.sort(answerResult);

		return answerResult;
	}
}
