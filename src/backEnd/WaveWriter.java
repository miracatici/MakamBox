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
 *	Simple Wavefile Writer Class
 *	This file is combined from various source.
 * 	Modified by Bilge Mirac Atici to use only in educational purposes.
 *
 * 
 * 
 * WaveFileWriter(float[] array,long sampleRate, int bitSize,int channel, String name)
 * 			
 * 					-> Based on writing byte array to a raw wav file and make a header
 * 			 		-> Constructor write a .wav file with given parametres.
 *					-> Be sure that your workspace directory is readable and writable!!!
 */

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import utilities.AudioUtilities;


public class WaveWriter {
	private byte[] byteData;
	private long mySubChunk1Size;
	private int myBitsPerSample;
	private int myFormat;
	private int nlengthInSamples;
	private long myChannels;
	private long mySampleRate;
	private long myByteRate;
	private int myBlockAlign;
	private long myDataSize;
	private long myChunk2Size;
	private long myChunkSize;
	private OutputStream os;
	private BufferedOutputStream bos;
	private DataOutputStream outFile;

	
	public WaveWriter(MakamBox box) throws IOException{
		this(box.getWavefile());
	}
	public WaveWriter(Wavefile af) throws IOException{
		 this(af.getFloatData(),(long)af.getSampleRate(),af.getBitRate(),af.getChannel(),af.getName());
	}

	public WaveWriter(float[] dataArray,long sampleRate, int bitSize,int channel, String name) throws IOException {
		System.out.println("Parameters are taken");
		nlengthInSamples = (dataArray.length*2);
		byteData = new byte[nlengthInSamples];
			
		AudioUtilities.floatToByteArray(dataArray,byteData);
		System.out.println("Float array was converted to Byte array");
		mySubChunk1Size = 16;
		myBitsPerSample= bitSize;
		myFormat = 1;
		myChannels = channel;
		mySampleRate = sampleRate;
		myByteRate = mySampleRate * myChannels * myBitsPerSample/8;
		myBlockAlign = (int) (myChannels * myBitsPerSample/8);

		myDataSize = byteData.length;
		myChunk2Size =  myDataSize * myChannels * myBitsPerSample/8;
		myChunkSize = 36 + myChunk2Size;
			
		os = new FileOutputStream(new File(name+".wav"));
		bos = new BufferedOutputStream(os);
		outFile = new DataOutputStream(bos);
	
		outFile.writeBytes("RIFF");                                 				// 00 - RIFF
		outFile.write(AudioUtilities.intToByteArray((int)myChunkSize), 0, 4);      	// 04 - how big is the rest of this file?
		outFile.writeBytes("WAVE");                                 				// 08 - WAVE
		outFile.writeBytes("fmt ");                                 				// 12 - fmt 
	  	outFile.write(AudioUtilities.intToByteArray((int)mySubChunk1Size), 0, 4);  	// 16 - size of this chunk
	   	outFile.write(AudioUtilities.shortToByteArray((short)myFormat), 0, 2);    	// 20 - what is the audio format? 1 for PCM = Pulse Code Modulation
	   	outFile.write(AudioUtilities.shortToByteArray((short)myChannels), 0, 2);   	// 22 - mono or stereo? 1 or 2?  (or 5 or ???)
	   	outFile.write(AudioUtilities.intToByteArray((int)mySampleRate), 0, 4);     	// 24 - samples per second (numbers per second)
	   	outFile.write(AudioUtilities.intToByteArray((int)myByteRate), 0, 4);      	// 28 - bytes per second
	   	outFile.write(AudioUtilities.shortToByteArray((short)myBlockAlign), 0, 2);    	// 32 - # of bytes in one sample, for all channels
	   	outFile.write(AudioUtilities.shortToByteArray((short)myBitsPerSample), 0, 2);  	// 34 - how many bits in a sample(number)?  usually 16 or 24
	   	outFile.writeBytes("data");                                 					// 36 - data
	   	outFile.write(AudioUtilities.intToByteArray((int)myDataSize), 0, 4);     	  	// 40 - how big is this data chunk
	   	outFile.write(byteData);                                   						// 44 - the actual data itself - just a long string of numbers
	   	outFile.flush();
	   	outFile.close();
	   	System.out.println("Wave file was written");
	}
	public  byte[] getByteData() {
		return byteData;
	}	
}	
