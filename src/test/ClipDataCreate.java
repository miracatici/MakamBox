//package test;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.util.HashMap;
//
//import org.example.trainear.data.ClipData;
//
//public class ClipDataCreate {
//	
//	public static void main (String[] args){
//		HashMap<String, ClipData> clipdatas = new HashMap<String,ClipData>();
//		String[] optionList = new String[]{"Major Second","Minor Second","Major Third","Minor Third"};
//		try {
//			BufferedReader file = new BufferedReader(new FileReader("n_2.txt"));
//			String line = "";
//			while((line = file.readLine())!= null){
//				String[] parse = line.split("\t");
//				System.out.println(parse.length);
//				for (int i = 0; i < parse.length; i++) {
//					System.out.print(parse[i]+ " ");
//				}
//				System.out.println();
//				clipdatas.put(parse[0], new ClipData(parse[0],parse[1],new float[]{Float.valueOf(parse[2]),Float.valueOf(parse[3])},optionList));
//			}
//			file.close();
//			FileOutputStream fileOut;
//			ObjectOutputStream out;
//			String name = "n_2";
//			try {
//				fileOut = new FileOutputStream(name+".ser");
//				out = new ObjectOutputStream(fileOut);
//				out.writeObject(clipdatas);
//				out.close();
//				fileOut.close();
//				System.out.println("Serialized data is saved in /"+name+".ser");
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
