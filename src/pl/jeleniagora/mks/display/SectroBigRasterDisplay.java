package pl.jeleniagora.mks.display;

import java.nio.charset.Charset;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.exceptions.DisplayFunctionNotSupportedEx;
import pl.jeleniagora.mks.rte.RTE_COM_DISP;

@Component
public class SectroBigRasterDisplay implements TextDisplayInterface {

	@Autowired
	@Qualifier("comDispBean")
	RTE_COM_DISP com;
	
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
		Vector<Byte> data = new Vector<Byte>();
		data.add(FRAME_START);
		data.add(CLEAR);
		data.add(FRAME_END);
		
		try {
			com.txBuferSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		com.txBuffer = new Vector<Byte>(data);
		
		com.txBuferSemaphore.release();
		com.activateTx = true;
		
	}

	@Override
	public void sendText(String text, int offsetCol, int offsetRow) {
		String lines[] = text.split("\\r?\\n"); // dzielenie wejściowego strina na linie
		
		if (offsetCol > 0) {
			StringBuilder builder = new StringBuilder();
			
			for (int i = 0; i < offsetCol; i++)
				builder.append(" ");
			
			/*
			 * Jeżeli tekst ma byź przesunięty w prawo o kilka kolumn do trzeba dociepać spacji na początek
			 */			
			for (int i = 0; i < 2; i++) { // wyświetlacz ma i tak tylko dwie linie rozdzielczości także można zignorować reszte				
				lines[i] += builder.toString();
				
			}
		}
		
		if (offsetRow > 0) {
			;
		}
		
		Vector<Byte> data = new Vector<Byte>();
		data.add(FRAME_START);
		data.add(TEXT_LL);
		data.add((byte)1);
		
		/*
		 * Typ String w Javie przechowuje tekst zakodowany jako UTF-16 (znaki 2 bajtowe), dlatego trzeba go przekodować na coś
		 * co jest zrozumiałe dla wyświetlacza
		 */
		byte[] line1st = (lines[0].getBytes(Charset.forName("ISO-8859-2")));
		
		for (int i = 0; i < line1st.length; i++)
			data.add(line1st[i]);		// dopisywanie pierwszej linijki
		
		data.add(FRAME_END);
		
		// komunikacja po RS
		try {
			com.txBuferSemaphore.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		com.txBuffer = new Vector<Byte>(data);
		
		com.txBuferSemaphore.release();
		com.activateTx = true;
	}

	@Override
	public void setBrightness(byte level) throws DisplayFunctionNotSupportedEx {
		// TODO Auto-generated method stub

	}

}
