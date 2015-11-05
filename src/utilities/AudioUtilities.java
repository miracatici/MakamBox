/*
Copyright 2009 The Open University
http://www.open.ac.uk/lts/projects/audioapplets/
This file is part of the "Open University audio applets" project.
The "Open University audio applets" project is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public
License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.
The "Open University audio applets" project is distributed in the hope that it
will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with the "Open University audio applets" project.
If not, see <http://www.gnu.org/licenses/>.
*/
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

package utilities;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.math3.util.FastMath;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;

/**
 * Static audio utility methods.
 */
public class AudioUtilities {
	
	/**
	 *  Divide array into the chunks that length of chunks are defined by chunkSize
	 * @param array Main array (ex. Pitch Track)
	 * @param chunkSize User defined length of one chunk
	 * @return 2D array 
	 */
	public static float[][] chunkArray(float[] array, int chunkSize) {
        int numOfChunks = (array.length/chunkSize)+1;
        float[][] out = new float[numOfChunks][];
        for(int i = 0; i < numOfChunks; ++i) {
            int start = i * chunkSize;
            int length = Math.min(array.length - start, chunkSize);

            float[] temp = new float[chunkSize];
            System.arraycopy(array, start, temp, 0, length);
            out[i] = temp;
        }
        return out;
    }
	/**
	 * Converts audio data in 'byte' format (little-endian 16-bit) to short array
	 * @param data Data
	 * @param length Number of bytes to actually use
	 * @param reduceStereo If true, reduces stereo data to become mono
	 * @return Short version of data
	 */
	public static short[] byteToShort(byte[] data,int length,boolean reduceStereo)
	{
		if(reduceStereo)
		{
			short[] shortData=new short[length/4];
			for(int i=0;i<shortData.length;i++)
			{
				short val1=(short)( (data[i*4+1]<<8) | (data[i*4]&0xff));
				short val2=(short)( (data[i*4+3]<<8) | (data[i*4+2]&0xff));
				shortData[i]=(short)((val1+val2)/2);
			}
			return shortData;
		}
		else
		{
			short[] shortData=new short[length/2];
			for(int i=0;i<shortData.length;i++)
			{
				shortData[i]=(short)( (data[i*2+1]<<8) | (data[i*2]&0xff));
			}
			return shortData;
		}
	}

	/**
	 * Converts audio data in 'short' format (little-endian) to byte array.
	 * @param data Data buffer
	 * @param length Length of buffer that's used (number of shorts)
	 * @param expandMono If true, expands mono data into stereo
	 * @return Byte array containing translated version
	 */
	public static byte[] shortToByte(short[] data,int length,boolean expandMono)
	{
		byte[] byteData;
		if(expandMono)
		{
			byteData=new byte[length*4];
			for(int i=0;i<length;i++)
			{
				byteData[i*4]=(byte)data[i];
				byteData[i*4+1]=(byte)(data[i]>>8);
				byteData[i*4+2]=(byte)data[i];
				byteData[i*4+3]=(byte)(data[i]>>8);
			}
		}
		else
		{
			byteData=new byte[length*2];
			for(int i=0;i<length;i++)
			{
				byteData[i*2]=(byte)data[i];
				byteData[i*2+1]=(byte)(data[i]>>8);
			}
		}
		return byteData;
	}

	/**
	 * Converts (resamples and mono to/from stereo) audio data.
	 * @param data Input data
	 * @param length Amount of input buffer that is actually used
	 * @param inStereo True if input is stereo
	 * @param outStereo True if output should be stereo
	 * @param inFrequency Frequency of input
	 * @param outFrequency Frequency of output
	 * @return Converted audio data
	 */
	public static byte[] convert(byte[] data, int length,
		boolean inStereo, boolean outStereo, int inFrequency, int outFrequency) {
		
		if(inStereo == outStereo && inFrequency == outFrequency)
		{
			return trimArray(data,length);
		}

		short[] converted=byteToShort(data,length,inStereo && !outStereo);
		converted=resample(converted, converted.length, outStereo,
			inFrequency, outFrequency);
		return shortToByte(converted, converted.length, !inStereo && outStereo);
	}

	/**
	 * Resamples audio data using simple methods.
	 * @param data Input data
	 * @param length Amount of input buffer that is actually used
	 * @param stereo True if input is stereo
	 * @param inFrequency Frequency of input
	 * @param outFrequency Frequency of output
	 * @return Resampled audio data
	 */
	public static short[] resample(short[] data, int length,
		boolean stereo, int inFrequency, int outFrequency)
	{
		if(inFrequency < outFrequency)
		{
			return upsample(data, length, stereo, inFrequency, outFrequency);
		}
		if(inFrequency > outFrequency)
		{
			return downsample(data, length, stereo, inFrequency, outFrequency);
		}
		return trimArray(data,length);
	}

