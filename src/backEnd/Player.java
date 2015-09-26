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
 * Main audio player class that uses Java Clip class to transport.
 * The constructors needs JButton object to define Stop Button on interface
 * It has slider set method, set loop position method and set volume method
 * 
 * Created @author mirac
 * Bahcesehir University, 2014
 * 
 */

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.JButton;

import applied.MakamBoxAnalysis;

public class Player{
	public int pos=0,fullLength,startPoint,endPoint;
	private Clip clip;
	private FloatControl gainControl;
	private boolean playing;
	private JButton stopbutton;
	
	public Player(MakamBox obj,JButton button) throws Exception{
		this(obj.getWavefile(),button);
	}
	public Player(Wavefile af,JButton button) throws Exception{
		stopbutton = button;
		setPlayer(af);
	}
	
	public void setPlayer(Wavefile af) throws Exception{
		clip = af.getClip();
        clip.open();
        fullLength = clip.getFrameLength();
        endPoint = fullLength;
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        playing = false;
        clip.addLineListener(new LineListener(){
			@Override
			public void update(LineEvent arg0) {
				while(playing){
					int tempframe = clip.getFramePosition();
					MakamBoxAnalysis.positionSlide.setValue(tempframe);
					if (stopbutton!=null&&tempframe == fullLength){
						stopbutton.doClick();
					} else if(stopbutton!=null && tempframe>=endPoint){
						playAgain();
					} else if (tempframe == fullLength){
						stop();
					}
				}
			}
        });
	}
	public void play(){
		clip.start();
		playing = true;
	}
	public void pause(){
		clip.stop();
		playing = false;
	}
	public void stop(){
		pause();
		setPosition(0);
		startPoint=0;
		endPoint = fullLength;
		setSlider(startPoint,endPoint);
	}
	public void setPosition(int pos){
		clip.setFramePosition(pos);
		MakamBoxAnalysis.positionSlide.setValue(pos);
	}
	public void setLoopPoint(int start,int stop){  // in frame type
		if(start>0 && stop>start && stop<fullLength){
			startPoint = start;
			endPoint = stop;
			setPosition(start);
			setSlider(startPoint,endPoint);
		}
	}
	public int getPosition(){
		return clip.getFramePosition();
	}
	public void setVolume(float value){
		gainControl.setValue(value);
	}
	public void playAgain(){
		setPosition(startPoint);
	}
	public void stopButton(){
		if (stopbutton!=null){
			stopbutton.doClick();
		}
	}
	public void setSlider(int a, int b){
		MakamBoxAnalysis.positionSlide.setMinimum(a);
		MakamBoxAnalysis.positionSlide.setMaximum(b);
		MakamBoxAnalysis.positionSlide.setValue(a);
	}
}

