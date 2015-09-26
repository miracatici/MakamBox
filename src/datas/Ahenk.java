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

import java.io.Serializable;

/**
 * This class is used for serializing ahenk data to a *.ser file which is used for transposing the audio file
 */


public class Ahenk implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1271410877893223878L;
	
	public String name;
	public String baseNote;
	public Float baseFreq;
	
	public Ahenk(String n,String b, float f){
		name = n;
		baseNote = b;
		baseFreq = f;
	}
}
