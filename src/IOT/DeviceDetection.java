package IOT;

import java.io.IOException;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

/**
 * Checks on what device this application is running on.
 * 
 * Source: http://stackoverflow.com/a/39542949 on the 30th Oct. 2016
 *
 */
public class DeviceDetection {
	/** True, if application is running on Windows */
	private boolean isWin;
	/** True, if application is running on Mac */
	private boolean isMac;
	/** True, if application is running on Linux */
	private boolean isLin;
	/** True, if application is running on Raspberry Pi */
	private boolean isRasp;

	/**
	 * Checks OS name, architecture and hostname to find out which device the
	 * application is running on.
	 */
	public DeviceDetection() {

		/* Check for OS name */
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
		/* If it's a Linux distribution check if it's on a Raspberry Pi */
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
