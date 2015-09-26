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
 * Wave file class for read and get details of audio files
 * It can be used with 'path', 'music file', 'float array' or 'byte array'
 * 
 * Created @author mirac
 * Bahcesehir University, 2014
 */

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

import org.apache.commons.io.IOUtils;

import utilities.AudioUtilities;

public class Wavefile {
	private float sampleRate,frameLength,frameSize,frameRate;	// As named, details of audio
	private float lengthInSecond,lengthInSample;				// As named, details of audio	
	private int channel,bitSize;								// bitSize is Bit Depth
	private float[] floatData;									// Float values of audio data
	private byte[] rawData;										// Byte values of audio data
	private File af;											// Wave file read from file
	private String name;										// Name of file
	private Clip clip;											// Java Clip object for playing and reading 
	private AudioFormat format;									// Format of wave file
	private AudioInputStream stream;							// Stream for reading values
	private DataLine.Info info;									// System DataLine info information
	
	public Wavefile(String path) throws Exception{				// Create object with given path
		this(new File(path));
	}
	public Wavefile (File musicFile) throws Exception{			// Create object with given wave file
		if(musicFile.getName().endsWith(".mp3")){
			musicFile = AudioUtilities.convertMP3toWAV(musicFile);
		}
		setClip(musicFile);										// Creating java clip 
		setDetails(musicFile.getName());						// Setting details about clip (wave file)
		
	}
	public Wavefile(AudioFormat form,float[] fData, String nam) throws Exception{	// Create object float data and parameter
		rawData = new byte[fData.length*2];											// Define byte array
		AudioUtilities.floatToByteArray(fData,rawData);										// Converting float array to byte array
		setClip(form,rawData);														// Creating java clip	
		setDetails(nam);															// Setting details about clip (wave file)
	}
	public Wavefile(AudioFormat form,byte[] bData, String nam) throws Exception{	// Create object byte data parameter
		setClip(form,bData);														// Create java clip
		setDetails(nam);															// Setting details about clip (wave file)
	}
	private void setClip(File music){										// Create clip from file
		try {
			System.gc();
			af = music;														// af is wave file from disk			
			stream = AudioSystem.getAudioInputStream(music); 				// Creating a input stream
			format = stream.getFormat();
			rawData = new byte[(int)stream.getFrameLength()*2*format.getChannels()];				// Define byte array size
			IOUtils.read(AudioSystem.getAudioInputStream(af), rawData);		// Read all data from clip to array
			info = new DataLine.Info(Clip.class, format);
	        clip = (Clip)AudioSystem.getLine(info);							// Creating clip 			
	        clip.open(stream);												// Opening clip with stream
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setClip(AudioFormat form,byte[] buff){		// Create clip from byte array
		try {
			System.gc();
			rawData = buff;									// Define byte array 
			format = form;
			info = new DataLine.Info(Clip.class, format);
	        clip = (Clip)AudioSystem.getLine(info);							// Creating clip 			
	        clip.open(form, rawData, 0, rawData.length);	// Create clip with byte array
			System.gc();
		}  catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	private void setDetails(String nam) throws IOException{	// Setting and getting details about wave file
		System.gc();
		frameLength = clip.getFrameLength();				// Get clip length in frame type
		sampleRate = format.getSampleRate();				// Get sample rate kHz of clip
		frameRate = format.getFrameRate();					// Usually same with sample rate, it's frame rate
		frameSize = format.getFrameSize();					// Get size of one frame (for 16 bit 2 byte/frame)
		channel = format.getChannels();						// Channel number
		bitSize = format.getSampleSizeInBits();				// Bit depth of audio
		lengthInSecond = frameLength/frameRate;				// Clip length in second 
		lengthInSample = frameLength*frameSize;				// Clip length in sample (for 16 bit)
		name = nam;											// Name of file
		if(channel==2){
			rawData = AudioUtilities.convert(rawData, rawData.length, true, false, (int)sampleRate, (int)sampleRate);			
		}
		floatData = AudioUtilities.byteToFloatArray(rawData);			// Getting float data with conversion
		System.gc();
	}
	public AudioFormat getFormat() {
		return format;
	}
	public float getSampleRate() {
		return sampleRate;
	}
	public float getFrameLength() {
		return frameLength;
	}
	public float getFrameSize() {
		return frameSize;
	}
	public float getFrameRate() {
		return frameRate;
	}
	public int getChannel() {
		return channel;
	}
	public int getBitRate() {
		return bitSize;
	}
	public float getLengthInSecond(){
		return lengthInSecond;		
	}
	public float getLengthInSample(){
		return lengthInSample;
	}
	public boolean isActive(){
		return clip.isRunning();
	}
	public void start(){
		clip.start();		   		
	}
	public void stop(){
		clip.stop();
	}
	public Clip getClip() {
		return clip;
	}
	public AudioInputStream getStream() {
		return stream;
	}
	public float[] getFloatData() {
		return floatData;
	}
	public byte[] getRawData() {
		return rawData;
	}
	public String getName() {
		return name;
	}
	public String getPath() {
		if(af==null){
			return "/Users/mirac/Documents/workspace/MakamToolBox/";
		} else{
			return af.getAbsoluteFile().getParent();
		}
	}
	public String getFolder(){
		if(af==null){
			return "/Users/mirac/Documents/workspace/MakamToolBox/";
		} else{
			return af.getAbsoluteFile().getParent();
		}
	}
}
