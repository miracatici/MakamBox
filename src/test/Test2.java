package test;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;



public class Test2 {
	public static void main (String[] args) throws Exception{
		JFrame frame = new JFrame("yeni");
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);
		frame.setPreferredSize(new Dimension(1300,300));
		JButton tuneButton = new JButton("Start tuning");
		frame.getContentPane().add(tuneButton);
		
		tuneButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		frame.pack();
	}
}