	/**
	 * Basic upsampling algorithm. Uses a linear approximation to fill in the
	 * missing data.
	 * @param data Input data
	 * @param length Amount of input buffer that is actually used
	 * @param stereo True if input is stereo
	 * @param inFrequency Frequency of input
	 * @param outFrequency Frequency of output
	 * @return Upsampled audio data
	 */
	private static short[] upsample(short[] data,int length,
		boolean stereo,int inFrequency,int outFrequency)
	{
		// Special case for no action
		if(inFrequency==outFrequency)
		{
			return trimArray(data, length);
		}

		double scale=(double)inFrequency/(double)outFrequency;
		double pos=0.0;
		short[] output;
		if(!stereo)
		{
			output=new short[(int)(length/scale)];
			for(int i=0;i<output.length;i++)
			{
				int inPos=(int)pos;
				double proportion=pos-inPos;
				if(inPos>=length-1)
				{
					inPos=length-2;
					proportion=1.0;
				}

				output[i]=(short)Math.round(
						data[inPos]*(1.0-proportion)+data[inPos+1]*proportion);
				pos+=scale;
			}
		}
		else
		{
			output=new short[2*(int)((length/2)/scale)];
			for(int i=0;i<output.length/2;i++)
			{
				int inPos=(int)pos;
				double proportion=pos-inPos;

				int inRealPos=inPos*2;
				if(inRealPos>=length-3)
				{
					inRealPos=length-4;
					proportion=1.0;
				}

				output[i*2]=(short)Math.round(
						data[inRealPos]*(1.0-proportion)+data[inRealPos+2]*proportion);
				output[i*2+1]=(short)Math.round(
					data[inRealPos+1]*(1.0-proportion)+data[inRealPos+3]*proportion);
				pos+=scale;
			}
		}

		return output;
	}

	/**
	 * Basic downsampling algorithm. Uses linear approximation to reduce data.
	 * @param data Input data
	 * @param length Amount of input buffer that is actually used
	 * @param stereo True if input is stereo
	 * @param inFrequency Frequency of input
	 * @param outFrequency Frequency of output
	 * @return Downsampled audio data
	 */
	private static short[] downsample(short[] data, int length,
		boolean stereo, int inFrequency,int outFrequency)
	{
		// Special case for no action
		if(inFrequency==outFrequency)
		{
			return trimArray(data, length);
		}

		double scale=(double)outFrequency/(double)inFrequency;
		short[] output;
		double pos=0.0;
		int outPos=0;
		if(!stereo)
		{
			double sum=0.0;
			output=new short[(int)(length*scale)];
			int inPos=0;
			while(outPos<output.length)
			{
				double thisVal=data[inPos++];
				double nextPos=pos+scale;
				if(nextPos>=1.0)
				{
					sum+=thisVal*(1.0-pos);
					output[outPos++]=(short)Math.round(sum);
					nextPos-=1.0;
					sum=nextPos*thisVal;
				}
				else
				{
					sum+=scale*thisVal;
				}
				pos=nextPos;

				if(inPos>=length && outPos<output.length)
				{
					output[outPos++]=(short)Math.round(sum/pos);
				}
			}
		}
		else
		{
			double sum1=0.0,sum2=0.0;
			output=new short[2*(int)((length/2)*scale)];
			int inPos=0;
			while(outPos<output.length)
			{
				double thisVal1=data[inPos++],thisVal2=data[inPos++];
				double nextPos=pos+scale;
				if(nextPos>=1.0)
				{
					sum1+=thisVal1*(1.0-pos);
					sum2+=thisVal2*(1.0-pos);
					output[outPos++]=(short)Math.round(sum1);
					output[outPos++]=(short)Math.round(sum2);
					nextPos-=1.0;
					sum1=nextPos*thisVal1;
					sum2=nextPos*thisVal2;
				}
				else
				{
					sum1+=scale*thisVal1;
					sum2+=scale*thisVal2;
				}
				pos=nextPos;

				if(inPos>=length && outPos<output.length)
				{
					output[outPos++]=(short)Math.round(sum1/pos);
					output[outPos++]=(short)Math.round(sum2/pos);
				}
			}
		}

		return output;
	}

	/**
	 * @param data Data
	 * @param length Length of valid data
	 * @return Array trimmed to length (or same array if it already is)
	 */
	public static short[] trimArray(short[] data, int length)
	{
		if(data.length==length)
		{
			return data;
		}
		else
		{
			short[] output=new short[length];
			System.arraycopy(output,0,data,0,length);
			return output;
		}
	}

