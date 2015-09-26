/*
*      _______                       _____   _____ _____  
*     |__   __|                     |  __ \ / ____|  __ \ 
*        | | __ _ _ __ ___  ___  ___| |  | | (___ | |__) |
*        | |/ _` | '__/ __|/ _ \/ __| |  | |\___ \|  ___/ 
*        | | (_| | |  \__ \ (_) \__ \ |__| |____) | |     
*        |_|\__,_|_|  |___/\___/|___/_____/|_____/|_|     
*                                                         
* -------------------------------------------------------------
*
* TarsosDSP is developed by Joren Six at IPEM, University Ghent
*  
* -------------------------------------------------------------
*
*  Info: http://0110.be/tag/TarsosDSP
*  Github: https://github.com/JorenSix/TarsosDSP
*  Releases: http://0110.be/releases/TarsosDSP/
*  
*  TarsosDSP includes modified source code by various authors,
*  for credits and info, see README.
* 
*/
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
 * An implementation of the AUBIO_YIN pitch tracking algorithm. See <a href=
 * "http://recherche.ircam.fr/equipes/pcm/cheveign/ps/2002_JASA_YIN_proof.pdf"
 * >the YIN paper.</a> Implementation based on <a
 * href="http://aubio.org">aubio</a>
 * 
 * @author Joren Six
 * @author Paul Brossier
 */
public final class Yin implements PitchDetector {
	/**
	 * The default YIN threshold value. Should be around 0.10~0.15. See YIN
	 * paper for more information.
	 */
	private static final double DEFAULT_THRESHOLD = 0.20;

	/**
	 * The default size of an audio buffer (in samples).
	 */
	public static final int DEFAULT_BUFFER_SIZE = 2048;

	/**
	 * The default overlap of two consecutive audio buffers (in samples).
	 */
	public static final int DEFAULT_OVERLAP = 1536;

	/**
	 * The actual YIN threshold.
	 */
	private final double threshold;

	/**
	 * The audio sample rate. Most audio has a sample rate of 44.1kHz.
	 */
	private final float sampleRate;

	/**
	 * The buffer that stores the calculated values. It is exactly half the size
	 * of the input buffer.
	 */
	private final float[] yinBuffer;
	
	/**
	 * The result of the pitch detection iteration.
	 */
	private final PitchDetectionResult result;

	/**
	 * Create a new pitch detector for a stream with the defined sample rate.
	 * Processes the audio in blocks of the defined size.
	 * 
	 * @param audioSampleRate
	 *            The sample rate of the audio stream. E.g. 44.1 kHz.
	 * @param bufferSize
	 *            The size of a buffer. E.g. 1024.
	 */
	public Yin(final float audioSampleRate, final int bufferSize) {
		this(audioSampleRate, bufferSize, DEFAULT_THRESHOLD);
	}

	/**
	 * Create a new pitch detector for a stream with the defined sample rate.
	 * Processes the audio in blocks of the defined size.
	 * 
	 * @param audioSampleRate
	 *            The sample rate of the audio stream. E.g. 44.1 kHz.
	 * @param bufferSize
	 *            The size of a buffer. E.g. 1024.
	 * @param yinThreshold
	 *            The parameter that defines which peaks are kept as possible
	 *            pitch candidates. See the YIN paper for more details.
	 */
	public Yin(final float audioSampleRate, final int bufferSize, final double yinThreshold) {
		this.sampleRate = audioSampleRate;
		this.threshold = yinThreshold;
		yinBuffer = new float[bufferSize / 2];
		result = new PitchDetectionResult();
	}

	/**
	 * The main flow of the YIN algorithm. Returns a pitch value in Hz or -1 if
	 * no pitch is detected.
	 * 
	 * @return a pitch value in Hz or -1 if no pitch is detected.
	 */
	public PitchDetectionResult getPitch(final float[] audioBuffer) {

		final int tauEstimate;
		final float pitchInHertz;

		// step 2
		difference(audioBuffer);

		// step 3
		cumulativeMeanNormalizedDifference();

		// step 4
		tauEstimate = absoluteThreshold();

		// step 5
		if (tauEstimate != -1) {
			final float betterTau = parabolicInterpolation(tauEstimate);

			// step 6
			// TODO Implement optimization for the AUBIO_YIN algorithm.
			// 0.77% => 0.5% error rate,
			// using the data of the YIN paper
			// bestLocalEstimate()

			// conversion to Hz
			pitchInHertz = sampleRate / betterTau;
		} else{
			// no pitch found
			pitchInHertz = -1;
		}
		
		result.setPitch(pitchInHertz);

		return result;
	}

	/**
	 * Implements the difference function as described in step 2 of the YIN
	 * paper.
	 */
	private void difference(final float[] audioBuffer) {
		int index, tau;
		float delta;
		for (tau = 0; tau < yinBuffer.length; tau++) {
			yinBuffer[tau] = 0;
		}
		for (tau = 1; tau < yinBuffer.length; tau++) {
			for (index = 0; index < yinBuffer.length; index++) {
				delta = audioBuffer[index] - audioBuffer[index + tau];
				yinBuffer[tau] += delta * delta;
			}
		}
	}

