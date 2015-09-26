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

 package backEnd;

/**
 * Audio recording class to record audio from default system input line
 * 
 * Created @author mirac
 * Bahcesehir University, 2014
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;

public class AudioInput{

	private AudioFormat format;  		// Recording format
	private DataLine.Info info; 		// Defining format and creating TargetDataLine
	private TargetDataLine recLine ; 	// Getting mixer line for recording
	private ByteArrayOutputStream out; 	// Stream for byte array output
	private Runnable runner; 			// Runner for new thread
	private Thread captureThread; 		// New thread for audio capture
	private int bufferSize; 			// Sample size of recorded package (buffer)
	private Wavefile af; 				// Wave file object for recorded audio. Create after stopping record
	private MakamBox recordedBox; 		// MakamBox object that created with 'af' Wavefile object
	private byte[] buffer,buff;  		// Buffer for recorded package
	public boolean running; 			// For draining capture method.
	 
	public AudioInput(AudioFormat af) { //Passing audio format 
		format = af;
	}
	public void runner(){
		runner = new Runnable() {									// Create new runner
			@Override
			public void run() {
				out = new ByteArrayOutputStream();					// Creating stream to able to write buffer pack
				running = true;										// Is recording?
				try {
					while (running) {
						int count = recLine.read(buffer, 			// Reading samples to buffer
												 0, 
												 buffer.length);
						if (count > 0) {							// If reading is true, write to output stream
							out.write(buffer, 0, count);
						}
					}
					out.close();									// When stopped, close the stream
				} catch (IOException e) {
					System.err.println("I/O problems: " + e);
					System.exit(-1);
				}
			}
		}; 
	}
	public void captureAudio() {									
		try {														
			bufferSize = 256;  											// Define Buffer size
			buffer = new byte[bufferSize];								// Buffer array
			info = new DataLine.Info(TargetDataLine.class, format);		// Dataline info
			recLine = (TargetDataLine) AudioSystem.getLine(info);		// Getting mixer line from driver (system selected)
			recLine.open(format);										// Opening recording line (make connection)
			recLine.start();											// Start draining line
			runner();													// Create new runner for thread
			captureThread = new Thread(runner);							// Define new thread
			captureThread.start();										// Start for capture, invoke thread
		} catch (LineUnavailableException e) {
			System.err.println("Line unavailable: " + e);
			System.exit(-2);
		}
	}
	public void stopCapture() throws Exception{							// Stop recording method
		recLine.stop();													// Stop recording from line	
		recLine.close();												// Close the line got from system
	}
	public MakamBox getRecordedBox(JButton b) throws Exception {		// Create MakamBox object to use everywhere :)
		if(recordedBox==null){											// Check if it's created before				
			buff = out.toByteArray();									// Write stream to array
			af = new Wavefile(format, buff, 							// Create wave file object with given parameter
					String.valueOf(System.currentTimeMillis()));
			recordedBox = new MakamBox(af,b);							// Create MakamBox object that created Wave file
		}
		return recordedBox;
	}
}