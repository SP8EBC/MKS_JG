package pl.jeleniagora.mks.settings;

public class SerialCommS {

	private String serialPort;
	
	/**
	 * Długość bufora odbiorczego i nadawczego dla portu szeregowego do komunikacji z chronometrem
	 */
	private static int serialBufferLn = 255;

	public String getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(String serialPort) {
		this.serialPort = serialPort;
	}

	public static int getSerialBufferLn() {
		return serialBufferLn;
	}

	public static void setSerialBufferLn(int serialBufferLn) {
		SerialCommS.serialBufferLn = serialBufferLn;
	}
}
