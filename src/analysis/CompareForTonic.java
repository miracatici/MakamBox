package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CompareForTonic {
	static int TT = 0 ; static int FF = 0;
	public static void main (String[] args){
			BufferedReader analysisFile  = null;
			try {
				analysisFile = new BufferedReader(new FileReader(new File("results/analysisForTonicResult.txt")));
				String resultLine = "";
				while ((resultLine = analysisFile.readLine())!=null){
					String[] column = resultLine.split("\t");
					float tonicCalc = (Float.valueOf(column[2]));
					float tonicAnnot = (Float.valueOf(column[3]));
					System.out.print(resultLine +"\t");
					compareTonic(tonicCalc, tonicAnnot);
				}
				System.out.println("Result: %"+(TT/255f)*100+ "\t"+"%"+(FF/255f)+" is wrong");
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
	public static boolean compareTonic(float tonicCalc, float tonicAnnot){
		if(Math.abs(tonicCalc / tonicAnnot)<1.035f && (tonicCalc / tonicAnnot)>0.98f){
			System.out.println("True"+"\t"+Math.abs(tonicCalc / tonicAnnot));
			TT++;
			return true;
		} else if (Math.abs(tonicCalc / (tonicAnnot*2))<1.035f && (tonicCalc / (tonicAnnot*2))>0.98f){
			System.out.println("True"+"\t"+Math.abs(tonicCalc / (tonicAnnot*2)));
			TT++;
			return true;
		} else if (Math.abs(tonicCalc / (tonicAnnot/2))<1.035f && (tonicCalc / (tonicAnnot/2))>0.98f){
			System.out.println("True"+"\t"+Math.abs(tonicCalc / (tonicAnnot/2)));
			TT++;
			return true;
		} else if (Math.abs(tonicCalc / (tonicAnnot*4))<1.035f && (tonicCalc / (tonicAnnot*4))>0.98f){
			System.out.println("True"+"\t"+Math.abs(tonicCalc / (tonicAnnot*4)));
			TT++;
			return true;
		} else if (Math.abs(tonicCalc / (tonicAnnot/4))<1.035f && (tonicCalc / (tonicAnnot/4))>0.98f){
			System.out.println("True"+"\t"+Math.abs(tonicCalc / (tonicAnnot/4)));
			TT++;
			return true;
		} else {
			System.out.println("False");
			FF++;
			return false;
		}
	}
}
