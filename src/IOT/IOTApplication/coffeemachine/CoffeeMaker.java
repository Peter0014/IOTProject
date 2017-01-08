package IOT.IOTApplication.coffeemachine;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Turns on the Coffee Machine that is connected to the Raspberry Pi that this
 * application is running on.
 * 
 * @author Peter Klosowski (a1403029)
 * @version Milestone3
 *
 */

public class CoffeeMaker {

	/** Pin that the Piezo is connected to. */
	private final int COFFEE_PIN = 0;
	/** 1 minute in miliseconds */
	private final long ONE_MIN_MS = 60000;
	/** 5 minutes in miliseconds */
	private final long FIVE_MIN_MS = 300000;
	/** 15 minutes in miliseconds */
	private final long FIFTEEN_MIN_MS = 900000;
	
	/** Setup the wiring just on the first run to allow writing to pins. */
	static {
		com.pi4j.wiringpi.Gpio.wiringPiSetup();
	}
	
	public void turnOn() {
		// create gpio controller instance
		final GpioController gpio = GpioFactory.getInstance();
		
		/* Provision gpio pin # COFFEE_PIN as an output pin */
		GpioPinDigitalOutput coffeeMaker = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(COFFEE_PIN), "CoffeeMaker");
		
		if (coffeeMaker.isHigh()) {
			return;
		}
		
		coffeeMaker.high();
		try {
			Thread.sleep(FIVE_MIN_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			coffeeMaker.low();
		}
		coffeeMaker.low();
	}
}
