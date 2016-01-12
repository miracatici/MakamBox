package test;

import backEnd.MakamBox;
import backEnd.SineSynth;

public class Test10 {
    public static void main(String[] args) throws Exception {
    	System.out.println("start");
    	
    	SineSynth sine = new SineSynth();
    	sine.synthSine(440);
    	MakamBox m = new MakamBox(sine.getSineWave(),null);
    	m.getPlayer().play();
    	System.out.println("Stop");
    }
}
