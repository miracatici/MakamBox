package test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import datas.Culture;
import datas.Makam;

public class Test9 {
	
	public Test9(SortedMap<String,Makam> makams){
		Set<Map.Entry<String,Makam>> set = makams.entrySet();
		// Get an iterator
		Iterator<Entry<String, Makam>> it = set.iterator();
		// Display elements
		while(it.hasNext()){
			Map.Entry<String,Makam> me = it.next();
			System.out.print(me.getValue().getName()+"\t");
			System.out.print(me.getValue().getTonicNote()+"\t");
			System.out.println(me.getValue());
		}	
	}
	public static void main(String[] args){
		Culture cult = Culture.deserialize("/Users/mirac/Documents/workspace/DataTool/arab.ser");
		SortedMap<String,Makam> makam = cult.getMakamsData();
		new Test9(makam);
	}
}
