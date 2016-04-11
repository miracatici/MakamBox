/** This file is part of MakamBox.

    MakamBox is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    MakamBox is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with MakamBox.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * MakamBox is an implementation of MakamToolBox Turkish Makam music analysis tool which is developed by Baris Bozkurt
 * This is the project of music tuition system that is a tool that helps users to improve their skills to perform music. 
 * This thesis aims at developing a computer based interactive tuition system specifically for Turkish music. 
 * 
 * Designed and implemented by @author Bilge Mirac Atici
 * Supervised by @supervisor Baris Bozkurt
 * Bahcesehir University, 2014-2015
 */

package datas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class TuningSystem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private TreeMap<String,Makam> makamsMap;
	
	public TuningSystem(File system){
		name = system.getName();
		makamsMap = new TreeMap<String,Makam>();
		readTheory(system.getAbsolutePath());
	}
	private void readTheory(String theoryFile){
		try {
			String line;
			BufferedReader buffReader = new BufferedReader (new FileReader(theoryFile));
			while((line = buffReader.readLine())!=null){
				String[] temp = line.split("\t");
				float[] interval = new float[temp.length-1];
				for (int i = 1; i < temp.length; i++) {
					 interval[i-1] = Float.valueOf(temp[i]); 
				}
				makamsMap.put(temp[0], new Makam(temp[0],interval));
			}
			buffReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeSystemToFile(String outputFilePath){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFilePath)));
			Set<Map.Entry<String,Makam>> set = makamsMap.entrySet();
			// Get an iterator
			Iterator<Entry<String, Makam>> it = set.iterator();
			// Display elements
			while(it.hasNext()){
				Map.Entry<String,Makam> me = it.next();
				bw.write(me.getValue().toString());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TreeMap<String, Makam> getMakamsMap() {
		return makamsMap;
	}
	public void setMakamsMap(TreeMap<String, Makam> makamsMap) {
		this.makamsMap = makamsMap;
	}
	
	public void serialize(String name){
		if(name.length()==0){
			setName("TuningSystems.ser");
		} else if(name.endsWith(".ser")){
			setName(name);
		} else{
			setName(name+".ser");
		}
		FileOutputStream fileOut;
		ObjectOutputStream out;
		try {
			fileOut = new FileOutputStream(name+".ser");
			out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
			JOptionPane.showMessageDialog(null, "Tuning System data file is created");
			System.out.println("Serialized data is saved in /"+name+".ser");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static TuningSystem deserialize(String path){
		FileInputStream fileIn;
		ObjectInputStream in;
		TuningSystem newSystem = null;
		try{
			fileIn = new FileInputStream(path);
	        in = new ObjectInputStream(fileIn);
	        newSystem = (TuningSystem) in.readObject();
	        in.close();
	        fileIn.close();
			JOptionPane.showMessageDialog(null, "Tuning system file is read");
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null,"Please select a file");
		}
		return newSystem;
	}
}
