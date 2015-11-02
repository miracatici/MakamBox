package test;

import java.io.File;

import backEnd.MakamBox;
import backEnd.PitchDetection;
import utilities.Plot;

public class Test13 {
	public static void main(String[] args) throws Exception{
			
			File dir = new File("/Users/miracatici/Documents/workspace/testDataSets/tetTestData/min");
			File[] list = dir.listFiles();
			
			for (int i = 0; i < list.length; i++) {
				MakamBox mb = new MakamBox(list[i], null);
				PitchDetection pd = mb.getPitchDetection();
				float[] pr = pd.getPitchResult();
				float[] prnew = new float[pr.length];
				Float[][] prc = pd.chunkPitchTrack(pr);
				int k=0;
				for (int t = 0; t < prc.length; t++) {
					for (int j = 0; j < prc[t].length; j++) {
						prnew[k] = prc[t][j];
						k++;
					}
				}
				Plot.addBar(pd.getChunkPosition());
				Plot.plot(pr, prnew);
				System.out.println("======");
			}
	}
}
