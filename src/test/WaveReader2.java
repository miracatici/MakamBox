package test;
/*
 *	SimpleAudioPlayer.java
 *	This file modified version of a file from jsresources.org
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
 * [Modified by Baris Bozkurt to use only in educational purposes]
 */


import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import utilities.AudioUtilities;

public class WaveReader2  {
	private AudioInputStream audioInputStream = null;
	private AudioFormat audioFormat;
	private float sampleRate;
	private int[] audioData;
	private byte[] abData;
	private float[] floatData;
	
	public WaveReader2(){
		
	}
	public void changeWave(File ss){
		compute(ss);
	}
	private void compute(File soundFile){
		System.gc();
		audioInputStream = null;
		try{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		}
		catch (Exception e){
			e.printStackTrace();System.exit(1);
		}

		audioFormat = audioInputStream.getFormat();
		sampleRate = audioFormat.getSampleRate();
		int	nBytesRead = 0;
		abData = new byte[(int)audioInputStream.getFrameLength()*2];
		while (nBytesRead != -1)
		{
			try{
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
		audioInputStream = null;
		//conversion from bytes to integers
		int nlengthInSamples = abData.length / 2;
		audioData = new int[nlengthInSamples];
		if (audioFormat.isBigEndian()) {
			for (int i = 0; i < nlengthInSamples; i++) {
				/* First byte is MSB (high order) */
				int MSB = abData[2*i];
				/* Second byte is LSB (low order) */
				int LSB = abData[2*i+1];
				audioData[i] = MSB << 8 | (255 & LSB);
			}
		} else {
			for (int i = 0; i < nlengthInSamples; i++) {
				/* First byte is LSB (low order) */
				int LSB = abData[2*i];
				/* Second byte is MSB (high order) */
				int MSB = abData[2*i+1];
				audioData[i] = MSB << 8 | (255 & LSB);
			}
		}
		floatData = new float[abData.length/2];
		if(audioFormat.getChannels()>1){
			abData = AudioUtilities.convert(abData, abData.length, true, false, (int)sampleRate,(int)sampleRate);	
		}
		tofloatArray(abData,floatData);
		System.gc();
	}
	private float[] tofloatArray(byte[] in_buff, float[] out_buff) {
        int ix =0;
        int len = (in_buff.length/2);
        for (int ox = 0; ox < len; ox++) {
            out_buff[ox] = ((short) ((in_buff[ix++] & 0xFF) | 
                       (in_buff[ix++] << 8))) * (1.0f / 32767.0f);
        }
        return out_buff;
    }
	public AudioFormat getAudioFormat() {
		return audioFormat;
	}
	public float getSampleRate() {
		return sampleRate;
	}
	public byte[] getByteData() {
		return abData;
	}
	public float[] getFloatData() {
		return floatData;
	}
}



