package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import applied.SelectCulture;
import backEnd.MakamBox;
import datas.Culture;

public class AnalysisForJoint {
	public static void main (String[] args){
		SelectCulture.setCulture(Culture.deserialize("settings/TurkishExtended.ser"));
		MakamBox file = null;
		BufferedReader fullFileList  = null;
		String rootPath = "CompMusicCorpus/";
		try {
			fullFileList = new BufferedReader(new FileReader(new File("results/finalFileList.txt")));
			String fileLine = "";
			System.out.println("FileName\tMakamRecog\tTonicRecog\tTonicAnnot\tMakamAnnot");
			while((fileLine = fullFileList.readLine())!=null){
				String[] column = fileLine.split("\t");
				String fileName = column[1];
				file = new MakamBox(rootPath+fileName,null);
				file.createMakam();
				System.out.println(fileName+"\t"+file.getMakamName() + "\t"+ file.getTonicHz()+"\t"+column[2]+"\t"+column[3].replace("makam: ", ""));
				file.killObject();
				file = null;
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				fullFileList.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
