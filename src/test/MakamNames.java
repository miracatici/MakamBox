package test;

import datas.Culture;

public class MakamNames {
	public static void main(String[] args){
		Culture cult = Culture.deserialize("/Users/miracatici/Documents/workspace/MakamBox/settings/TurkishExtended.ser");
		String[] makams = cult.getMakamsData().keySet().toArray(new String[0]);
		
		for (int i = 0; i < makams.length; i++) {
			System.out.println(makams[i]);
		}
	}
}
