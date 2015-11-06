package test;

import java.io.File;
import java.util.Arrays;

import backEnd.PitchDetection;
import backEnd.Wavefile;
import utilities.AudioUtilities;

public class Test14 {
	public static void main(String[] args) throws Exception {
		float[] rawData;
		File dir = new File("dataset");
		File[] files = dir.listFiles();
		
		for (int i = 0; i < files.length; i++) {
			Wavefile wf = new Wavefile(files[i]);
			rawData = wf.getFloatData();
			float a = analyze(rawData,1);
			System.out.println(a+" "+ AudioUtilities.hertzToCent(a));
		}
	}
	public static float analyze(float[]rawDat,int noteNumber){
		float answerResult = 0f;
		PitchDetection pd = new PitchDetection(rawDat,44100);
		float[] pr = pd.getPitchResult();
		float[][] prc = pd.chunkPitchTrack(pr);
		float[][] prcl = pd.pickLongChunks(prc, noteNumber);
		for (int i = 0; i < prcl.length; i++) {
			answerResult = AudioUtilities.findMedian(prcl[i]);
		}
//		Arrays.sort(answerResult);
		return answerResult;
	}
}
