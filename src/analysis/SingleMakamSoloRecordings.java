package analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.io.FileUtils;

public class SingleMakamSoloRecordings {
	
    public static void main(String[] args) {
    	//Audio metadata files folder, file format is json
    	File jsonFolder = new File("turkish_makam_corpus_stats-master/data/AudioMetadata");	
    	
    	//Path contains different folders, so Apache Commons tools do file listing recursively, to a new array
    	File[] jsonFileList = FileUtils.listFiles(jsonFolder, new String[]{"json"}, true).toArray(new File[0]);
    	System.out.println("Number of all files: "+jsonFileList.length);
    	
    	try {
			for (int i = 0; i < jsonFileList.length; i++) {
				JsonReader reader = Json.createReader(new FileReader(jsonFileList[i]));
				JsonObject obj = reader.readObject();
				try {
					JsonArray makamArray = obj.getJsonArray("makam");
					if(makamArray.size() <2 && makamArray.size()>0){
						JsonArray artistArray = obj.getJsonArray("artists");
						if (artistArray.size()<2){
							System.out.println(jsonFileList[i].getName().replaceAll(".json", ""));													
						}
					} else if (makamArray.size() == 0){
//						System.out.println(fileList[i].getName()+ " have makam 0 makam");
					}
				} catch (NullPointerException n){
//					System.out.println(fileList[i].getName()+ " dont have makam");
				}
			}
    	} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
}
