package utilities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Write {

	public static void writeText(String path, float[] data){
		System.out.println("Star");
		PrintWriter outFile;
		try {
			outFile = new PrintWriter(new FileWriter(path));
			for(int i=0;i<data.length;i++){
				outFile.println(data[i]);
			}
			outFile.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
