package pl.jeleniagora.mks.display;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;
import pl.jeleniagora.mks.rte.RTE_COM;

@Component
public class SectroBigRasterDisplay implements TextDisplayInterface {

	@Autowired
	RTE_COM com;
	
	final static byte FRAME_START = 0x02;
	final static byte FRAME_END = 0x03;
	
	final static byte TEXT_LL = 0x13;
	final static byte CLEAR = 0x17;
	final static byte TEXT_L1 = 0x11;
	final static byte TEXT_L2 = 0x12;
	final static byte TEXT_BIG = 0x13;
	final static byte LF = 0x0A;
	final static byte CR = 0x0D;
	
	final static byte TIME_DISP = 0x15;
	final static byte TEMP_DISP = 0x16;
	final static byte TIME_SET = 0x18;
	final static byte MODE = 0x19;
	final static byte ECHO = 0x1A;
	final static byte BRIGHT = 0x1C;
	
	
	@Override
	public int getColumnRes() {
		return 16;
	}

	@Override
	public int getRowRes() {
		return 2;
	}

	@Override
	public void clearDisplay() {
		
		
	}

	@Override
	public void sendText(String text, int offsetCol, int offsetRow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBrightness(byte level) throws DisplayFunctionNotSupportedEx {
		// TODO Auto-generated method stub

	}

}
