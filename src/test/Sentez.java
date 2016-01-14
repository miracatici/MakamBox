package test;

import java.io.File;

import backEnd.Histogram;
import backEnd.MakamBox;
import backEnd.SineSynth;

public class Sentez {
	public static void main(String[] args){
		try {
			Histogram h = new Histogram(new File("02_cecenkizi_huseyni_kemence.yin.txt"));
			SineSynth ss = new SineSynth(h.getPitchTrack());
			ss.synth();
			MakamBox m = new MakamBox(ss.getSynthedWave(),null);
			m.getPlayer().play();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
