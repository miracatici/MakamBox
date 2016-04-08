package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class TonicAnnotationMatch {
	public static void main(String[] args) {
		TreeMap<String, Float> tonicAnnotations = new TreeMap<String, Float>();
		BufferedReader annotationItem  = null;
		BufferedReader soloRecordings = null;
		try {
			annotationItem = new BufferedReader(new FileReader(new File("/Users/miracatici/Documents/workspace/MakamBox/tonikAnnot.txt")));
			String annotLine = "";
			while ((annotLine = annotationItem.readLine()) != null){
				String[] columns1 = annotLine.split("\t");
				tonicAnnotations.put(columns1[0], Float.valueOf(columns1[1]));
			}
			System.out.println(tonicAnnotations.size());
			
			soloRecordings = new BufferedReader(new FileReader(new File("/Users/miracatici/Documents/workspace/MakamBox/singleMakamSoloRecordings.txt")));
			String recLine = "";
			while ((recLine = soloRecordings.readLine()) != null){
				if(tonicAnnotations.get(recLine) != null){
					System.out.println(recLine + "\t"+tonicAnnotations.get(recLine));
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				annotationItem.close();
				soloRecordings.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
