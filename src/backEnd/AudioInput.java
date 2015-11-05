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

	private AudioFormat format;  														// Recording format
	private TargetDataLine recLine ; 													// Getting mixer line for recording
	private ByteArrayOutputStream out; 													// Stream for byte array output
	 
	public AudioInput(AudioFormat af) { 												//Passing audio format 
		format = af;
	}
	
	public void captureAudio() {									
		try {														
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);		// Dataline info Defining format and creating TargetDataLine
			recLine = (TargetDataLine) AudioSystem.getLine(info);						// Getting mixer line from driver (system selected)
			recLine.open(format);														// Opening recording line (make connection)
			recLine.start();															// Start draining line
			Thread captureThread = new Thread(new Runnable() {							// Create new runner Define new thread New thread for audio capture
				@Override
				public void run() {
					out = new ByteArrayOutputStream();									// Creating stream to able to write buffer pack
					boolean running = true;												// Is recording? For draining capture method.
					byte[] buffer = new byte[256];										// Buffer array Buffer for recorded package
					try {
						while (running) {
							int count = recLine.read(buffer, 							// Reading samples to buffer
													 0, 
													 buffer.length);
							if (count > 0) {											// If reading is true, write to output stream
								out.write(buffer, 0, count);
							}
						}
						out.close();													// When stopped, close the stream
					} catch (IOException e) {
						System.err.println("I/O problems: " + e);
						System.exit(-1);
					}
				}
			}); 						
			captureThread.start();														// Start for capture, invoke thread
		} catch (LineUnavailableException e) {
			System.err.println("Line unavailable: " + e);
			System.exit(-2);
		}
	}
	public void stopCapture() throws Exception{											// Stop recording method
		recLine.stop();																	// Stop recording from line	
		recLine.close();																// Close the line got from system
	}
	public MakamBox getRecordedBox(JButton b) throws Exception {						// Create MakamBox object to use everywhere :)
		byte[] buff = out.toByteArray();												// Write stream to array
		Wavefile af = new Wavefile(format, buff, 										// Create wave file object with given parameter
				String.valueOf(System.currentTimeMillis()));
		MakamBox recordedBox = new MakamBox(af,b);										// Create MakamBox object that created Wave file
		return recordedBox;
	}
}