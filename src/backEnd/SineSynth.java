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
 * It's a sine wave synthesizer. Works with given pitch array to synthesize a sine wave represent of audio signal.
 * Also has a synthSine() method for synthesize a 1 second sine wave with given frequency. (Used for sounding histogram chart)
 * It can returns a MakamBox object to use in everywhere
 * 
 * Created @author mirac
 * Bahcesehir University, 2014
 */

import javax.sound.sampled.AudioFormat;

import utilities.Write;


public class SineSynth {
	private AudioFormat af;
	private int start;
	private float phase,fSample,gain = 0.3f,twoPiF,time,frameLenSamp;
	private float[] output,frame,timeAr,fadeIn,fadeOut,f0track;
	private Wavefile synthedWave,sineWave;
	private String name;
	
	public SineSynth(PitchDetection pitch){
		name = pitch.getName();
		phase = 0f;
		af = new AudioFormat(44100,16,1,true,false);
		f0track = pitch.getPitchResult();
		fSample = pitch.getAudio().getSampleRate();
		frameLenSamp =pitch.getBufferSize();
		timeAr = new float[(int)frameLenSamp];
		fadeIn = new float[(int)frameLenSamp];
		fadeOut = new float[(int)frameLenSamp];
		frame = new float[(int)frameLenSamp];
		output = new float[(int)frameLenSamp*f0track.length];
		
		for(int i=0;i<frameLenSamp;i++){
			timeAr[i]=(i/fSample);
		}
		for(int i=0;i<frameLenSamp;i++){
			fadeIn[i]=i/(frameLenSamp-1);
		}
		//RE-CODE THIS!!!!
		for(int i=0;i>fadeOut.length;i++){
			fadeOut[i]=fadeIn[fadeIn.length-1-i];
		}	
	}
	
	public SineSynth(){
		af = new AudioFormat(44100,16,1,true,false);
		frame = new float[44100];
		fSample = 44100;
		time = 0;
		name = "tuneSynth.wav";
		frameLenSamp = 256;
		fadeIn = new float[(int)frameLenSamp];
		fadeOut = new float[(int)frameLenSamp];
		for(int i=0;i<frameLenSamp;i++){
			fadeIn[i]= (float)( i/(frameLenSamp-1));
		}
		//RE-CODE THIS!!!!
		for(int i=0;i>fadeOut.length;i++){
			fadeOut[i]= (float) (frameLenSamp-1-i) / (frameLenSamp-1);
		}	
	}
	public SineSynth(float[] arr){
		name = "from array";
		phase = 0f;
		af = new AudioFormat(44100,16,1,true,false);
		f0track = arr;
		fSample = 44100;
		frameLenSamp = 441;
		timeAr = new float[(int)frameLenSamp];
		fadeIn = new float[(int)frameLenSamp];
		fadeOut = new float[(int)frameLenSamp];
		frame = new float[(int)frameLenSamp];
		output = new float[(int)frameLenSamp*f0track.length];
		
		for(int i=0;i<frameLenSamp;i++){
			timeAr[i]=(i/fSample);
		}
		for(int i=0;i<frameLenSamp;i++){
			fadeIn[i]=i/(frameLenSamp-1);
		}
		//RE-CODE THIS!!!!
		for(int i=0;i>fadeOut.length;i++){
			fadeOut[i]=fadeIn[fadeIn.length-1-i];
		}	
	}
	public void synthSine(float freq){
		frame = new float[44100];
		twoPiF = (float) (2 * Math.PI * freq);
		for(int i = 0 ; i < frame.length ; i++){
			time = i / fSample;
			frame[i] = (float) (gain * Math.sin(twoPiF * time));
		}
		phase = twoPiF * frame.length / fSample + phase;
		/* Creating fade ins and outs*/
		for(int i=0;i<fadeIn.length;i++){
			frame[i] = frame[i]*fadeIn[i];
		}
		for(int i=0;i<fadeOut.length;i++){
			frame[i] = frame[i]*fadeOut[i];
		}	
		output = frame.clone();
	}
	public void synth(){
		System.out.println("Start loops");
		for(int k=0;k<f0track.length;k++){	
			start = k*((int)frameLenSamp);
			
			/*Creating sine wave frame */
			for(int i=0;i<frameLenSamp;i++){
				frame[i]=((float) (Math.cos(2.0*Math.PI*f0track[k]*timeAr[i]+phase)));
			}
	
			/* Phase Correction */
			float first =(float) Math.acos(frame[frame.length-2]);
			float last =(float) Math.acos(frame[frame.length-1]);
			if(first>last){
				phase = (-1f)*last;
			} else {
				phase = last;
			}
			
    		/* Creating fade ins and outs*/
    		if(k==0){
    			for(int i=0;i<fadeIn.length;i++){
    				frame[i] = frame[i]*fadeIn[i];
    			}
    		}
    		else if (f0track[k-1]<30){
    			for(int i=0;i<fadeIn.length;i++){
    				frame[i] = frame[i]*fadeIn[i];
    			}
    		}
    		if(k==(f0track.length-1)){
    			for(int i=0;i<fadeOut.length;i++){
    				frame[i] = frame[i]*fadeOut[i];
    			}	
    		}
    		else if(f0track[k+1]<30){
    			for(int i=0;i<fadeOut.length;i++){
    				frame[i] = frame[i]*fadeOut[i];
    			}	
    		}	
    		
    		/* Adding each frame to output array*/
    		System.arraycopy(frame, 0, output, start, frame.length);
		}	
		System.out.println("Loops ends");

		/* Gain Scaling */
		for(int i=0;i<output.length;i++){
			output[i]=(output[i]*gain);    
		}
		Write.writeText("output.txt", output);
	}
	public AudioFormat getAudioFormat() {
		return af;
	}
	public float[] getOutput() {
		return output;
	}
	public Wavefile getSynthedWave() throws Exception{
		synthedWave = new Wavefile(af,output,name);
		return synthedWave;
	}
	public Wavefile getSineWave() throws Exception{
		if(frame==null){
			synthSine(440);
		}
		sineWave = new Wavefile(af,frame,name);
		return sineWave;
	}
}
