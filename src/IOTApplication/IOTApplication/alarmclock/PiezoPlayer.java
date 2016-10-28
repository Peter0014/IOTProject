package IOTApplication.IOTApplication.alarmclock;

import com.pi4j.wiringpi.SoftTone;

public class PiezoPlayer {
	
	private final int PIEZO_PIN = 0;
	
	static {
		com.pi4j.wiringpi.Gpio.wiringPiSetup();
	}
	
	private final int[][] MARIO_THEME = 
		{ 
			{ 
				2637, 2637, 0, 2637,
				0, 2093, 2637, 0,
				3136, 0, 0, 0,
				1568, 0, 0, 0,
				
				2093, 0, 0, 1568,
				0, 0, 1319, 0,
				0, 1760, 0, 1976,
				0, 1865, 1760, 0,

				1568, 2637, 3136,
				3520, 0, 2794, 3136,
				0, 2637, 0, 2093,
				2349, 1976, 0, 0,

				2093, 0, 0, 1568,
				0, 0, 1319, 0,
				0, 1760, 0, 1976,
				0, 1865, 1760, 0,

				1568, 2637, 3136,
				3520, 0, 2794, 3136,
				0, 2637, 0, 2093,
				2349, 1976, 0, 0
			},
			
			{ 
				12, 12, 12, 12,
				12, 12, 12, 12,
				12, 12, 12, 12,
				12, 12, 12, 12,

				12, 12, 12, 12,
				12, 12, 12, 12,
				12, 12, 12, 12,
				12, 12, 12, 12,

				9, 9, 9,
				12, 12, 12, 12,
				12, 12, 12, 12,
				12, 12, 12, 12,

				12, 12, 12, 12,
				12, 12, 12, 12,
				12, 12, 12, 12,
				12, 12, 12, 12,

				9, 9, 9,
				12, 12, 12, 12,
				12, 12, 12, 12,
				12, 12, 12, 12, }
		};
	
	public void playTune(int melody) {
		if (melody < 0) {
			return;
		}
		int [][] tune;
		switch (melody) {
		default: 
		case 0: 
			tune = MARIO_THEME;
			break;
		}
		SoftTone.softToneCreate(PIEZO_PIN);
		
		for (int i = 0; i < tune[0].length; i++) {
			System.out.println("Sounding now: " + tune[0][i]);
			SoftTone.softToneWrite(PIEZO_PIN, tune[0][i]);
			try {
				System.out.println("Waiting now: " + 1000 / tune[1][i]);
				Thread.sleep(1000 / tune[1][i]);
				System.out.println("Sounding now: 0");
				SoftTone.softToneWrite(PIEZO_PIN, 0);
				System.out.println("Waiting now: " + (int) (1000 / tune[1][i] * 1.3));
				Thread.sleep((int) (1000 / tune[1][i] * 1.3));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SoftTone.softToneWrite(PIEZO_PIN, 0);
		SoftTone.softToneStop(PIEZO_PIN);
		System.out.println("Finished now!");
		
	}
}
