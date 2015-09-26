package test;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Test10 {
    public static void main(String[] args) {
    	int nb = 4;
    	if (args != null && args.length > 0) {
    		nb = Integer.parseInt(args[0]);
    	}

    	final int frameCount = nb;
    	SwingUtilities.invokeLater(new Runnable() {
    		@Override
			public void run() {
    			for (int i = 0; i < frameCount; i++) {
    				JFrame frame = new JFrame("Frame number " + i);
    				frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    				JPanel p = new JPanel(new BorderLayout());
    				p.add(new JLabel("Click on the corner to close..."), BorderLayout.CENTER);
    				frame.setContentPane(p);
    				frame.setSize(200, 200);
    				frame.setLocation(100 + 20 * i, 100 + 20 * i);
    				frame.setVisible(true);
    			}
    		}
    	});

    }
}