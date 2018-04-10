package pl.jeleniagora.mks.settings;

public class SerialCommS {

	private String serialPort;
	
	private static int maxRxTimeoutMsec = 1000;
	
	/**
	 * Ustawienie na true spowoduje że po wystąpieniu timeout odbiór danych będzie automatycznie
	 * wznawiany z tymi samymi ustawieniami.
	 */
	private static boolean autoRestartAfterTimeout = true;
	
	private static boolean detectTimeoutAfterFirstByte = true;
	
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

	public static int getMaxRxTimeoutMsec() {
		return maxRxTimeoutMsec;
	}

	public static void setMaxRxTimeoutMsec(int maxRxTimeoutMsec) {
		SerialCommS.maxRxTimeoutMsec = maxRxTimeoutMsec;
	}

	public static boolean isAutoRestartAfterTimeout() {
		return autoRestartAfterTimeout;
	}

	public static void setAutoRestartAfterTimeout(boolean autoRestartAfterTimeout) {
		SerialCommS.autoRestartAfterTimeout = autoRestartAfterTimeout;
	}

	public static boolean isDetectTimeoutAfterFirstByte() {
		return detectTimeoutAfterFirstByte;
	}

	public static void setDetectTimeoutAfterFirstByte(boolean detectTimeoutAfterFirstByte) {
		SerialCommS.detectTimeoutAfterFirstByte = detectTimeoutAfterFirstByte;
	}
}
