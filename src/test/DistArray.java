package test;

import backEnd.MakamBox;
import utilities.Plot;

public class DistArray {
	public static void main (String[] args){
		try {
			MakamBox m = new MakamBox("taksim_2_h_b-m.wav", null);
			m.createMakam();
			float[][] d =  m.getMakam().getDistanceArray();
			float [][] d2 = new float[3][];
			for (int i = 0; i < d2.length; i++) {
				d2[i] = d[i];
			}
			Plot.plot(d2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
