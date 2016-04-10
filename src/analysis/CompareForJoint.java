package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CompareForJoint {
	static int TT = 0 ; static int FF = 0;
	public static void main (String[] args){
		BufferedReader analysisFile  = null;
		try {
			analysisFile = new BufferedReader(new FileReader(new File("results/analysisForMakamResult.txt")));
			String resultLine = "";
			while ((resultLine = analysisFile.readLine())!=null){
				String[] column = resultLine.split("\t");
				float tonicAnnot  = Float.valueOf(column[3]); float tonicCalc = Float.valueOf(column[2]); String makamCalc = column[1]; String makamAnnot = column[4];
				boolean tonic = compareTonic(tonicCalc, tonicAnnot);
				boolean makam = compareMakam(makamCalc, makamAnnot); 
				boolean joint = compareJoint(makam, tonic);
				System.out.println(resultLine+"\t"+ tonic+"\t"+makam+"\t"+joint);	
			}
			System.out.println("Result: %"+(TT/255f)*100+ "\t"+"%"+(FF/255f)*100+" is wrong");
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				analysisFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	static public boolean compareJoint(boolean makam, boolean tonic){
		if(makam == true && tonic==true){
			return true;
		} else {
			return false;
		}
	}
	static public boolean compareTonic(float tonicCalc, float tonicAnnot){
		if(Math.abs(tonicCalc / tonicAnnot)<1.035f && (tonicCalc / tonicAnnot)>0.98f){
			return true;
		} else if (Math.abs(tonicCalc / (tonicAnnot*2))<1.035f && (tonicCalc / (tonicAnnot*2))>0.98f){
			return true;
		} else if (Math.abs(tonicCalc / (tonicAnnot/2))<1.035f && (tonicCalc / (tonicAnnot/2))>0.98f){
			return true;
		} else if (Math.abs(tonicCalc / (tonicAnnot*4))<1.035f && (tonicCalc / (tonicAnnot*4))>0.98f){
			return true;
		} else if (Math.abs(tonicCalc / (tonicAnnot/4))<1.035f && (tonicCalc / (tonicAnnot/4))>0.98f){
			return true;
		} else {
			return false;
		}
	}
	static public boolean compareMakam (String makamCalc, String makamAnnot){
		if (makamCalc.equals(makamAnnot)){
			TT++;
			return true;
		} else {
			FF++;
			return false;
		}
	}
}
