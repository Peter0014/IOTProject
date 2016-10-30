package IOT.IOTApplication.alarmclock;

import com.pi4j.wiringpi.SoftTone;

/**
 * Plays tunes on a Piezo that is connected to the Raspberry Pi that this
 * application is running on.
 * 
 * Sources: Mario Theme: http://www.princetronics.com/supermariothemesong/ on
 * the 28th Oct 2016
 * 
 * @author Peter Klosowski (a1403029)
 * @version Milestone1
 *
 */
public class PiezoPlayer {

	/** Pin that the Piezo is connected to. */
	private final int PIEZO_PIN = 0;

	/** Setup the wiring just on the first run to allow writing to pins. */
	static {
		com.pi4j.wiringpi.Gpio.wiringPiSetup();
	}

	/** Frequences[0][x] that are written onto the Piezo and duration[1][x]. */
	private final int[][] MARIO_THEME = { { 2637, 2637, 0, 2637, 0, 2093, 2637, 0, 3136, 0, 0, 0, 1568, 0, 0, 0,

			2093, 0, 0, 1568, 0, 0, 1319, 0, 0, 1760, 0, 1976, 0, 1865, 1760, 0,

			1568, 2637, 3136, 3520, 0, 2794, 3136, 0, 2637, 0, 2093, 2349, 1976, 0, 0,

			2093, 0, 0, 1568, 0, 0, 1319, 0, 0, 1760, 0, 1976, 0, 1865, 1760, 0,

			1568, 2637, 3136, 3520, 0, 2794, 3136, 0, 2637, 0, 2093, 2349, 1976, 0, 0 },

			{ 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,

					12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,

					9, 9, 9, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,

					12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,

					9, 9, 9, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, } };

	/**
	 * Plays a melody that was preconfigured onto the piezo element at
	 * {@value PiezoPlayer#PIEZO_PIN}.
	 * 
	 * @param melody
	 *            Track number of predefined melody, 0 for
	 *            {@value PiezoPlayer#MARIO_THEME}.
	 */
	public void playTune(int melody) {
		/* Select melody first */
		int[][] tune;
		switch (melody) {
		default:
		case 0:
			tune = MARIO_THEME;
			break;
		}
		/* Activate Pin */
		SoftTone.softToneCreate(PIEZO_PIN);

		/* Play melody */
		for (int i = 0; i < tune[0].length; i++) {
			/* Write frequency from array to piezo pin */
			SoftTone.softToneWrite(PIEZO_PIN, tune[0][i]);
			try {
				/* Play tune for */
				Thread.sleep(1000 / tune[1][i]);
				/* Write 0 and cancel any sound */
				SoftTone.softToneWrite(PIEZO_PIN, 0);
				/* Break for duration * 1.3 before playing next tune */
				Thread.sleep((int) (1000 / tune[1][i] * 1.3));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/* Stop any sounds and close pin again */
		SoftTone.softToneWrite(PIEZO_PIN, 0);
		SoftTone.softToneStop(PIEZO_PIN);
	}
}
