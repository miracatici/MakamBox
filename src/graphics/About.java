package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class About extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3944578980125481440L;

	/**
	 * Create the dialog.
	 */
	public About() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("About");
		setBounds(100, 100, 502, 493);
		setLocation(new Point(450, 150));
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setLayout(null);
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		okButton.setBounds(394, 283, 75, 29);
		getContentPane().add(okButton);
		okButton.setActionCommand("OK");
		getRootPane().setDefaultButton(okButton);
		
		JTextPane txtpnMakamboxCulturespecific = new JTextPane();
		txtpnMakamboxCulturespecific.setText("\tThis is the tuning analysis software that is a tool that helps users to analyze recordings. The project aims developing a computer based interactive tuning analysis system specifically for Makam music traditions.  \n\n\tThis involves implementation of pitch analysis and visualisation tools that can work real-time and provide a visual feedback to the user for comparison of the user input signal and the reference recording. By creating a culture-specific settings file, software can be used for different traditional music theories, such as Arab, Iran, Asia etc.\n\nDesigned, developed and implemented by Bilge Miraç ATICI\nSupervised by Barış BOZKURT");
		txtpnMakamboxCulturespecific.setForeground(Color.WHITE);
		txtpnMakamboxCulturespecific.setBackground(Color.DARK_GRAY);
		txtpnMakamboxCulturespecific.setBounds(19, 60, 477, 208);
		getContentPane().add(txtpnMakamboxCulturespecific);
		
		JTextPane txtpnMakamboxCulture = new JTextPane();
		txtpnMakamboxCulture.setEditable(false);
		txtpnMakamboxCulture.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		txtpnMakamboxCulture.setText("   Culture Specific Music Anaysis Tools \nfor Makam Music Traditions - MakamBox");
		txtpnMakamboxCulture.setForeground(Color.WHITE);
		txtpnMakamboxCulture.setBackground(Color.DARK_GRAY);
		txtpnMakamboxCulture.setBounds(90, 11, 323, 38);
		getContentPane().add(txtpnMakamboxCulture);
		
		JLabel lblERC = new JLabel("ERC");
		lblERC.setIcon(new ImageIcon(About.class.getResource("/erc.jpg")));
		lblERC.setBounds(12, 324, 311, 137);
		getContentPane().add(lblERC);
		
		JLabel lblCompmusic = new JLabel("CompMusic");
		lblCompmusic.setIcon(new ImageIcon(About.class.getResource("/compLogo.png")));
		lblCompmusic.setBounds(326, 324, 163, 131);
		getContentPane().add(lblCompmusic);
		
		JTextPane txtpnLink = new JTextPane();
		txtpnLink.setBackground(Color.DARK_GRAY);
		txtpnLink.setForeground(Color.CYAN);
		txtpnLink.setFont(new Font("Courier", Font.ITALIC, 13));
		txtpnLink.setText("http://www.miracatici.com/makambox");
		txtpnLink.setBounds(19, 292, 279, 14);
		getContentPane().add(txtpnLink);
		
	}
}