	/**
	 * The cumulative mean normalized difference function as described in step 3
	 * of the YIN paper. <br>
	 * <code>
	 * yinBuffer[0] == yinBuffer[1] = 1
	 * </code>
	 */
	private void cumulativeMeanNormalizedDifference() {
		int tau;
		yinBuffer[0] = 1;
		float runningSum = 0;
		for (tau = 1; tau < yinBuffer.length; tau++) {
			runningSum += yinBuffer[tau];
			yinBuffer[tau] *= tau / runningSum;
		}
	}

	/**
	 * Implements step 4 of the AUBIO_YIN paper.
	 */
	private int absoluteThreshold() {
		// Uses another loop construct
		// than the AUBIO implementation
		int tau;
		// first two positions in yinBuffer are always 1
		// So start at the third (index 2)
		for (tau = 2; tau < yinBuffer.length; tau++) {
			if (yinBuffer[tau] < threshold) {
				while (tau + 1 < yinBuffer.length && yinBuffer[tau + 1] < yinBuffer[tau]) {
					tau++;
				}
				// found tau, exit loop and return
				// store the probability
				// From the YIN paper: The threshold determines the list of
				// candidates admitted to the set, and can be interpreted as the
				// proportion of aperiodic power tolerated
				// within a periodic signal.
				//
				// Since we want the periodicity and and not aperiodicity:
				// periodicity = 1 - aperiodicity
				result.setProbability(1 - yinBuffer[tau]);
				break;
			}
		}

		
		// if no pitch found, tau => -1
		if (tau == yinBuffer.length || yinBuffer[tau] >= threshold) {
			tau = -1;
			result.setProbability(0);
			result.setPitched(false);	
		} else {
			result.setPitched(true);
		}

		return tau;
	}

	/**
	 * Implements step 5 of the AUBIO_YIN paper. It refines the estimated tau
	 * value using parabolic interpolation. This is needed to detect higher
	 * frequencies more precisely. See http://fizyka.umk.pl/nrbook/c10-2.pdf and
	 * for more background
	 * http://fedc.wiwi.hu-berlin.de/xplore/tutorials/xegbohtmlnode62.html
	 * 
	 * @param tauEstimate
	 *            The estimated tau value.
	 * @return A better, more precise tau value.
	 */
	private float parabolicInterpolation(final int tauEstimate) {
		final float betterTau;
		final int x0;
		final int x2;

		if (tauEstimate < 1) {
			x0 = tauEstimate;
		} else {
			x0 = tauEstimate - 1;
		}
		if (tauEstimate + 1 < yinBuffer.length) {
			x2 = tauEstimate + 1;
		} else {
			x2 = tauEstimate;
		}
		if (x0 == tauEstimate) {
			if (yinBuffer[tauEstimate] <= yinBuffer[x2]) {
				betterTau = tauEstimate;
			} else {
				betterTau = x2;
			}
		} else if (x2 == tauEstimate) {
			if (yinBuffer[tauEstimate] <= yinBuffer[x0]) {
				betterTau = tauEstimate;
			} else {
				betterTau = x0;
			}
		} else {
			float s0, s1, s2;
			s0 = yinBuffer[x0];
			s1 = yinBuffer[tauEstimate];
			s2 = yinBuffer[x2];
			// fixed AUBIO implementation, thanks to Karl Helgason:
			// (2.0f * s1 - s2 - s0) was incorrectly multiplied with -1
			betterTau = tauEstimate + (s2 - s0) / (2 * (2 * s1 - s2 - s0));
		}
		return betterTau;
	}
}
interface PitchDetector {
	/**
	 * Analyzes a buffer with audio information and estimates a pitch in Hz.
	 * Currently this interface only allows one pitch per buffer.
	 * 
	 * @param audioBuffer
	 *            The buffer with audio information. The information in the
	 *            buffer is not modified so it can be (re)used for e.g. FFT
	 *            analysis.
	 * @return An estimation of the pitch in Hz or -1 if no pitch is detected or
	 *         present in the buffer.
	 */
	PitchDetectionResult getPitch(final float[] audioBuffer);
}
class PitchDetectionResult {	
	/**
	 * The pitch in Hertz.
	 */
	private float pitch;
	
	private float probability;
	
	private boolean pitched;
	
	public PitchDetectionResult(){
		pitch = -1;
		probability = -1;
		pitched = false;
	}
	
	/**
	 * A copy constructor. Since PitchDetectionResult objects are reused for performance reasons, creating a copy can be practical.
	 * @param other
	 */
	public PitchDetectionResult(PitchDetectionResult other){
		this.pitch = other.pitch;
		this.probability = other.probability;
		this.pitched = other.pitched;
	}
		 
	
	/**
	 * @return The pitch in Hertz.
	 */
	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public PitchDetectionResult clone(){
		return new PitchDetectionResult(this);
	}

	/**
	 * @return A probability (noisiness, (a)periodicity, salience, voicedness or
	 *         clarity measure) for the detected pitch. This is somewhat similar
	 *         to the term voiced which is used in speech recognition. This
	 *         probability should be calculated together with the pitch. The
	 *         exact meaning of the value depends on the detector used.
	 */
	public float getProbability() {
		return probability;
	}

	public void setProbability(float probability) {
		this.probability = probability;
	}

	/**
	 * @return Whether the algorithm thinks the block of audio is pitched. Keep
	 *         in mind that an algorithm can come up with a best guess for a
	 *         pitch even when isPitched() is false.
	 */
	public boolean isPitched() {
		return pitched;
	}

	public void setPitched(boolean pitched) {
		this.pitched = pitched;
	}	
}

