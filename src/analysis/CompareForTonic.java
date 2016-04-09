package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CompareForTonic {
	public static void main (String[] args){
			BufferedReader analysisFile  = null;
			try {
				analysisFile = new BufferedReader(new FileReader(new File("/Users/miracatici/Documents/workspace/MakamBox/results/analysisForTonicResult.txt")));
				String resultLine = "";
				int TT = 0 ; int FF = 0;
				while ((resultLine = analysisFile.readLine())!=null){
					String[] column = resultLine.split("\t");
					float tonicCalc = (Float.valueOf(column[2]));
					float tonicAnnot = (Float.valueOf(column[3]));
					if(Math.abs(tonicCalc / tonicAnnot)<1.03 && (tonicCalc / tonicAnnot)>0.97f){
						System.out.println(resultLine+"\t"+"True");
						TT++;
					} else if (Math.abs(tonicCalc / (tonicAnnot*2))<1.03 && (tonicCalc / (tonicAnnot*2))>0.97f){
						System.out.println(resultLine+"\t"+"True");
						TT++;
					} else if (Math.abs(tonicCalc / (tonicAnnot/2))<1.03 && (tonicCalc / (tonicAnnot/2))>0.97f){
						System.out.println(resultLine+"\t"+"True");
						TT++;
					} else {
						System.out.println(resultLine+"\t"+"False");
						FF++;
					}
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
}
