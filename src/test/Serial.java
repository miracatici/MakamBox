package test;

public class Serial {
//	private HashMap<String, ClipData> results;
//	int noteContain = 2 ;
//	String type = "n";
//	int sira = 0;
//	String[] opt ;
//	File folder;
//	File[] files;
//	
//	public Serial(){
//		opt = new String[]{"Minor Second","Major Second","Minor Third","Major Third"};
//		folder = new File("/Users/mirac/Documents/workspace/TrainEar/assets/audio/n/2");
//		files = folder.listFiles();
//		results = new HashMap<String, ClipData>();
//	}
//	public void process(){
//		System.out.println(files.length);
//		for (int i = 0; i < files.length; i++) {
//			String name = files[i].getName();
//			String name2 = name.replace(".mp3", "");
//			float[] tempRes = new float[noteContain];	
//			String[] nameSplit = name2.split("_");
//			for (int j = 0; j < noteContain; j++) {
//				tempRes[j] = Float.valueOf(nameSplit[j+2]);
//			}
//			if(sira == 0){
//				results.put(name, new ClipData(name,"Minor Second",tempRes,opt));	
//				sira++;				
//			} else if(sira == 1){
//				results.put(name, new ClipData(name,"Major Second",tempRes,opt));	
//				sira++;				
//			} else if(sira == 2){
//				results.put(name, new ClipData(name,"Minor Third",tempRes,opt));	
//				sira++;				
//			} else if(sira == 3){
//				results.put(name, new ClipData(name,"Major Third",tempRes,opt));	
//				sira = 0;
//			}	
//		}
//		serialize(type+"_"+String.valueOf(noteContain),results);
//	}
//	public void serialize(String name, Object obj){
//		System.out.println("Start");	
//		FileOutputStream fileOut;
//		ObjectOutputStream out;
//		try {
//			fileOut = new FileOutputStream(name+".ser");
//			out = new ObjectOutputStream(fileOut);
//			out.writeObject(obj);
//			out.close();
//			fileOut.close();
//			System.out.println("Done");	
//		} catch (FileNotFoundException e) {
//			System.out.println("Error1");	
//		} catch (IOException e) {
//			System.out.println("Error2");	
//		}
//		toString();
//	}
//	public Object deserialize(String path){
//		FileInputStream fileIn;
//        ObjectInputStream in;
//        Object newCulture = null;
//		try {	        
//			fileIn = new FileInputStream(path);
//	        in = new ObjectInputStream(fileIn);
//	        newCulture = in.readObject();
//	        in.close();
//	        fileIn.close();
//		}
//		catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return newCulture;
//	}
//	@SuppressWarnings("unchecked")
//	public String toString(){
//		String print = "";
//		HashMap<String, ClipData>  data = (HashMap<String, ClipData> ) deserialize("/Users/mirac/Documents/workspace/MakamBox/n_2.ser");
//		for (int i = 0; i < data.size(); i++) {
//			System.out.println(data.get(files[i].getName()).getName());
//			System.out.println(data.get(files[i].getName()).getTheoryAnswer());
//			float[] temp = data.get(files[i].getName()).getFreqAnswer();
//			for (int j = 0; j < temp.length; j++) {
//				System.out.print(temp[j] + " ");
//			}
//			System.out.println("");
//		}
//		return print;
//		
//	}
//	public static void main (String[] args){
//		Serial s = new Serial();
//		s.process();
//	}
}
