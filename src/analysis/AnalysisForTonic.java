package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import applied.SelectCulture;
import backEnd.MakamBox;
import datas.Culture;

public class AnalysisForTonic {
	public static void main (String[] args){
		SelectCulture.setCulture(Culture.deserialize("settings/TurkishExtended.ser"));
		MakamBox file = null;
		BufferedReader fullFileList  = null;
		String rootPath = "CompMusicCorpus/";
		PrintWriter resultWrite = null;
		try {
			resultWrite = new PrintWriter(new File("results/analysisForTonicResult.txt"));
			fullFileList = new BufferedReader(new FileReader(new File("results/finalFileList.txt")));
			String fileLine = "";
			while((fileLine = fullFileList.readLine())!=null){
				String[] column = fileLine.split("\t");
				String fileName = column[1];
				file = new MakamBox(rootPath+fileName,null);
				file.createMakam(column[3].replace("makam: ", ""));
				String res = fileName+"\t"+file.getMakamName() + "\t"+ file.getTonicHz()+"\t"+column[2]+"\t"+column[3];
				System.out.println(res);
				resultWrite.println(res);
				resultWrite.flush();
				file.killObject();
				file = null;
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				resultWrite.close();
				fullFileList.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
