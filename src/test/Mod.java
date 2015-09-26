package test;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import backEnd.MakamBox;
import backEnd.Wavefile;

public class Mod {
	public static void main(String[] arg){
		new Mod().start();
	}
	public void start(){
		JFrame frame = new JFrame("yeni");
		frame.setLayout(new FlowLayout());
		frame.setPreferredSize(new Dimension(1300,300));
		frame.setVisible(true);
		
		try {
			MakamBox voxB = new MakamBox("vox.wav",null);
			MakamBox modB = new MakamBox("mod.wav",null);
				
			float[] vox = voxB.getPitchTrackData();
			float[] mod = modB.getPitchTrackData();
			float[] sum = new float[vox.length];
			
			for (int i = 0; i < sum.length; i++) {
				sum[i] = (float) ((float)vox[i] * (float)mod[i]);
			}
//			AudioUtilities.normalizeMax(sum);
			Wavefile sumW = new Wavefile(voxB.getWavefile().getFormat(), sum, "sum.wav");
			MakamBox sumB = new MakamBox(sumW,null);
			
			sumB.getPlayer().play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