	/**
	 * @param data Data
	 * @param length Length of valid data
	 * @return Array trimmed to length (or same array if it already is)
	 */
	public static byte[] trimArray(byte[] data, int length)
	{
		if(data.length==length)
		{
			return data;
		}
		else
		{
			byte[] output=new byte[length];
			System.arraycopy(output,0,data,0,length);
			return output;
		}
	}
	public static int findMedian(int[] array){				// Finding median of given array
		int x=0;
		if(array.length%2==0){
			x = Math.round((array[array.length/2] + array[(array.length/2)-1])/2);
		}
		else{
			x = Math.round(array[(array.length-1)/2]);
		}
		return x;	
	}
	public static byte[] floatToByteArray(float[] in_buff, byte[] out_buff) {
        int ox = 0;
        int len = in_buff.length;
        for (int ix = 0; ix < len; ix++) {
            int x = (int) (in_buff[ix] * 32767.0);
            out_buff[ox++] = (byte) x;
            out_buff[ox++] = (byte) (x >>> 8);
        }
        return out_buff;
    }
	public static float[] byteToFloatArray(byte[] ba) {
		float [] out = new float[ba.length/2];
		int ix = 0;
        int len = (ba.length/2);
        for (int ox = 0; ox < len; ox++) {
            out[ox] =((short) ((ba[ix++] & 0xFF) | 
                       (ba[ix++] << 8))) * (1.0f / 32767.0f);
        }
		return out;
    }
	public static byte[] shortToByteArray(short data) {
		return new byte[]{(byte)(data & 0xff),(byte)((data >>> 8) & 0xff)};
	}
	public static byte[] intToByteArray(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) (i & 0x00FF);
		b[1] = (byte) ((i >> 8) & 0x000000FF);
		b[2] = (byte) ((i >> 16) & 0x000000FF);
		b[3] = (byte) ((i >> 24) & 0x000000FF);
		return b;
	}
	public static int[] hertzToCent(float[] array){
		final float REF_FREQ =8.17579892f*2f;
		int[] centData = new int[array.length];
		for(int i=0;i<array.length;i++){
			centData[i] =(int) Math.round((1200 * FastMath.log(2, array[i] / REF_FREQ)));
		}
		return centData;	
	}
	public static int hertzToCent(float hertz){
		final float REF_FREQ =8.17579892f*2f;
		int centData =(int) Math.round((1200 * FastMath.log(2, hertz / REF_FREQ)));
		return centData;
	}
	public static void normalizeMax(float[] ar){
		float maximum = 0f;
		float[] temp = new float[ar.length];
		temp = ar.clone();
		Arrays.sort(temp);
		maximum = temp[temp.length-1];
		for (int i = 0; i < ar.length; i++) {
			ar[i] = ar[i]/maximum;
		}
	}
	public static void normalizeSum(float[] ar){
		float sum = 0f;
		for (int i = 0; i < ar.length; i++) {
			sum+=ar[i];
		}
		for (int i = 0; i < ar.length; i++) {
			ar[i] = ar[i]/sum;
		}
	}
	public static float centToHertz(float absoluteCent) {
		float freq = 8.17579892f*2f;
		return (float)(freq * Math.pow(2, absoluteCent / 1200.0));
	}
	public static float[] centToHertz(int[] centArray){
		float[] freqArray = new float[centArray.length];
		for (int i = 0; i < centArray.length; i++) {
			freqArray[i] = centToHertz(centArray[i]);
		}
		return freqArray;
	}
	public static float[] smootize(float[] ar,int range){
		float[] temp = new float[ar.length];
		for (int i = range; i < ar.length-range; i++) {
			temp[i] = 0;
			for (int j = -1*range ; j < range+1; j++) {
				temp[i]+=ar[i+j];
			}
			temp[i]/=2*range+1;
		}
		for(int i=0;i<range;i++){
			temp[i] = ar[i];
			temp[temp.length-1-i] = ar[ar.length-1-i];
		}
		return temp;
	}
	public static File convertMP3toWAV(String path){
		File mp3 = new File(path);
		return convertMP3toWAV(mp3);
	}
	public static File convertMP3toWAV(File mp3){
		File temp=null;
		try {
			temp = File.createTempFile(mp3.getName().split("\\.")[0],".wav");
			temp.deleteOnExit();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Converter converter = new Converter();
		try {
			converter.convert(mp3.getAbsolutePath(), temp.getAbsolutePath());
		} catch (JavaLayerException e) {
			e.printStackTrace();
		}
		return temp;
	}
}