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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class Culture implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7684878716093729483L;
	private TreeMap<String,Note> notesData;
	private TreeMap<String,Makam> makamsData;
	private TreeMap<String,Ahenk> ahenksData;
	private String name;
	
	public Culture(){
		// Just to use object and adding attributes
	}
	public Culture(String name,TreeMap<String,Note> note,TreeMap<String,Makam> makam,TreeMap<String,Ahenk> ahenk){
		notesData = note;
		makamsData = makam;
		ahenksData = ahenk;
		this.name = name;
	}

	public TreeMap<String, Note> getNotesData() {
		if(notesData==null){
			JOptionPane.showMessageDialog(null,"There is no note data");
			return null;
		} else {
			return notesData;
		}
	}
	public TreeMap<String, Makam> getMakamsData() {
		if(makamsData==null){
			JOptionPane.showMessageDialog(null,"There is no makam data");
			return null;
		} else {
			return makamsData;
		}
	}
	public TreeMap<String, Ahenk> getAhenksData() {
		if(ahenksData==null){
			JOptionPane.showMessageDialog(null,"There is no ahenk data");
			return null;
		} else {
			return ahenksData;
		}	
	}
	public String getName() {
		if(name==null){
			JOptionPane.showMessageDialog(null,"There is no name data");
			return null;
		} else {
			return name;
		}
	}

	public void setNotesData(TreeMap<String, Note> notesData) {
		this.notesData = notesData;
	}
	public void setMakamsData(TreeMap<String, Makam> makamsData) {
		this.makamsData = makamsData;
	}
	public void setAhenksData(TreeMap<String, Ahenk> ahenksData) {
		this.ahenksData = ahenksData;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void serialize(String name){
		if(name.length()==0){
			setName("default.ser");
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
			JOptionPane.showMessageDialog(null, "Culture data file is created");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Culture data file isn't created");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Culture data file isn't created");
		}
	}
	public static Culture deserialize(String path){
		FileInputStream fileIn;
        ObjectInputStream in;
        Culture newCulture = null;
		try{	        
			fileIn = new FileInputStream(path);
	        in = new ObjectInputStream(fileIn);
	        newCulture = (Culture) in.readObject();
	        in.close();
	        fileIn.close();
			JOptionPane.showMessageDialog(null, "Culture data file is read");
		}
		catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,"Culture data isn't read");
		}
		return newCulture;
	}
	public static Culture deserializeFromJar(InputStream st){
        ObjectInputStream in;
        Culture newCulture = null;
		try{	        
	        in = new ObjectInputStream(st);
	        newCulture = (Culture) in.readObject();
	        in.close();
	        st.close();
			JOptionPane.showMessageDialog(null, "Culture data file is read");
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null,"Culture data isn't read");
		}
		return newCulture;
	}
}
