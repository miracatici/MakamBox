package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import utilities.AudioUtilities;

public class CompareForTonic {
	public static void main (String[] args){
			BufferedReader analysisFile  = null;
			try {
				analysisFile = new BufferedReader(new FileReader(new File("/Users/miracatici/Documents/workspace/MakamBox/analysisForTonicResult.txt")));
				String resultLine = "";
				while ((resultLine = analysisFile.readLine())!=null){
					String[] column = resultLine.split("\t");
					float tonicCalc = AudioUtilities.hertzToCent(Float.valueOf(column[2]));
					float tonicAnnot = AudioUtilities.hertzToCent(Float.valueOf(column[3]));
					
					if(Math.abs(tonicCalc - tonicAnnot)<15.5f){
						System.out.println(resultLine+"\t"+"True");
					} else if (Math.abs(tonicCalc - (tonicAnnot-1200))<15.5f){
						System.out.println(resultLine+"\t"+"True");
					} else if (Math.abs(tonicCalc - (tonicAnnot+1200))<15.5f){
						System.out.println(resultLine+"\t"+"True");
					} else {
						System.out.println(resultLine+"\t"+"False");
					}
					
//					if (Math.abs(tonicCalc - tonicAnnot)<10f){
//						System.out.println(resultLine+"\t"+"True");
//					} else {
//						System.out.println(resultLine+"\t"+"False");
//					}
					
				}
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
