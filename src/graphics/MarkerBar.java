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

package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class MarkerBar extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int xAxis=0;
	private Color renk;
	
	public MarkerBar(Color colour){
		this.setPreferredSize(new Dimension(713,230));
		this.setOpaque(false);
		this.setVisible(true);
		renk = colour;
	}
	public void addLine(int a1) {
		xAxis = a1/650;        
		repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		g.setColor (renk);
		g.fillRect(xAxis, 5, 3 , 180);
	}
	public int getXaxis() {
		return xAxis;
	}
}
