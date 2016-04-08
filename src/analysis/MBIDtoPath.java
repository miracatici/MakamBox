package analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.TreeMap;

public class MBIDtoPath {
	public static void main (String[] args){
		TreeMap<String,String> pathTree = new TreeMap<String,String>();	
		TreeMap<String,String> makamTree = new TreeMap<String,String>();	
		BufferedReader pathFile  = null;
		BufferedReader mbidFile = null;
		try {
			pathFile = new BufferedReader(new FileReader(new File("/Users/miracatici/Documents/workspace/MakamBox/filePath.txt")));
			mbidFile = new BufferedReader(new FileReader(new File("/Users/miracatici/Documents/workspace/MakamBox/finalFileList.txt")));
			
			String pathLine = "";
			while ((pathLine = pathFile.readLine())!=null){
				String[] column = pathLine.split("\t");
				pathTree.put(column[0], column[1]);
				if (column.length==3){
					makamTree.put(column[0], column[2]);
				} else {
					makamTree.put(column[0], " ");
				}
			}
			String fileLine = "";
			while((fileLine = mbidFile.readLine())!=null){
				String[] column2 = fileLine.split("\t");
				if(pathTree.get(column2[0]) != null){
					System.out.println(column2[0]+"\t"+pathTree.get(column2[0])+"\t"+column2[1] + "\t"+ makamTree.get(column2[0]));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
