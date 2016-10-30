package IOT;

import java.io.IOException;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

/**
 * 
 * Source: http://stackoverflow.com/a/39542949 on the 30th Oct. 2016
 * 
 * @author peter
 *
 */
public class DeviceDetection {
	private boolean isWin;
	private boolean isMac;
	private boolean isLin;
	private boolean isRasp;

	public DeviceDetection() {

		final String osName = SystemInfo.getOsName().toLowerCase();
		if (osName.indexOf("windows") >= 0) {
			isWin = true;
		} else {
			isWin = false;
		}
		if (osName.indexOf("linux") >= 0) {
			isLin = true;
		} else {
			isLin = false;
		}
		if (osName.indexOf("mac") >= 0) {
			isMac = true;
		} else {
			isMac = false;
		}

		isRasp = false;
		if (isLin) {
			try {
				final String osArch = SystemInfo.getOsArch().toLowerCase();
				final String hostName = NetworkInfo.getHostname().toLowerCase();
				if (osArch.startsWith("arm") && hostName.startsWith("raspberrypi")) {
					isRasp = true;
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

		}
		System.out.println("Windows: " + isWin);
		System.out.println("Mac: " + isMac);
		System.out.println("Linux: " + isLin);
		System.out.println("Raspberry Pi: " + isRasp);
	}

	public boolean isWin() {
		return isWin;
	}

	public boolean isMac() {
		return isMac;
	}

	public boolean isLin() {
		return isLin;
	}

	public boolean isRasp() {
		return isRasp;
	}
}
