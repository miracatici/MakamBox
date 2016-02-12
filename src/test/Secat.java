package test;

import java.io.FileWriter;
import java.io.PrintWriter;

import backEnd.PitchDetection;
import backEnd.Wavefile;

public class Secat {
	public static void main (String[] args){
		try {
			PitchDetection pd = new PitchDetection(new Wavefile("test.wav"));
			float[] fres = pd.getPitchResult();
			System.out.println(pd.getBufferSize());
			PrintWriter outFile = new PrintWriter(new FileWriter("result2.txt"));
			for (int i = 0; i < fres.length; i++) {
				outFile.println(fres[i]);
			}
			outFile.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}
}
